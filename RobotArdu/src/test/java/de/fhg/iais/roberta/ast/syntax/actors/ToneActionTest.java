package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionTest extends AstTest {

    @Test
    public void playTone() throws Exception {
        final String a = "tone(9,300, 100);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_PlaySound.xml");
    }
}
