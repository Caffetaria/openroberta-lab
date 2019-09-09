package de.fhg.iais.roberta.transformer;

import static de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst.block2OldConfiguration;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

/**
 * This class stores the AST representation of the program and the configuration
 */
public class Project {

    private static final Logger LOG = LoggerFactory.getLogger(Project.class);

    private IRobotFactory robotFactory;
    private final List<Key> errorMessages = new ArrayList<>();
    private ProgramAst<Void> program = null;
    private ConfigurationAst configuration = null;
    Map<String, Object> workerResults = new HashMap<>();
    private StringBuilder sourceCodeBuilder = new StringBuilder();
    private final StringBuilder indentationBuilder = new StringBuilder();
    private String compiledHex = "";

    private String token;
    private String robot;
    private String programName;
    private String fileExtension;
    private String SSID;
    private String password;
    private ILanguage language;

    private Project() {

    }

    public String getCompiledHex() {
        return this.compiledHex;
    }

    public void setCompiledHex(String compiledHex) {
        this.compiledHex = compiledHex;
    }

    public String getToken() {
        return this.token;
    }

    public String getRobot() {
        return this.robot;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public String getSSID() {
        return this.SSID;
    }

    public String getPassword() {
        return this.password;
    }

    public ILanguage getLanguage() {
        return this.language;
    }

    public StringBuilder getSourceCode() {
        return this.sourceCodeBuilder;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCodeBuilder = new StringBuilder(sourceCode);
    }

    public StringBuilder getIndentation() {
        return this.indentationBuilder;
    }

    public IRobotFactory getRobotFactory() {
        return robotFactory;
    }

    public Object getWorkerResult(String beanName) {
        return this.workerResults.get(beanName);
    }

    public void addWorkerResult(String beanName, Object bean) {
        this.workerResults.put(beanName, bean);
    }

    /**
     * @return the list of keys with errorMessages
     */
    public List<Key> getErrorMessages() {
        return this.errorMessages;
    }

    public Key getErrorMessage() {
        if ( !this.errorMessages.isEmpty() && this.errorMessages != null ) {
            return this.errorMessages.get(0);
        } else {
            return Key.COMPILERWORKFLOW_SUCCESS;
        }
    }

    /**
     * @return the programTransformer
     */
    public ProgramAst<Void> getProgramAst() {
        return this.program;
    }

    /**
     * @return the robot configuration
     */
    public ConfigurationAst getConfigurationAst() {
        return this.configuration;
    }

    public String getAnnotatedProgramAsXml() {
        String programXML = "";
        try {
            programXML = jaxbToXml(astToJaxb(this.program));
        } catch ( JAXBException e ) {
            throw new DbcException("Transformation of program AST into blockset and into XML failed.");
        }
        return programXML;
    }

    public String getAnnotatedConfigurationAsXml() {
        String configurationXML = "";
        try {
            configurationXML = jaxbToXml(this.configuration.generateBlockSet());
        } catch ( JAXBException e ) {
            throw new DbcException("Transformation of configuration AST into blockset and into XML failed.");
        }
        return configurationXML;
    }

    public static Project.Builder setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\\\s*</program>\\\\s*<config>\\\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[1];
        return setupWithExportXML(factory, program, configuration);
    }

    public static Project.Builder setupWithExportXML(IRobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return new Project.Builder().setConfigurationXml(configurationXmlAsString).setProgramXml(programXmlAsString);
    }

    private static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    private static BlockSet astToJaxb(ProgramAst<Void> program) {
        ArrayList<ArrayList<Phrase<Void>>> astProgram = program.getTree();
        final BlockSet blockSet = new BlockSet();
        blockSet.setDescription(program.getDescription());
        blockSet.setRobottype(program.getRobotType());
        blockSet.setTags(program.getTags());
        blockSet.setXmlversion(program.getXmlVersion());

        for ( final ArrayList<Phrase<Void>> tree : astProgram ) {
            final Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            for ( final Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        return blockSet;
    }

    public static class Builder {
        private Project project = new Project();

        private String configurationXml;
        private String programXml;

        public Builder() {
        }

        public Builder setToken(String token) {
            project.token = token;
            return this;
        }

        public Builder setRobot(String robot) {
            project.robot = robot;
            return this;
        }

        public Builder setProgramName(String programName) {
            project.programName = programName;
            return this;
        }

        public Builder setProgramXml(String programXml) {
            this.programXml = programXml;
            return this;
        }

        public Builder setConfigurationXml(String configurationXml) {
            this.configurationXml = configurationXml;
            return this;
        }

        public Builder setFileExtension(String fileExtension) {
            project.fileExtension = fileExtension;
            return this;
        }

        public Builder setSSID(String sSID) {
            project.SSID = sSID;
            return this;
        }

        public Builder setPassword(String password) {
            project.password = password;
            return this;
        }

        public Builder setLanguage(ILanguage language) {
            project.language = language;
            return this;
        }

        public Builder setConfigurationAst(ConfigurationAst configurationAst) {
            project.configuration = configurationAst;
            return this;
        }

        public Builder setFactory(IRobotFactory factory) {
            project.robotFactory = factory;
            return this;
        }

        public Project build() {
            if ( project.configuration == null ) {
                transformConfiguration();
            }
            if ( project.program == null ) {
                transformProgram();
            }
            return project;
        }

        /**
         * Transforms program XML into AST.
         */
        private void transformProgram() {
            Key errorMessage = null;
            if ( this.programXml == null || this.programXml.trim().equals("") ) {
                errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND;
            } else {
                try {
                    Jaxb2ProgramAst<Void> programTransformer = JaxbHelper.generateProgramTransformer(this.project.robotFactory, this.programXml);
                    this.project.program = programTransformer.getData();
                } catch ( Exception e ) {
                    LOG.error("Transformer failed", e);
                    errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
                }
            }
            if ( errorMessage != null ) {
                this.project.errorMessages.add(errorMessage);
            }
        }

        /**
         * Transforms configuration XML into AST.
         */
        private void transformConfiguration() {
            Key errorMessage = null;
            if ( this.configurationXml == null || this.configurationXml.trim().equals("") ) {
                errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND;
            } else {
                try {
                    final BlockSet blockSet = JaxbHelper.xml2BlockSet(this.configurationXml);
                    if ( this.project.robotFactory.getConfigurationType().equals("new") ) {
                        this.project.configuration = transformConfiguration(blockSet);
                    } else {
                        this.project.configuration =
                            transformOldConfiguration(
                                blockSet,
                                this.project.robotFactory.getTopBlockOfOldConfiguration(),
                                this.project.robotFactory.getSensorPrefix());
                    }
                } catch ( Exception e ) {
                    LOG.error("Generation of the configuration failed", e);
                    errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED;
                }
            }
            if ( errorMessage != null ) {
                this.project.errorMessages.add(errorMessage);
                return;
            }
        }

        private ConfigurationAst transformConfiguration(BlockSet blockSet) {
            List<Instance> instances = blockSet.getInstance();
            List<List<Block>> blocks = new ArrayList<>();
            for ( int i = 0; i < instances.size(); i++ ) {
                blocks.add(instances.get(i).getBlock());
            }
            return Jaxb2ConfigurationAst.blocks2NewConfiguration(blocks, this.project.robotFactory.getBlocklyDropdownFactory());
        }

        private ConfigurationAst transformOldConfiguration(BlockSet blockSet, String topBlockName, String sensorsPrefix) {
            Block startingBlock = Jaxb2ConfigurationAst.getTopBlock(blockSet, topBlockName);
            return block2OldConfiguration(startingBlock, this.project.robotFactory.getBlocklyDropdownFactory(), sensorsPrefix);
        }
    }
}
