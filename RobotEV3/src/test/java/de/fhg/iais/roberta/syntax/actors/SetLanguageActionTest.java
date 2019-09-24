package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SetLanguageActionTest extends AstTest {

    @Test
    public void setLanguage() throws Exception {
        String a = "\nhal.setLanguage(\"de\");}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/actions/action_SetLanguage.xml");
    }
}
