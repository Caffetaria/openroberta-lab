package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class MbotCompilerWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(MbotCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        String robot = project.getRobot();
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), programName, "." + project.getFileExtension());
        Pair<Key, String> workflowResult = runBuild(project);
        project.setResult(workflowResult.getFirst());
        project.addResultParam("MESSAGE", workflowResult.getSecond());
        if ( workflowResult.getFirst() == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile {} program {} successful", robot, programName);
        } else {
            LOG.error("compile {} program {} failed with {}", robot, programName, workflowResult);
        }
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return a pair of Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED and the cross compiler output
     */
    private Pair<Key, String> runBuild(Project project) {
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
        Path path = Paths.get(tempDir + project.getToken() + "/" + project.getProgramName());
        Path base = Paths.get("");

        String fqbnArg = "-fqbn=arduino:avr:uno";
        String[] executableWithParameters =
            {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-libraries=" + compilerResourcesDir + "/libraries",
                fqbnArg,
                "-prefs=compiler.path=" + compilerBinDir,
                "-build-path=" + base.resolve(path).toAbsolutePath().normalize() + "/target/",
                base.resolve(path).toAbsolutePath().normalize() + "/source/" + project.getProgramName() + "." + project.getFileExtension()
            };

        Pair<Boolean, String> result = AbstractCompilerWorkflow.runCrossCompiler(executableWithParameters);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            project.setCompiledHex(AbstractCompilerWorkflow.getBase64EncodedHex(path + "/target/" + project.getProgramName() + ".ino.hex"));
            if ( project.getCompiledHex() != null ) {
                resultKey = Key.COMPILERWORKFLOW_SUCCESS;
            } else {
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
        return Pair.of(resultKey, result.getSecond());
    }
}