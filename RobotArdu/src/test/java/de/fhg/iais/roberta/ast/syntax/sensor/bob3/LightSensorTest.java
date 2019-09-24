package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorTest extends AstTest {

    @Test
    public void getAmbientLight() throws Exception {
        final String a = "double ___item; void setup() { ___item=0;} void loop() {___item=rob.getIRLight();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_bob3AmbientLight.xml");
    }
}