package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TouchSensorTest extends AstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nhal.isPressed(SensorPort.S1)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/sensors/sensor_Touch.xml");
    }
}
