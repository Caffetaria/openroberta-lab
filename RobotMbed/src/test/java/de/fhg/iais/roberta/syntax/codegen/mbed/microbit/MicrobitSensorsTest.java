package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitSensorsTest extends AstTest {

    @Test
    public void waitTimeConditionTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/sensor/microbit_timer_test.py", "/sensor/microbit_timer_test.xml");
    }

}
