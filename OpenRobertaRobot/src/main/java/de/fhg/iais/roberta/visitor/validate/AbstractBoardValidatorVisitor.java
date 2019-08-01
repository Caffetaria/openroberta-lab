package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;

public abstract class AbstractBoardValidatorVisitor extends AbstractProgramValidatorVisitor {

    public AbstractBoardValidatorVisitor(ConfigurationAst brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        // Do nothing
    }

    @Override
    protected void checkMotorPort(MoveAction<Void> action) {
        // Do nothing
    }
}
