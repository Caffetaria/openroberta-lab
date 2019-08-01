package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public interface IRobotFactory {

    BlocklyDropdownFactory getBlocklyDropdownFactory();

    /**
     * Get the compiler workflow object for this robot.
     *
     * @return
     */
    ICompilerWorkflow getRobotCompilerWorkflow();

    /**
     * Get the compiler workflow object for the simulation of this robot.
     *
     * @return
     */
    ICompilerWorkflow getSimCompilerWorkflow();

    /**
     * Get the file extension of the specific language for this robot. This is used when we want to download locally the source code into a file.
     */
    String getFileExtension();

    String getProgramToolboxBeginner();

    String getProgramToolboxExpert();

    String getProgramDefault();

    String getConfigurationToolbox();

    String getConfigurationDefault();

    String getRealName();

    Boolean hasSim();

    Boolean hasMultipleSim();

    String getInfo();

    Boolean isBeta();

    String getConnectionType();

    default String getVendorId() {
        return null;
    }

    default String getCommandline() {
        return null;
    }

    default String getSignature() {
        return null;
    }

    Boolean hasConfiguration();

    AbstractSimValidatorVisitor getSimProgramCheckVisitor(ConfigurationAst brickConfiguration);

    AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration);

    default AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst robotConfiguration, String SSID, String password) {
        return getRobotProgramCheckVisitor(robotConfiguration);
    }

    String getGroup();

    String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping);

    default String getMenuVersion() {
        return null;
    }

    PluginProperties getPluginProperties();

    Boolean hasWlanCredentials();

    /**
     * Returns the helper method generator for usage and definition of possibly needed helper methods.
     *
     * @return the helper method generator
     */
    HelperMethodGenerator getHelperMethodGenerator();
    
    default List<String> getWorkerNames() {
        return null;
    }

    void execute(Project project, List<String> workerNames);

    default public List<IWorker> getWorkerPipe(String workflow) {
        throw new DbcException("Get worker pipe fails");
    }
}
