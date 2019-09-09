package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class ArduinoCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), programName, ".ino");
        Key workflowResult = runBuild(project);
        project.getErrorMessages().add(workflowResult);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile arduino program {} successful", programName);
        } else {
            LOG.error("compile arduino program {} failed with {}", programName, workflowResult);
        }
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED
     */
    private Key runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();

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
        final Path path = Paths.get(tempDir + project.getToken() + "/" + project.getProgramName());
        final Path base = Paths.get("");

        String[] executableWithParameters =
            new String[] {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-libraries=" + compilerResourcesDir + "/libraries",
                compilerWorkflowBean.getFqbn(),
                "-prefs=compiler.path=" + compilerBinDir,
                "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + project.getProgramName() + ".ino"
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
