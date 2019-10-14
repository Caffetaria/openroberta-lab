package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;

public class CalliopeSimValidatorWorker extends AbstractValidatorWorker {

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project) {
        return new CalliopeSimValidatorVisitor(builder, project.getConfigurationAst());
    }

    @Override
    protected String getBeanName() {
        return "SimValidator";
    }
}
