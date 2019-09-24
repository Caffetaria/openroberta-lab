package de.fhg.iais.roberta.ast.syntax.sensors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrickSensorTest extends AstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nButtonPressed(BTNCENTER,false)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_brick1.xml");
    }
}
