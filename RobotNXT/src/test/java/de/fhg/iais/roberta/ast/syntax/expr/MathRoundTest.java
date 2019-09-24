package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRoundTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "MathRound(0)MathRoundUp(0)MathFloor(0)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_round.xml");
    }
}
