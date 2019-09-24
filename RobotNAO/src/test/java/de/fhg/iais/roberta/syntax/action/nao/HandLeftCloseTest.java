package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HandLeftCloseTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "Hand [LEFT, REST]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/handLeftClose.xml");

    }
    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/handLeftOpen.xml");
    }*/
}