package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore // TODO: reactivate this test REFACTORING
public class MotorOnActionTest extends AstTest {

    @Ignore // not implemented yet
    @Test
    public void motorOn() throws Exception {
        String a = "one.move1mPID(B,30);one.move1mPID(C,50);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "one.servo1(30);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorOnFor.xml");
    }
}