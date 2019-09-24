package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "sin(PI/180.0*(0))cos(PI/180.0*(0))tan(PI/180.0*(0))180.0/PI*asin(0)180.0/PI*acos(0)180.0/PI*atan(0)";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==sin(PI/180.0*(0))){one.movePID(180.0/PI*acos(0),180.0/PI*acos(0));}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml");
    }

}
