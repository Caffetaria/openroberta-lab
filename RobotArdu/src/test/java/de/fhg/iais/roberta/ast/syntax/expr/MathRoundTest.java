package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "round(0)ceil(0)floor(0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_round.xml");
    }
}
