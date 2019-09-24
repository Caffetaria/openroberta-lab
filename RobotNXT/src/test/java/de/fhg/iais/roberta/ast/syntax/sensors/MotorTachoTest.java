package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorTachoTest extends AstTest {

    @Test
    public void setMotorTacho() throws Exception {
        final String a = "\nMotorTachoCount(OUT_A)/360.0" + "MotorTachoCount(OUT_C)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_setEncoder.xml");
    }

    @Test
    public void resetMotorTacho() throws Exception {
        final String a = "\nResetTachoCount(OUT_A);";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_resetEncoder.xml");
    }
}
