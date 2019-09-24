package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ShowPictureActionTest extends AstTest {

    @Test
    public void drawPicture() throws Exception {
        String a = "\nhal.drawPicture(predefinedImages.get(\"EYESOPEN\"), 0, 0);}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_ShowPicture.xml");
    }
}
