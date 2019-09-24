package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3MathTest extends AstTest {

    @Test
    public void clampTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/ast/math/bob3_clamp_test.ino", "/ast/math/bob3_clamp_test.xml");
    }

}
