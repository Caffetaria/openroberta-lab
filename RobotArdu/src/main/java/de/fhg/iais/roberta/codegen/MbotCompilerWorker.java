package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class MbotCompilerWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(MbotCompilerWorker.class);

    @Override
    public void execute(Project project) {
        // TODO Auto-generated method stub

    }

    /**
     * 1. Make target folder (if not exists).<br>
     * 2. Clean target folder (everything inside).<br>
     * 3. Compile .java files to .class.<br>
     * 4. Make jar from class files and add META-INF entries.<br>
     *
     * @param token
     * @param mainFile
     * @param mainPackage
     */
    private Key runBuild(Project project, String token, String mainFile, String mainPackage) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();

        final StringBuilder sb = new StringBuilder();
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

        String fqbnArg = "-fqbn=arduino:avr:uno";
        String[] executableWithParameters =
            new String[] {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-libraries=" + compilerResourcesDir + "/libraries",
                fqbnArg,
                "-prefs=compiler.path=" + compilerBinDir,
                "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + mainFile + ".ino"
            };
        boolean success = AbstractCompilerWorkflow.runCrossCompilerNoResponse(executableWithParameters);
        if ( success ) {
            project.setCompiledHex(AbstractCompilerWorkflow.getBase64EncodedHex(path + "/target/" + project.getProgramName() + ".ino.hex"));
            if ( project.getCompiledHex() != null ) {
                return Key.COMPILERWORKFLOW_SUCCESS;
            } else {
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}