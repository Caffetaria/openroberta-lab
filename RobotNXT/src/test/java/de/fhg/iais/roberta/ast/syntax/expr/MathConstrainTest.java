package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathConstrainTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "Constrain(SensorUS(S4),1,100)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_constrain.xml");
    }
}
