package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class FourDigitDisplayShowActionTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfFourDigitDisplayShowActionClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=409, y=23], MainTask [], FourDigitDisplayShowAction [NumConst [1234], NumConst [0], BoolConst [true]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/fourdigitdisplay_show.xml");

        
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/fourdigitdisplay_show.xml");
    }
}
