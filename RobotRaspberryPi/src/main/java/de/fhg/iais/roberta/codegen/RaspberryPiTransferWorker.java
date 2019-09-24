package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.raspberrypi.RaspberryPiCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class RaspberryPiTransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(RaspberryPiTransferWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String ip = compilerWorkflowBean.getIp();
        RaspberryPiCommunicator communicator = new RaspberryPiCommunicator(project.getRobotFactory().getPluginProperties());
        try {
            String programLocation = tempDir + project.getToken() + File.separator + project.getProgramName() + File.separator + "source";
            communicator.uploadFile(programLocation, project.getProgramName());
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        } catch ( Exception e ) {
            LOG.error("Uploading the generated program to {} failed", ip, e);
            project.setResult(Key.RASPBERRY_PROGRAM_UPLOAD_ERROR);
        }
    }
}
