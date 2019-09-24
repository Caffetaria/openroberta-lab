package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FlowControlStmtTest extends AstTest {

    //
    public void flowControlStmt() throws Exception {
        String a =
            "\nwhile ( 0 == 0 ) \n"

                + "    \nwhile ( !(0 == 0) ) \n"

                + "       break;\n"
                + "    }\n"
                + "    break;\n"
                + "}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/stmt/flowControl_stmt.xml");
    }
}