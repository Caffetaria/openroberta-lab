package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SoundSensorTest extends AstTest {

    @Ignore
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_setLight.xml");
    }
}
