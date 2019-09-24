package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore // TODO: reactivate this test REFACTORING
public class MotorStopActionTest extends AstTest {

    @Test
    public void stopMotor() throws Exception {
        final String a = "one.stop1m(2)\n";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorStop.xml");
    }
}