package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsIsEmptyTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "ArrIsEmpty({0,0,0})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_is_empty.xml");
    }
}
