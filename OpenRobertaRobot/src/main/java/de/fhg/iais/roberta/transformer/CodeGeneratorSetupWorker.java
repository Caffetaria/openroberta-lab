package de.fhg.iais.roberta.transformer;

import de.fhg.iais.roberta.visitor.collect.UsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class CodeGeneratorSetupWorker implements IWorker {

    @Override
    public void execute(Project project) {
        CodeGeneratorSetupBean.Builder builder = new CodeGeneratorSetupBean.Builder();
        UsedMethodCollectorVisitor visitor = new UsedMethodCollectorVisitor(builder);
        project.getProgramAst().accept(visitor);
        project.addWorkerResult("CodeGeneratorSetup", builder.build());
    }

}
