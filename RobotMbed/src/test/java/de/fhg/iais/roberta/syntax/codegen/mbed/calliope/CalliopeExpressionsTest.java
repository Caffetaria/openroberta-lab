package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CalliopeExpressionsTest extends AstTest {

    @Test
    public void calliopeBinaryTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/expr/calliope_binary_test.cpp", "/expr/calliope_binary_test.xml");
    }

}
