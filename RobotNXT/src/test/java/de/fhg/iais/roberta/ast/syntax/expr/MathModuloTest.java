package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathModuloTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a = "float___variablenName;taskmain(){___variablenName=1%0;";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_modulo.xml");
    }

}
