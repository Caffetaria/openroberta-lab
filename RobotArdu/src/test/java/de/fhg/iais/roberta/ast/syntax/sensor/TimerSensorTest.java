package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TimerSensorTest extends AstTest {

    @Test
    public void getTimerValue() throws Exception {
        String a = "\n(int)(millis()-__time)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_getSampleTimer.xml");
    }

    @Test
    public void resetTimer() throws Exception {
        String a = "\n__time = millis();";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_resetTimer.xml");
    }
}
