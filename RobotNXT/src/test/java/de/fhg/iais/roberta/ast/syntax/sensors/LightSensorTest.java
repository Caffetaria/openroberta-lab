package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

public class LightSensorTest {

    @Test
    public void setColor() throws Exception {
        final String a = "\nSensorColor(IN_3,\"RED\")SensorColor(IN_4,\"AMBIENTLIGHT\")";

        //Helper.assertCodeIsOk(a, "/ast/sensors/sensor_setLight.xml");
    }
}
