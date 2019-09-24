package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3CodePadTest extends AstTest {

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "double ___item; void setup(){ ___item = 0; } void loop() { ___item = rob.getID();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_bob3CodePad.xml");
    }
}
