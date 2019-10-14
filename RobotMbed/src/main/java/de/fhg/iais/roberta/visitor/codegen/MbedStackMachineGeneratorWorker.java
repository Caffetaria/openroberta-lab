package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbedStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(UsedHardwareBean usedHardwareBean,
                                                           Project project) {
        return new MbedStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
