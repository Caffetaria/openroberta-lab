package de.fhg.iais.roberta.syntax.codegen.WeDo;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.WeDoFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class WeDoTest {

    private static IRobotFactory weDoFactory;

    @BeforeClass
    public static void setup() {
        weDoFactory = new WeDoFactory(new PluginProperties("wedo", "", "", Util1.loadProperties("classpath:/wedo.properties")));
    }

    @Test
    public void weDoEverythingTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(weDoFactory, "/everything_test.json", "/everything_test.xml");
    }

    @Test
    public void weDoAllBlocksTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(weDoFactory, "/all_blocks_test.json", "/all_blocks_test.xml");
    }

    @Test
    public void motorTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEquality(weDoFactory, "/motor_test.json", "/motor_test.xml");
    }
}
