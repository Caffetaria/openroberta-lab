package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class DriveActionTest extends AstTest {

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(DriveDirection.FOREWARD, 50);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(DriveDirection.FOREWARD, 50, 20);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_MotorDiffOnFor.xml");
    }
}