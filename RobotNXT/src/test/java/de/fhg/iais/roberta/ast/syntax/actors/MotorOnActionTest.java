package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorOnActionTest extends AstTest {

    @Test
    public void motorOn() throws Exception {
        String a =
            "#defineWHEELDIAMETER5.6#defineTRACKWIDTH11.0#defineMAXLINES8#include\"NEPODefs.h\"//containsNEPOdeclarationsfortheNXCNXTAPIresources"
                + "OnFwdRegEx(OUT_B,SpeedTest(30),OUT_REGMODE_SPEED,RESET_NONE);OnFwdRegEx(OUT_C, SpeedTest(50), OUT_REGMODE_SPEED,RESET_NONE);}";

        this.h.assertWrappedCodeIsOk(a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "RotateMotor(OUT_B,SpeedTest(30), 360 * 1);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorOnFor.xml");
    }
}