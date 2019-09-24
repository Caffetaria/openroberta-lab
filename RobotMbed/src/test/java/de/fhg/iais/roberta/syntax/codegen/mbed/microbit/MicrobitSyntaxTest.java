package de.fhg.iais.roberta.syntax.codegen.mbed.microbit;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MicrobitSyntaxTest extends AstTest {

    @Test
    public void emptyValuesTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/stmts/microbit_emtpy_values_test.py", "/stmts/microbit_emtpy_values_test.xml");
    }

    @Test
    public void waitTimeConditionTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/stmts/microbit_wait_test.py", "/stmts/microbit_wait_test.xml");
    }
}
