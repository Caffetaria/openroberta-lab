package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowTextActionTest extends AstTest {

    @Test
    public void clearDisplay() throws Exception {
        final String a = "\nTextOut(0,(MAXLINES - 0) * MAXLINES,\"Hallo\");";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_ShowText.xml");
    }

}
