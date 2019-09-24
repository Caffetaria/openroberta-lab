package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "floatElement=BlocklyMethods.round(0);floatElement2=BlocklyMethods.floor(0);floatElement3=BlocklyMethods.ceil(0);publicvoidrun()throwsException{}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_round.xml");
    }
}
