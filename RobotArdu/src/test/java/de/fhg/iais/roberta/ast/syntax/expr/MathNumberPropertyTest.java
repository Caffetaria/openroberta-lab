package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathNumberPropertyTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "(fmod(0,2)==0)(fmod(0,2)!=0)isPrimeD(0)isWholeD(0)(0>0)(0<0)(fmod(0,0)==0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_number_property.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "___item=(fmod(0,2)==0);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_number_property1.xml");
    }

}
