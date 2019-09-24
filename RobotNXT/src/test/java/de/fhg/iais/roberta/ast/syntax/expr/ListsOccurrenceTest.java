package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsOccurrenceTest extends AstTest {

    //ignore
    public void Test() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence.xml");
    }

    //ignore
    public void Test1() throws Exception {
        final String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence1.xml");
    }
}
