package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HandRightCloseTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfHandClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=138, y=163], " + "MainTask [], " + "Hand [RIGHT, REST]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/handRightClose.xml");

    }
    /*
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
    
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/handLeftOpen.xml");
    }*/
}