package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightActionStatusTest extends AstTest {

    @Test
    public void ledOff() throws Exception {
        String a = "\nhal.ledOff();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_BrickLightStatus.xml");
    }

    @Test
    public void resetLED() throws Exception {
        String a = "\nhal.resetLED();}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_BrickLightStatus1.xml");
    }
}
