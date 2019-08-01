package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.SenseboxCompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.codegen.SenseboxCppVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.SenseboxBrickValidatorVisitor;

public class SenseboxFactory extends AbstractRobotFactory {

    public SenseboxFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new SenseboxCompilerWorkflow(this.pluginProperties);
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

    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration, String SSID, String password) {
        return new SenseboxBrickValidatorVisitor(brickConfiguration, SSID, password);
    }

    @Override
    public String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return SenseboxCppVisitor.generate(brickConfiguration, phrasesSet, withWrapping);
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return null;
    }
}
