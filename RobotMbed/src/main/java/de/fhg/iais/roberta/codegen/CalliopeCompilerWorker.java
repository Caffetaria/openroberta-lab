package de.fhg.iais.roberta.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class CalliopeCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(CalliopeCompilerWorker.class);

    protected String crosscompilerResponse = "";

    @Override
    public void execute(Project project) {
        Key workflowResult = null;
        UsedHardwareBean usedHardwareBean = (UsedHardwareBean) project.getWorkerResult("CollectedHardware");
        EnumSet<CalliopeCompilerFlag> compilerFlags =
            usedHardwareBean.isRadioUsed() ? EnumSet.of(CalliopeCompilerFlag.RADIO_USED) : EnumSet.noneOf(CalliopeCompilerFlag.class);
        compileSourceCode(
            project,
            workflowResult,
            project.getToken(),
            project.getProgramName(),
            project.getLanguage(),
            compilerFlags,
            project.getSourceCode().toString());
        project.getErrorMessages().add(workflowResult);
    }

    public void compileSourceCode(
        Project project,
        Key workflowResult,
        String token,
        String programName,
        ILanguage language,
        Object flagProvider,
        String generatedSourceCode) {
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
        workflowResult = runBuild(project, token, programName, "generated.main", isRadioUsed);
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

    private Key runBuild(Project project, String token, String mainFile, String mainPackage, boolean radioUsed) {
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
        boolean success = AbstractCompilerWorkflow.runCrossCompilerNoResponse(executableWithParameters);
        if ( success ) {
            project.setCompiledHex(AbstractCompilerWorkflow.getBase64EncodedHex(pathToSrcFile + "/target/" + project.getProgramName() + ".ino.hex"));
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
