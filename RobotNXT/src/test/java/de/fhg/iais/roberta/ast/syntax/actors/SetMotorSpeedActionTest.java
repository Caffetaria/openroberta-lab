package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetMotorSpeedActionTest extends AstTest {

    @Test
    public void setMotorSpeed() throws Exception {
        final String a = "OnFwdRegEx(OUT_B,SpeedTest(30),OUT_REGMODE_SPEED,RESET_NONE);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorSetPower.xml");
    }
}