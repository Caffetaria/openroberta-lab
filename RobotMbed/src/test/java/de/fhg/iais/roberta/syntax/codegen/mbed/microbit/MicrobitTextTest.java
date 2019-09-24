package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitTextTest extends AstTest {

    @Test
    public void mathOnListsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/function/microbit_text_join_test.py", "/function/microbit_text_join_test.xml");
    }

}
