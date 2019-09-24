package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsEmptyListTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<String>()" + "newArrayList<Pickcolor>()" + "newArrayList<Boolean>()" + "newArrayList<Float>()}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_empty_list.xml");
    }

}
