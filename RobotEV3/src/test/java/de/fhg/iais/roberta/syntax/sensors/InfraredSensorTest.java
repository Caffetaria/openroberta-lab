package de.fhg.iais.roberta.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.codegen.Ev3JavaGeneratorWorker;

public class InfraredSensorTest extends Ev3LejosAstTest {

    @Test
    public void setInfrared() throws Exception {
        String a = "\nhal.getInfraredSensorDistance(SensorPort.S4)" + "hal.getInfraredSensorSeek(SensorPort.S3)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/sensors/sensor_setInfrared.xml",
                                                                                   new Ev3JavaGeneratorWorker());
    }
}
