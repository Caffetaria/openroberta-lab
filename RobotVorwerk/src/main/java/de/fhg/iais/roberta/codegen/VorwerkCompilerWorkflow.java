package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.vorwerk.VorwerkCommunicator;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class VorwerkCompilerWorkflow implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(VorwerkCompilerWorkflow.class);

    private final VorwerkCommunicator vorwerkCommunicator;

    private final HelperMethodGenerator helperMethodGenerator; // TODO pull up to abstract compiler workflow once implemented for all robots

    public VorwerkCompilerWorkflow(PluginProperties pluginProperties, HelperMethodGenerator helperMethodGenerator) {
        this.vorwerkCommunicator = new VorwerkCommunicator(pluginProperties.getCompilerResourceDir());
        this.helperMethodGenerator = helperMethodGenerator;
    }

    @Override
    public void execute(Project project) {
        // TODO Auto-generated method stub

    }

    public Key compileSourceCode(Project project, String sourceCode, String token, String programName, ILanguage language, Object flagProvider) {
        Util1.storeGeneratedProgram(sourceCode, token, programName, ".py");
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String tempDir = compilerWorkflowBean.getTempDir();
        try {
            String programLocation = tempDir + token + File.separator + programName + File.separator + "source";
            this.vorwerkCommunicator.uploadFile(programLocation, programName + ".py");
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            LOG.error("Uploading the generated program to " + this.vorwerkCommunicator.getIp() + " failed", e.getCause());
            return Key.VORWERK_PROGRAM_UPLOAD_ERROR;
        }
    }
}
