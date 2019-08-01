package de.fhg.iais.roberta.visitor.collect;

import java.util.Set;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;

public class WedoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor {

    public WedoUsedHardwareCollectorVisitor(ConfigurationAst configuration) {
        super(configuration);
    }

    public Set<UsedSensor> getTimer() {
        return this.usedSensors;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().visit(this);
        }
        return null;
    }
}
