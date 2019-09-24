package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextAppendTest extends AstTest {

    @Ignore
    public void Test() throws Exception {
        final String a = "item+=String(S1);item+=NumToStr(0);item+=\"aaa\";";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/text/text_append.xml");
    }
}
