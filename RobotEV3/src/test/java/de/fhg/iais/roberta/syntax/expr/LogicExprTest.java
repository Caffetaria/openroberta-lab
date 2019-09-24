package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LogicExprTest extends AstTest {

    @Test
    public void test1() throws Exception {
        String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "(5 <= 7) == (8 > 9)\n"
                + "( 5 != 7 ) >= ( 8 == 9 )\n"
                + "(5 + 7) >= (( 8 + 4 ) / ((float) ( 9 + 3 )))\n"
                + "( (5 + 7) == (5 + 7) ) >= (( 8 + 4 ) / ((float) ( 9 + 3 )))\n"
                + "( (5 + 7) == (5 + 7) ) >= ( ((5 + 7) == (5 + 7)) && ((5 + 7) <= (5 + 7) ))\n"
                + "!((5 + 7) == (5 + 7)) == true}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/logic_expr.xml");
    }

    @Test
    public void logicNegate() throws Exception {
        String a = "\n!((0 != 0) && false)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/logic_negate.xml");
    }

    @Test
    public void logicNull() throws Exception {
        String a = "\nnull}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/logic_null.xml");
    }

    @Test
    public void logicTernary() throws Exception {
        String a = "\n( 0 == 0 ) ? false : true}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/expr/logic_ternary.xml");
    }
}