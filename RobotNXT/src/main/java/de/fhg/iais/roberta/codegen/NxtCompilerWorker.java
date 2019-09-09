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

public class NxtCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(NxtCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), programName, ".nxc");
        Key workflowResult = runBuild(project);
        project.getErrorMessages().add(workflowResult);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile arduino program {} successful", programName);
        } else {
            LOG.error("compile arduino program {} failed with {}", programName, workflowResult);
        }
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
    Key runBuild(Project project) {
        String token = project.getToken();
        String mainFile = project.getProgramName();
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();

        Path path = Paths.get(compilerResourcesDir);
        Path base = Paths.get("");

        String nbcCompilerFileName = compilerResourcesDir + "/windows/nbc.exe";
        if ( SystemUtils.IS_OS_LINUX ) {
            nbcCompilerFileName = "nbc";
        } else if ( SystemUtils.IS_OS_MAC ) {
            nbcCompilerFileName = compilerResourcesDir + "/osx/nbc";
        }

        String[] executableWithParameters =
            new String[] {
                nbcCompilerFileName,
                "-q",
                "-sm-",
                tempDir + token + "/" + mainFile + "/source/" + mainFile + ".nxc",
                "-O=" + tempDir + token + "/" + mainFile + "/target/" + mainFile + ".rxe",
                "-I=" + base.resolve(path).toAbsolutePath().normalize().toString()
            };
        return AbstractCompilerWorkflow.runCrossCompilerNoResponse(executableWithParameters)
            ? Key.COMPILERWORKFLOW_SUCCESS
            : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
    }
}
