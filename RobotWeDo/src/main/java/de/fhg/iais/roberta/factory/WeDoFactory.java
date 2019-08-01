package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.WeDoCompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.WedoBrickValidatorVisitor;

public class WeDoFactory extends AbstractRobotFactory {
    public WeDoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new WeDoCompilerWorkflow(this.pluginProperties);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return null;
    }

    @Override
    public AbstractBrickValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return new WedoBrickValidatorVisitor<Void>(brickConfiguration);
    }

    @Override
    public String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return WeDoStackMachineVisitor.generate(brickConfiguration, phrasesSet);
    }
}
