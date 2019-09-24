package de.fhg.iais.roberta.ast.syntax.sensor.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3ArmsTest extends AstTest {

    @Test
    public void getLeftArmRightArmLight() throws Exception {
        final String a = "void setup(){}void loop(){if((rob.getArm(1)>0)){}elseif((rob.getArm(2)==1)){}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/sensors/sensor_bob3Arms.xml");
    }
}
