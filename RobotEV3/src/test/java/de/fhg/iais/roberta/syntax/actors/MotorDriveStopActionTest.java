package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorDriveStopActionTest extends AstTest {

    @Test
    public void stop() throws Exception {
        String a = "\nhal.stopRegulatedDrive();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_Stop.xml");
    }
}