package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltrasonicSensorTest extends AstTest {

    @Test
    public void setUltrasonic() throws Exception {
        String a = "\nhal.getUltraSonicSensorDistance(SensorPort.S4)" + "hal.getUltraSonicSensorPresence(SensorPort.S2)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/sensors/sensor_setUltrasonic.xml");
    }
}
