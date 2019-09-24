package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsLengthTest extends AstTest {

    @Test
    public void Test() throws Exception {
        final String a = "ArrayLen({0.1,0.0,0})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_length.xml");
    }
}
