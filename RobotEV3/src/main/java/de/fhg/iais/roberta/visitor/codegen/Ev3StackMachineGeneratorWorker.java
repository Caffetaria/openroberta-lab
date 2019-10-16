package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class Ev3StackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(UsedHardwareBean usedHardwareBean, Project project) {
        return new Ev3StackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree(), project.getLanguage());
    }
}
