package de.fhg.iais.roberta.visitor.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class Ev3CxxGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3CxxGeneratorWorker.class);

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        Ev3C4ev3Visitor visitor =
            new Ev3C4ev3Visitor(
                (UsedHardwareBean) usedHardwareBean,
                (CodeGeneratorSetupBean) codeGeneratorSetupBean,
                project.getProgramName(),
                project.getConfigurationAst(),
                project.getProgramAst().getTree(),
                project.getLanguage());
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(true);
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }
}
