package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;

public final class NxtSimValidatorVisitor extends AbstractSimValidatorVisitor {

    public NxtSimValidatorVisitor(UsedHardwareBean.Builder builder, ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }
}
