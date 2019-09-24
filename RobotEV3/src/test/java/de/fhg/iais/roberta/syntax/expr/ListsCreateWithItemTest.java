package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsCreateWithItemTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a = "newArrayList<>(Collections.nCopies(5, (float) 1))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_create_with_item.xml");
    }

}
