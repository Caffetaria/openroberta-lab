package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SayTextActionTest extends AstTest {

    @Test
    public void sayText() throws Exception {
        String a = "\nhal.sayText(\"Hello world\");}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_SayText.xml");
    }
}
