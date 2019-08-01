package de.fhg.iais.roberta.util.test;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class GenericHelperForXmlTest extends AbstractHelperForXmlTest {
    public GenericHelperForXmlTest() {
        super(new TestFactory(new PluginProperties("test", "", "", Util1.loadProperties("classpath:/pluginProperties/test.properties"))), (ConfigurationAst) null);
    }

    private static class TestFactory extends AbstractRobotFactory {

        public TestFactory(PluginProperties pluginProperties) {
            super(pluginProperties);
        }

        @Override
        public ICompilerWorkflow getRobotCompilerWorkflow() {
            return null;
        }

        @Override
        public ICompilerWorkflow getSimCompilerWorkflow() {
            return null;
        }

        @Override
        public String getFileExtension() {
            return "py";
        } // TODO

        @Override
        public AbstractSimValidatorVisitor getSimProgramCheckVisitor(ConfigurationAst brickConfiguration) {
            return null;
        }

        @Override
        public String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
            return null;
        }

        @Override
        public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration) {
            return null;
        }

    }
}
