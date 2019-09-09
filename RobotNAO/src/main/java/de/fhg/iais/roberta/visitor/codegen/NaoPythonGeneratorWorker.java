package de.fhg.iais.roberta.visitor.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class NaoPythonGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(NaoPythonGeneratorWorker.class);

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        NaoPythonVisitor visitor =
            new NaoPythonVisitor(
                (UsedHardwareBean) usedHardwareBean,
                (CodeGeneratorSetupBean) codeGeneratorSetupBean,
                project.getConfigurationAst(),
                project.getProgramAst().getTree(),
                0,
                project.getLanguage());
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(true);
    }
}
