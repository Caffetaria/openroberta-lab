package de.fhg.iais.roberta.util.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.xml.sax.SAXException;

import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class UnitTestHelper {

    public static boolean executeWorkflow(String workflowName, IRobotFactory robotFactory, Project project) {
        List<IWorker> workflowPipe = robotFactory.getWorkerPipe(workflowName);
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                if ( !project.hasSucceeded() ) {
                    break;
                }
            }
        }
        return project.hasSucceeded();
    }

    public static Project.Builder setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return UnitTestHelper.setupWithConfigurationAndProgramXML(factory, program, configuration);
    }

    public static Project.Builder setupWithConfigurationAndProgramXML(IRobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static Project.Builder setupWithProgramXML(IRobotFactory factory, String programXmlAsString) {
        return new Project.Builder().setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static class TestFactory extends AbstractRobotFactory {

        public TestFactory() {
            super(new PluginProperties("test", "", "", Util1.loadProperties("classpath:/pluginProperties/test.properties")));
        }

        @Override
        public String getFileExtension() {
            return "test";
        }
    }

    public static void checkProgramReverseTransformation(IRobotFactory factory, String programBlocklyXmlFilename) throws SAXException, IOException {
        String programXml = Util1.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        Project project = builder.build();
        String annotatedProgramXml = project.getAnnotatedProgramAsXml();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(programXml, annotatedProgramXml);
        Assert.assertTrue(diff.identical());
    }

    public static void checkProgramAstEquality(IRobotFactory factory, String expectedAst, String programBlocklyXmlFilename) throws Exception {
        String generatedAst = getAst(factory, programBlocklyXmlFilename).toString();
        generatedAst = "BlockAST [project=" + generatedAst + "]";
        Assert.assertEquals(expectedAst.replaceAll("\\s+", ""), generatedAst.replaceAll("\\s+", ""));
    }

    public static Phrase<Void> getAstOfFirstBlock(IRobotFactory factory, String programBlocklyXmlFilename) {
        return getAst(factory, programBlocklyXmlFilename).get(0).get(1);
    }

    public static ArrayList<ArrayList<Phrase<Void>>> getAst(IRobotFactory factory, String programBlocklyXmlFilename) {
        String programXml = Util1.readResourceContent(programBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        Project project = builder.build();
        return project.getProgramAst().getTree();
    }

    public static void checkGeneratedSourceEquality(IRobotFactory factory, String expectedSourceFilename, String exportedBlocklyXmlFilename) throws Exception {
        String programSource = Util1.readResourceContent(expectedSourceFilename).replaceAll("\\s+", "");
        String exportedXml = Util1.readResourceContent(exportedBlocklyXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, exportedXml);
        Project project = builder.build();
        executeWorkflow("showsource", factory, project);

        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(programSource, generatedProgramSource);
    }

    public static void checkGeneratedSourceEqualityWithSourceAsString(IRobotFactory factory, String expectedSource, String exportedBlocklyXmlFilename)
        throws Exception {
        String exportedXml = Util1.readResourceContent(exportedBlocklyXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, exportedXml);
        Project project = builder.build();
        executeWorkflow("showsource", factory, project);

        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }
}
