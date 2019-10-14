package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.codegen.Ev3JavaGeneratorWorker;

public class TextJoinTextTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "String.valueOf(0)+String.valueOf(0)+String.valueOf(\"a\")+String.valueOf" +
                   "(\"b\") + String.valueOf(true)+String.valueOf(hal.isPressed(SensorPort.S1))}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/text/text_join.xml",
                                                                                   new Ev3JavaGeneratorWorker());
    }

}
