package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorStopActionTest extends AstTest {

    @Test
    public void stopMotor() throws Exception {
        final String a = "\nFloat(OUT_A);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_MotorStop.xml");
    }
}