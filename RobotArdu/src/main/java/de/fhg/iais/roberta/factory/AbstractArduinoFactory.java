package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ArduinoCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCppVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.ArduinoBrickValidatorVisitor;

public abstract class AbstractArduinoFactory extends AbstractRobotFactory {
    public AbstractArduinoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new ArduinoCompilerWorkflow(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return null;
    }

    @Override
    public AbstractBrickValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return new ArduinoBrickValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return ArduinoCppVisitor.generate(brickConfiguration, phrasesSet, withWrapping);
    }
}
