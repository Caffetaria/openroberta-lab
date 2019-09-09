package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ev3lejos.JavaSourceCompiler;
import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class Ev3LejosCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3LejosCompilerWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), project.getProgramName(), ".java");

        JavaSourceCompiler scp = new JavaSourceCompiler(project.getProgramName(), project.getSourceCode().toString(), compilerResourcesDir);
        scp.compileAndPackage(tempDir, project.getToken());
        if ( scp.isSuccess() ) {
            LOG.info("jar for program {} generated successfully", project.getProgramName());
            project.getErrorMessages().add(Key.COMPILERWORKFLOW_SUCCESS);
        } else {
            LOG.error("build exception. Messages from the compiler are:\n" + scp.getCompilerResponse());
            project.getErrorMessages().add(Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED);
        }
    }
}
