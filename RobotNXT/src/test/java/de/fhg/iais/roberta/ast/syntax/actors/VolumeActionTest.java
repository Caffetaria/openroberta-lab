package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VolumeActionTest extends AstTest {

    @Test
    public void setVolume() throws Exception {
        String a = "\nvolume=(50)*4/100.0;";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_SetVolume.xml");
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nvolume*100/4";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_GetVolume.xml");
    }
}
