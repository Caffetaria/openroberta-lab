package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SoundSensorTest extends AstTest {

    @Test
    public void getSampleSound() throws Exception {
        String a = "\nhal.getSoundLevel(SensorPort.S1)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/sensors/sensor_getSampleSound.xml");
    }
}
