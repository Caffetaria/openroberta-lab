package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3ActorsTest extends AstTest {

    @Test
    public void listsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/ast/actions/bob3_actors_test.ino", "/ast/actions/bob3_actors_test.xml");
    }

}
