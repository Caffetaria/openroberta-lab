package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ToneActionTest extends AstTest {

    @Test
    public void playTone() throws Exception {
        String a = "\nhal.playTone(300, 100);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_PlaySound.xml");
    }
}
