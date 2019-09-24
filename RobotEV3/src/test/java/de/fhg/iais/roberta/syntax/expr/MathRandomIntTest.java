package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathRandomIntTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.randInt(1,100)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_random_int.xml");
    }
}
