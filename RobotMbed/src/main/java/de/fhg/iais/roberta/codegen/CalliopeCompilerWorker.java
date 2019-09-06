package de.fhg.iais.roberta.codegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.MbedUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class CalliopeCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(CalliopeCompilerWorker.class);

    protected String crosscompilerResponse = "";

    private String compiledHex;

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execute(Project project) {
        Key workflowResult = null;
        MbedUsedHardwareCollectorVisitor usedHardwareVisitor =
            new MbedUsedHardwareCollectorVisitor(project.getProgramAst().getTree(), project.getConfigurationAst());
        EnumSet<CalliopeCompilerFlag> compilerFlags =
            usedHardwareVisitor.isRadioUsed() ? EnumSet.of(CalliopeCompilerFlag.RADIO_USED) : EnumSet.noneOf(CalliopeCompilerFlag.class);
        compileSourceCode(
            workflowResult,
            project.getToken(),
            project.getProgramName(),
            project.getLanguage(),
            compilerFlags,
            project.getSourceCode().toString());
        project.getErrorMessages().add(workflowResult);
        project.setCompiledHex(this.compiledHex);
    }

    @Override
    public Map<String, String> getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Key getResultKey() {
        // TODO Auto-generated method stub
        return null;
    }

    public void compileSourceCode(Key workflowResult, String token, String programName, ILanguage language, Object flagProvider, String generatedSourceCode) {
        storeGeneratedProgram(token, programName, ".cpp", generatedSourceCode);
        boolean isRadioUsed;
        if ( flagProvider == null ) {
            isRadioUsed = false;
        } else if ( flagProvider instanceof EnumSet<?> ) {
            EnumSet<?> flags = (EnumSet<?>) flagProvider;
            isRadioUsed = flags.contains(CalliopeCompilerFlag.RADIO_USED);
        } else {
            isRadioUsed = false;
        }
        workflowResult = runBuild(token, programName, "generated.main", isRadioUsed);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile calliope program {} successful", programName);
        } else {
            LOG.error("compile calliope program {} failed with {}", programName, workflowResult);
        }
    }

    protected final void storeGeneratedProgram(String token, String programName, String ext, String generatedSourceCode) {
        try {
            String tempDir = "/tmp/";
            Assert.isTrue(token != null && programName != null && generatedSourceCode != null);
            File sourceFile = new File(tempDir + token + "/" + programName + "/source/" + programName + ext);
            Path path = Paths.get(tempDir + token + "/" + programName + "/target/");
            try {
                Files.createDirectories(path);
                FileUtils.writeStringToFile(sourceFile, generatedSourceCode, StandardCharsets.UTF_8.displayName());
            } catch ( IOException e ) {
                String msg = "could not write source code to file system";
                LOG.error(msg, e);
                throw new DbcException(msg, e);
            }
            LOG.info("stored under: " + sourceFile.getPath());
        } catch ( Exception e ) {
            LOG.error("Storing the generated program " + programName + " into directory " + token + " failed", e);
        }
    }

    private Key runBuild(String token, String mainFile, String mainPackage, boolean radioUsed) {
        final String compilerResourcesDir = System.getenv("robot_crosscompiler_resourcebase") + "RobotMbed/libs2017/";
        String tempDir = "/tmp/";

        String scriptName = compilerResourcesDir + "../compile." + (SystemUtils.IS_OS_WINDOWS ? "bat" : "sh");
        String bluetooth = radioUsed ? "" : "-b";
        Path pathToSrcFile = Paths.get(tempDir + token + "/" + mainFile);

        String compilerBinDir = "";
        String[] executableWithParameters =
            new String[] {
                scriptName,
                compilerBinDir,
                mainFile,
                Paths.get("").resolve(pathToSrcFile).toAbsolutePath().normalize().toString() + "/",
                compilerResourcesDir,
                bluetooth
            };
        boolean success = runCrossCompiler(executableWithParameters);
        if ( success ) {
            try {
                this.compiledHex = FileUtils.readFileToString(new File(pathToSrcFile + "/target/" + mainFile + ".hex"), "UTF-8");
                return Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( IOException e ) {
                LOG.error("compilation of Calliope program successful, but reading the binary failed", e);
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

    protected final boolean runCrossCompiler(String[] executableWithParameters) {
        int ecode = -1;
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(executableWithParameters);
            procBuilder.redirectErrorStream(true);
            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.PIPE);
            Process p = procBuilder.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            this.crosscompilerResponse = sj.toString();
            ecode = p.waitFor();
            p.destroy();
        } catch ( Exception e ) {
            this.crosscompilerResponse = "exception when calling the cross compiler";
            LOG.error(this.crosscompilerResponse, e);
            ecode = -1;
        }
        LOG.error("DEBUG INFO: " + this.crosscompilerResponse);
        if ( ecode == 0 ) {
            return true;
        } else {
            LOG.error("compilation of program failed with message: \n" + this.crosscompilerResponse);
            return false;
        }
    }

}
