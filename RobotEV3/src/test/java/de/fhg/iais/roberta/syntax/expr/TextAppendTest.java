package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextAppendTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a = "item+=String.valueOf(hal.isPressed(SensorPort.S1));item+=String.valueOf(0);item+=String.valueOf(\"aaa\");}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/text/text_append.xml");
    }
}
