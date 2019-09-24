package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class UltrasonicSensorTest extends AstTest {

    @Test
    public void setUltrasonic() throws Exception {
        final String a = "\nbnr.ultrasonicDistance(4)bnr.ultrasonicDistance(2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_setUltrasonic.xml");
    }
}
