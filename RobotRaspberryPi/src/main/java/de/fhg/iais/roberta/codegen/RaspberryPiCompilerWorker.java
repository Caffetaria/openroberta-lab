package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.raspberrypi.RaspberryPiCommunicator;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class RaspberryPiCompilerWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(RaspberryPiCompilerWorker.class);

    private final RaspberryPiCommunicator communicator;

    public RaspberryPiCompilerWorker(PluginProperties pluginProperties, HelperMethodGenerator helperMethodGenerator) {
        this.communicator = new RaspberryPiCommunicator(pluginProperties);
    }

    public Key compileSourceCode(Project project, String token, String programName, ILanguage language, Object flagProvider) {
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), token, programName, ".py");
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String ip = compilerWorkflowBean.getIp();
        try {
            String programLocation = tempDir + token + File.separator + programName + File.separator + "source";
            this.communicator.uploadFile(programLocation, programName);
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            LOG.error("Uploading the generated program to " + ip + " failed", e.getMessage());
            return Key.RASPBERRY_PROGRAM_UPLOAD_ERROR;
        }
    }

    @Override
    public void execute(Project project) {
        // TODO Auto-generated method stub

    }
}
