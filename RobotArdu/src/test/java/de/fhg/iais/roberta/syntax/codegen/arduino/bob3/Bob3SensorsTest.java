package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3SensorsTest extends AstTest {

    @Test
    public void listsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/ast/sensors/bob3_sensors_test.ino", "/ast/sensors/bob3_sensors_test.xml");
    }

    @Test
    public void waitBlockTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/ast/sensors/bob3_wait_test.ino", "/ast/sensors/bob3_wait_test.xml");
    }

}
