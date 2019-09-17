package de.fhg.iais.roberta.ast;

import static org.junit.Assert.fail;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util1;

public class RoundTripTransformationTest {

    private static final String ORA_CC_RSC_ENVVAR = ServerProperties.CROSSCOMPILER_RESOURCE_BASE.replace('.', '_');
    private static final Logger LOG = LoggerFactory.getLogger(RoundTripTransformationTest.class);

    @Test
    public void test() throws Exception {
        Project.Builder projectBuilder = new Project.Builder();
        String projectXml = Util1.readResourceContent("/ast/roundTrip.xml");
        String[] parts = projectXml.split("\\s*</program>\\s*<config>\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[0];
        if ( System.getenv(ORA_CC_RSC_ENVVAR) == null ) {
            LOG.error("the environment variable \"" + ORA_CC_RSC_ENVVAR + "\" must contain the absolute path to the ora-cc-rsc repository - test fails");
            fail();
        }
        PluginProperties properties = new PluginProperties("uno", System.getenv(ORA_CC_RSC_ENVVAR), "/tmp/", Util1.loadProperties("classpath:/uno.properties"));
        IRobotFactory unoFactory = new UnoFactory(properties);
        projectBuilder.setProgramXml(program);
        projectBuilder.setConfigurationXml(configuration);
        projectBuilder.setFactory(unoFactory);
        Project project = projectBuilder.build();
        String transformedProgram = project.getAnnotatedProgramAsXml();
        String transformedConfiguration = project.getAnnotatedConfigurationAsXml();
        String transformedProject =
            "<export xmlns=\"http://de.fhg.iais.roberta.blockly\">\n<program>\n"
                + transformedProgram
                + "</program>\n<config>"
                + transformedConfiguration
                + "</config>\n</export>";
        System.out.println(projectXml);
        System.out.println(transformedProject);
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(transformedProject, projectXml);
        System.out.println(diff);
        Assert.assertTrue(diff.identical());
    }
}
