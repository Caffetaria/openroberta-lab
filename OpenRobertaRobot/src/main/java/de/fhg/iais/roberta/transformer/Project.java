package de.fhg.iais.roberta.transformer;

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

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
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
    private final List<Key> errorMessages = new ArrayList<>();
    private ProgramAst<Void> program = null;
    private ConfigurationAst configuration = null;
    Map<Key, Map<String, String>> validationResults = new HashMap<>();
    private StringBuilder sourceCode = new StringBuilder();
    private final StringBuilder indentation = new StringBuilder();
    private String compiledHex = "";

    private HelperMethodGenerator helperMethodGenerator;

    private String token;
    private String robot;
    private String programName;
    private String programBlockSet;
    private String configurationName;
    private String configurationBlockSet;
    private String fileExtension;
    private String SSID;
    private String password;
    private ILanguage language;

    private Project(ProgramAst<Void> program, ConfigurationAst configuration) {
        this.program = program;
        this.configuration = configuration;
    }

    private Project(
        String token,
        String robot,
        String programName,
        String programBlockSet,
        String configName,
        String configurationBlockSet,
        String SSID,
        String password,
        ILanguage language) {
        this.token = token;
        this.robot = robot;
        this.programName = programName;
        this.programBlockSet = programBlockSet;
        this.configurationName = configName;
        this.configurationBlockSet = configurationBlockSet;
        this.SSID = SSID;
        this.password = password;
        this.language = language;

        // make transformation here instead of just setting fields?
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getRobot() {
        return this.robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramBlockSet() {
        return this.programBlockSet;
    }

    public void setProgramBlockSet(String programBlockSet) {
        this.programBlockSet = programBlockSet;
    }

    public String getConfigurationName() {
        return this.configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getConfigurationBlockSet() {
        return this.configurationBlockSet;
    }

    public void setConfigurationBlockSet(String configurationBlockSet) {
        this.configurationBlockSet = configurationBlockSet;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getSSID() {
        return this.SSID;
    }

    public void setSSID(String sSID) {
        this.SSID = sSID;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ILanguage getLanguage() {
        return this.language;
    }

    public void setLanguage(ILanguage language) {
        this.language = language;
    }

    public StringBuilder getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = new StringBuilder(sourceCode);
    }

    public StringBuilder getIndentation() {
        return this.indentation;
    }

    public static Project setupWithExportXML(IRobotFactory factory, String exportXmlAsString) {
        String[] parts = exportXmlAsString.split("\\\\s*</program>\\\\s*<config>\\\\s*");
        String[] programParts = parts[0].split("<program>");
        String program = programParts[1];
        String[] configurationParts = parts[1].split("</config>");
        String configuration = configurationParts[1];
        return setupWithExportXML(factory, program, configuration);
    }

    public static Project setupWithExportXML(IRobotFactory factory, String programXmlAsString, String configurationXmlAsString) {
        return Project.transform(factory, programXmlAsString, configurationXmlAsString);
    }

    public HelperMethodGenerator getHelperMethodGenerator() {
        return this.helperMethodGenerator;
    }

    public void setHelperMethodGenerator(HelperMethodGenerator helperMethodGenerator) {
        this.helperMethodGenerator = helperMethodGenerator;
    }

    public Map<Key, Map<String, String>> getValidationResults() {
        return this.validationResults;
    }

    public Map<String, String> getPinValidationResults() {
        Map<String, String> result = new HashMap<>();
        String[] keys =
            {
                "BLOCK",
                "PIN"
            };
        Map<String, String> values = this.validationResults.get(Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS);
        if ( values != null ) {
            result.put(keys[0], (String) values.keySet().toArray()[0]);
            result.put(keys[1], (String) values.values().toArray()[0]);
        }
        return result;
    }

    public void setValidationResults(Map<Key, Map<String, String>> validationResults) {
        this.validationResults = validationResults;
    }

    /**
     * Transforms blockly xml program and brick configuration into AST.
     *
     * @param programText as XML
     * @param configurationText as XML
     * @return
     */
    private static Project transform(IRobotFactory factory, String programText, String configurationText) {
        Key errorMessage = null;
        if ( programText == null || programText.trim().equals("") ) {
            errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND;
        } else if ( configurationText == null || configurationText.trim().equals("") ) {
            errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND;
        }

        Jaxb2ProgramAst<Void> programTransformer = null;
        try {
            programTransformer = JaxbHelper.generateProgramTransformer(factory, programText);
        } catch ( Exception e ) {
            LOG.error("Transformer failed", e);
            errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
        }
        ConfigurationAst brickConfiguration = null;
        try {
            brickConfiguration = factory.getRobotCompilerWorkflow().generateConfiguration(factory, configurationText);
        } catch ( Exception e ) {
            LOG.error("Generation of the configuration failed", e);
            errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED;
        }
        Project project = new Project(programTransformer.getData(), brickConfiguration);
        if ( errorMessage != null ) {
            project.errorMessages.add(errorMessage);
        }
        return project;
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
        private String token;
        private String robot;
        private String programName;
        private String programBlockSet;
        private String configurationName;
        private String configurationBlockSet;
        private String fileExtension;
        private String SSID;
        private String password;
        private ILanguage language;
        private IRobotFactory factory;

        public Builder() {
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setRobot(String robot) {
            this.robot = robot;
            return this;
        }

        public Builder setProgramName(String programName) {
            this.programName = programName;
            return this;
        }

        public Builder setProgramBlockSet(String programBlockSet) {
            this.programBlockSet = programBlockSet;
            return this;
        }

        public Builder setConfigurationName(String configurationName) {
            this.configurationName = configurationName;
            return this;
        }

        public Builder setConfigurationBlockSet(String configurationBlockSet) {
            this.configurationBlockSet = configurationBlockSet;
            return this;
        }

        public Builder setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
            return this;
        }

        public Builder setSSID(String sSID) {
            this.SSID = sSID;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setLanguage(ILanguage language) {
            this.language = language;
            return this;
        }

        public Builder setFactory(IRobotFactory factory) {
            this.factory = factory;
            return this;
        }

        public Project build() {
            Project project = Project.setupWithExportXML(this.factory, this.programBlockSet, this.configurationBlockSet);
            project.setHelperMethodGenerator(this.factory.getHelperMethodGenerator());
            project.setToken(this.token);
            project.setRobot(this.robot);
            project.setProgramName(this.programName);
            project.setProgramBlockSet(this.programBlockSet);
            project.setConfigurationName(this.configurationName);
            project.setConfigurationBlockSet(this.configurationBlockSet);
            project.setFileExtension(this.fileExtension);
            project.setSSID(this.SSID);
            project.setPassword(this.password);
            project.setLanguage(this.language);
            project.getConfigurationAst().setRobotName(this.robot);
            return project;
        }

    }
}
