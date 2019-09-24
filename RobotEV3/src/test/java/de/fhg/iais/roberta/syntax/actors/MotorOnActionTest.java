package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorOnActionTest extends AstTest {

    @Test
    public void motorOn() throws Exception {
        String a = "hal.turnOnRegulatedMotor(ActorPort.B, 30);" + "hal.turnOnUnregulatedMotor(ActorPort.C, 50);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 1);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_MotorOnFor.xml");
    }
}