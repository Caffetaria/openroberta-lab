package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LedBarSetTest extends AstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLedBarSetActionClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=382, y=50], MainTask [], LedBarSetAction [ NumConst [0], NumConst [5] ]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/action/ledbar_set.xml");

        
    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/action/ledbar_set.xml");
    }
}
