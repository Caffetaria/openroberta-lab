package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class IRSeekerSensorTest extends AstTest {

    @Test
    public void getIRSeeker() throws Exception {
        String a =
            "hal.drawText(String.valueOf(hal.getHiTecIRSeekerModulated(SensorPort.S1)), 0, 0);"
                + "hal.drawText(String.valueOf(hal.getHiTecIRSeekerUnmodulated(SensorPort.S1)), 0, 0);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/sensors/sensor_getIRSeeker.xml");
    }
}
