package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorActionTest extends AstTest {

    @Ignore
    public void redOn() throws Exception {
        String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_LightSensorAction.xml");
    }
}