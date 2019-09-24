package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnActionTest extends AstTest {

    @Test
    public void turn() throws Exception {
        final String a = "OnFwdSync(OUT_BC,SpeedTest(50), -100);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "RotateMotorEx(OUT_BC,SpeedTest(50), ( 20 * TRACKWIDTH / WHEELDIAMETER), -100, true, true );\nWait( 1 );";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }
}