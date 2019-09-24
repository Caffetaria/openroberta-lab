package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;

public class Ev3DevControlTest {

    private final ConfigurationAst configuration = HelperEv3ForXmlTest.makeStandardEv3DevConfiguration();

    @Test
    public void ev3DevWaitTest() throws Exception {
        this.ev3DevHelper.compareExistingAndGeneratedPythonSource("/ast/control/ev3dev_wait_test.py", "/ast/control/ev3dev_wait_test.xml", this.configuration);
    }
}
