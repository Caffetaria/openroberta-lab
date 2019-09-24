package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayFileActionTest extends AstTest {

    @Test
    public void playFile() throws Exception {
        String a = "\nhal.playFile(1);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_PlayFile.xml");
    }
}
