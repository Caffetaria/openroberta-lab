package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WaitStmtTest extends AstTest {

    @Test
    public void test1() throws Exception {
        String a = "publicvoidrun()throwsException{while(true){if(hal.isPressed(BrickKey.ENTER)==true){break;}hal.waitFor(15);}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/control/wait_stmt2.xml");
    }

    @Test
    public void javaCode() throws Exception {
        String a = "\nhal.waitFor(500);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/control/wait_time_stmt.xml");
    }
}