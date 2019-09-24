package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltrasonicSensorTest extends AstTest {

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nSensorUS(S4)SensorUS(S2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
