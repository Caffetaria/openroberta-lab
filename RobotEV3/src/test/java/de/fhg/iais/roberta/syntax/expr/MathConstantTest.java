package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstantTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "BlocklyMethods.PIBlocklyMethods.EBlocklyMethods.GOLDEN_RATIOBlocklyMethods.sqrt(2)BlocklyMethods.sqrt((float)1.0/(float)2.0)Float.POSITIVE_INFINITY}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_constant.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "hal.rotateRegulatedMotor(ActorPort.B,BlocklyMethods.PI,MotorMoveMode.ROTATIONS,BlocklyMethods.GOLDEN_RATIO);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_constant1.xml");
    }

}
