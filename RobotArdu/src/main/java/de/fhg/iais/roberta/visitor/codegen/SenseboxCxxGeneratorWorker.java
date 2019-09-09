package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.transformer.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class SenseboxCxxGeneratorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        SenseboxCppVisitor visitor =
            new SenseboxCppVisitor(
                (UsedHardwareBean) usedHardwareBean,
                (CodeGeneratorSetupBean) codeGeneratorSetupBean,
                project.getConfigurationAst(),
                project.getProgramAst().getTree(),
                project.getSSID(),
                project.getPassword(),
                1);
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(true);
    }
}
