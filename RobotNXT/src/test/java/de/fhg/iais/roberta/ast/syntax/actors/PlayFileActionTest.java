package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PlayFileActionTest extends AstTest {

    @Test
    public void playFile() throws Exception {
        String a = "\nPlayFile(1);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_PlayFile.xml");
    }
}
