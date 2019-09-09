package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;

public final class NxtBrickValidatorVisitor extends AbstractBrickValidatorVisitor {

    public NxtBrickValidatorVisitor(UsedHardwareBean.Builder builder, ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }

}
