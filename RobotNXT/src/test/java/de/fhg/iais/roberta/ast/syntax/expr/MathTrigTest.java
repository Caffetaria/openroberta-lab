package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "MathSin(0)MathCos(0)MathTan(0)MathAsin(0)MathAcos(0)MathAtan(0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==MathSin(0)){OnFwdRegEx(OUT_BC,SpeedTest(MathAcos(0)),OUT_REGMODE_SYNC,RESET_NONE);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml");
    }

}
