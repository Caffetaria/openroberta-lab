package de.fhg.iais.roberta.transformer;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.bean.CompilerSetupBean.Builder;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class CompilerSetupWorker implements IWorker {

    @Override
    public void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();
        CompilerSetupBean.Builder builder = new Builder();
        builder.setCompilerBinDir(properties.getCompilerBinDir());
        builder.setCompilerResourcesDir(properties.getCompilerResourceDir());
        builder.setTempDir(properties.getTempDir());
        CompilerSetupBean compilerWorkflowBean = builder.build();
        project.addWorkerResult("CompilerSetup", compilerWorkflowBean);
    }

}
