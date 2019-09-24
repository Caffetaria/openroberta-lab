package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ExprTest extends AstTest {

    @Test
    public void test1() throws Exception {
        final String a = "\n(8+((-3+5)))(88-((8+((-3+5)))))(((88-((8+((-3+5))))))-((88-((8+((-3+5)))))))(2*((2-2)))(2-((2*2)))";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/expr1.xml");
    }

    @Test
    public void test2() throws Exception {
        final String a = "\n(2*((2-2)))(2-((2*2)))(((88-((8+((-3+5))))))-((2*2)))(((((88-((8+((-3+5))))))-((2*2))))/((((((88-((8+((-3+5))))))-((2*2)))))*1.0))";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/expr2.xml");
    }
}