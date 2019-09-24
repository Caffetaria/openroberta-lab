package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;

public class BotnrollSensorTest {

    ConfigurationAst configuration = HelperBotNrollForXmlTest.makeConfiguration();

    @Test
    public void botnrollSensorTest() throws Exception {
        this.botnrollHelper
            .compareExistingAndGeneratedSource("/ast/sensors/botnroll_sensors_test.ino", "/ast/sensors/botnroll_sensors_test.xml", this.configuration);
    }
}
