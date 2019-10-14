package de.fhg.iais.roberta.util.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.xml.sax.SAXException;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageGeneratorWorker;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class UnitTestHelper {

    private static boolean executeWorkflow(String workflowName, IRobotFactory robotFactory,
                                     Project project) {
        List<IWorker> workflowPipe = robotFactory.getWorkerPipe(workflowName);
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                Assert.assertTrue("Worker " + worker.getClass().getSimpleName() + " failed",
                                  project.hasSucceeded());
                if ( !project.hasSucceeded() ) {
                    break;
                }
            }
        }
        return project.hasSucceeded();
    }

    public static void checkWorkers(IRobotFactory factory,
                                    String expectedSource,
                                    String programXmlFilename,
                                    IWorker... workers) {
        String programXml = Util1.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setWithWrapping(false);
        Project project = builder.build();
        for (IWorker worker : workers) {
            worker.execute(project);
        }

        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
    }

    public static Project.Builder setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        return setupWithConfigurationAndProgramXML(factory, program, configuration);
    }

    public static Project.Builder setupWithConfigurationAndProgramXML(IRobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString).setFactory(factory);
    }

    public static Project.Builder setupWithProgramXML(IRobotFactory factory, String programXmlAsString) {
        return new Project.Builder().setProgramXml(programXmlAsString).setFactory(factory).setProgramName("Test");
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

    // TODO merge this with the method above
    public static <V> Phrase<V> generateAst(IRobotFactory factory, String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        Jaxb2ProgramAst<V> transformer = new Jaxb2ProgramAst<>(factory);
        transformer.transform(project);
        ArrayList<ArrayList<Phrase<V>>> tree = transformer.getTree();
        return tree.get(0).get(1);
    }

    public static void checkGeneratedSourceEqualityWithExportXml(IRobotFactory factory, String expectedSourceFilename, String exportedXmlFilename) throws Exception {
        String exportedXml = Util1.readResourceContent(exportedXmlFilename);
        Project.Builder builder = setupWithExportXML(factory, exportedXml);
        checkGeneratedSourceEquality(factory,
                                     Util1.readResourceContent(expectedSourceFilename),
                                     builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(IRobotFactory factory, String expectedSourceFilename, String programXmlFilename)
            throws Exception {
        String programXml = Util1.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        checkGeneratedSourceEquality(factory,
                                     Util1.readResourceContent(expectedSourceFilename),
                                     builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(
        IRobotFactory factory,
        String expectedSourceFilename,
        String programXmlFilename,
        ConfigurationAst configurationAst)
        throws Exception {
        String programXml = Util1.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        checkGeneratedSourceEquality(factory,
                                     Util1.readResourceContent(expectedSourceFilename),
                                     builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXml(IRobotFactory factory,
                                                                  String expectedSourceFilename,
                                                                  String programXmlFilename,
                                                                  String configurationXmlFilename)
            throws Exception {
        String programXml = Util1.readResourceContent(programXmlFilename);
        String configurationXml = Util1.readResourceContent(configurationXmlFilename);
        Project.Builder builder = setupWithConfigurationAndProgramXML(factory, programXml, configurationXml);
        builder.setSSID("mySSID");
        builder.setPassword("myPassw0rd");
        checkGeneratedSourceEquality(factory,
                                     Util1.readResourceContent(expectedSourceFilename),
                                     builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(IRobotFactory factory,
                                                                                   String expectedSource,
                                                                                   String programXmlFilename,
                                                                                   boolean withWrapping) throws Exception {
        String programXml = Util1.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    public static void checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(IRobotFactory factory,
                                                                                   String expectedSource,
                                                                                   String programXmlFilename,
                                                                                   ConfigurationAst configurationAst,
                                                                                   boolean withWrapping) throws Exception {
        String programXml = Util1.readResourceContent(programXmlFilename);
        Project.Builder builder = setupWithProgramXML(factory, programXml);
        builder.setConfigurationAst(configurationAst);
        builder.setWithWrapping(withWrapping);
        checkGeneratedSourceEquality(factory, expectedSource, builder.build());
    }

    private static void checkGeneratedSourceEquality(IRobotFactory factory,
                                                     String expectedSource,
                                                     Project project) {
        executeWorkflow("showsource", factory, project);

        String generatedProgramSource = project.getSourceCode().toString().replaceAll("\\s+", "");
        Assert.assertEquals(expectedSource.replaceAll("\\s+", ""), generatedProgramSource);
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
}
