package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.vorwerk.VorwerkCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class VorwerkTransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(VorwerkTransferWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        VorwerkCommunicator vorwerkCommunicator = new VorwerkCommunicator(compilerWorkflowBean.getCompilerResourcesDir());
        final String tempDir = compilerWorkflowBean.getTempDir();
        try {
            String programLocation = tempDir + project.getToken() + File.separator + project.getProgramName() + File.separator + "source";
            vorwerkCommunicator.uploadFile(programLocation, project.getProgramName() + "." + project.getFileExtension());
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        } catch ( Exception e ) {
            LOG.error("Uploading the generated program to {} failed", vorwerkCommunicator.getIp(), e);
            project.setResult(Key.VORWERK_PROGRAM_UPLOAD_ERROR);
        }
    }
}
