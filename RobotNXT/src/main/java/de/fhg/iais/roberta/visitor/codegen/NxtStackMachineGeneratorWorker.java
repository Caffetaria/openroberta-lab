package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class NxtStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(UsedHardwareBean usedHardwareBean,
                                                           Project project) {
        return new NxtStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
