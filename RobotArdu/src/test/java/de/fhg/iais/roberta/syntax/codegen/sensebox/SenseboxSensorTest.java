package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.util.Util1;

public class SenseboxSensorTest {

    @Test
    public void builtinAndSimpleActorsTest() throws Exception {
        ConfigurationAst config =
            this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/sensors/sensebox_simple_and_builtin_sensor_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource(
                "/ast/sensors/sensebox_simple_and_builtin_sensor_test.ino",
                "/ast/sensors/sensebox_simple_and_builtin_sensor_test.xml",
                config);
    }
}
