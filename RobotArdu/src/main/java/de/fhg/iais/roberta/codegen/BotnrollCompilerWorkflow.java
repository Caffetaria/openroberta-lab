package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformers.arduino.Jaxb2BotNrollConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.BotnrollCppVisitor;

public class BotnrollCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(MbotCompilerWorkflow.class);

    private String compiledHex = "";

    public BotnrollCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            this.generatedSourceCode = BotnrollCppVisitor.generate(data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true);
            LOG.info("botnroll c++ code generated");
        } catch ( Exception e ) {
            LOG.error("botnroll c++ code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        this.storeGeneratedProgram(token, programName, ".ino");
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            this.workflowResult = this.runBuild(token, programName, "generated.main");
            if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
                LOG.info("compile botnroll program {} successful", programName);
            } else {
                LOG.error("compile botnroll program {} failed with {}", programName, this.workflowResult);
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2BotNrollConfigurationTransformer transformer = new Jaxb2BotNrollConfigurationTransformer(factory.getBlocklyDropdownFactory());
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.compiledHex;
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED
     */
    private Key runBuild(String token, String mainFile, String mainPackage) {
        final String compilerBinDir = this.pluginProperties.getCompilerBinDir();
        final String compilerResourcesDir = this.pluginProperties.getCompilerResourceDir();
        final String tempDir = this.pluginProperties.getTempDir();

        String scriptName = "";
        String os = "";
        if ( SystemUtils.IS_OS_LINUX ) {
            if ( System.getProperty("os.arch").contains("arm") ) {
                scriptName = compilerResourcesDir + "arduino-builder/linux-arm/arduino-builder";
                os = "arduino-builder/linux-arm";
            } else {
                scriptName = compilerResourcesDir + "arduino-builder/linux/arduino-builder";
                os = "arduino-builder/linux";
            }
        } else if ( SystemUtils.IS_OS_WINDOWS ) {
            scriptName = compilerResourcesDir + "arduino-builder/windows/arduino-builder.exe";
            os = "arduino-builder/windows";
        } else if ( SystemUtils.IS_OS_MAC ) {
            scriptName = compilerResourcesDir + "arduino-builder/osx/arduino-builder";
            os = "arduino-builder/osx";
        }

        Path path = Paths.get(tempDir + token + "/" + mainFile);
        Path base = Paths.get("");

        String[] executableWithParameters =
            new String[] {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-libraries=" + compilerResourcesDir + "/libraries",
                "-fqbn=arduino:avr:uno",
                "-prefs=compiler.path=" + compilerBinDir,
                "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + mainFile + ".ino"
            };
        boolean success = runCrossCompiler(executableWithParameters);
        if ( success ) {
            this.compiledHex = getBase64EncodedHex(path + "/target/" + mainFile + ".ino.hex");
            return this.compiledHex == null ? Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED : Key.COMPILERWORKFLOW_SUCCESS;
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
