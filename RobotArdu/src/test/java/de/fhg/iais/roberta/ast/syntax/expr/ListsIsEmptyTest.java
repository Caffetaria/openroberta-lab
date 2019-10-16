package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCxxGeneratorWorker;

public class ListsIsEmptyTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "NULL";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_is_empty.xml", new ArduinoCxxGeneratorWorker());
    }
}
