package de.fhg.iais.roberta.syntax.codegen.arduino.bob3;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Bob3StructureTest extends AstTest {

    @Test
    public void listsTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(testFactory, "/ast/variables/bob3_datatypes_test.ino", "/ast/variables/bob3_datatypes_test.xml");
    }

}
