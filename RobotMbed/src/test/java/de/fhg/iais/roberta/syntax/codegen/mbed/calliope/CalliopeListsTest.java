package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CalliopeListsTest extends AstTest {

    @Test
    public void calliopeGetSetTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/lists/calliope_lists_get_set_test.cpp", "/lists/calliope_lists_get_set_test.xml");
    }

    @Test
    public void calliopeFindFirstLastTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEquality(testFactory, "/lists/calliope_lists_find_last_first_test.cpp", "/lists/calliope_lists_find_last_first_test.xml");
    }
}
