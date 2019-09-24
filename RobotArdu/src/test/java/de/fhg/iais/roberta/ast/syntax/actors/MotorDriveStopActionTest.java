package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorDriveStopActionTest extends AstTest {

    @Test
    public void stop() throws Exception {
        final String a = "\none.stop();";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_Stop.xml");
    }
}