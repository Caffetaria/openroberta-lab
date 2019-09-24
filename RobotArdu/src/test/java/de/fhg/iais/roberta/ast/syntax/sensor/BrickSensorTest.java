package de.fhg.iais.roberta.ast.syntax.sensor;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore // TODO: reactivate this test REFACTORING
public class BrickSensorTest extends AstTest {

    @Test
    public void isPressed() throws Exception {
        String a = "\nbnr.buttonIsPressed(2)";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_brick1.xml");
    }
}
