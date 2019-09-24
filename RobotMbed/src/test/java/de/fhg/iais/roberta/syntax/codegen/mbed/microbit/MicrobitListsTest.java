package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitListsTest extends AstTest {

    @Test
    public void mathOnListsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/lists/microbit_math_on_lists_test.py", "/lists/microbit_math_on_lists_test.xml");
    }

    @Test
    public void fullListsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/lists/microbit_lists_full_test.py", "/lists/microbit_lists_full_test.xml");
    }

}
