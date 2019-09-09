package de.fhg.iais.roberta.util.test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class GenericHelperForXmlTest extends AbstractHelperForXmlTest {
    public GenericHelperForXmlTest() {
        super(
            new TestFactory(new PluginProperties("test", "", "", Util1.loadProperties("classpath:/pluginProperties/test.properties"))),
            (ConfigurationAst) null);
    }

    private static class TestFactory extends AbstractRobotFactory {

        public TestFactory(PluginProperties pluginProperties) {
            super(pluginProperties);
        }

        @Override
        public String getFileExtension() {
            return "py";
        }
    }
}
