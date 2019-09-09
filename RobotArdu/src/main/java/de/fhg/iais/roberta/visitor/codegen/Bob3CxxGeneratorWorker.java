package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.transformer.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class Bob3CxxGeneratorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        Bob3CppVisitor visitor =
            new Bob3CppVisitor((UsedHardwareBean) usedHardwareBean, (CodeGeneratorSetupBean) codeGeneratorSetupBean, project.getProgramAst().getTree(), 1);
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(true);
    }
}
