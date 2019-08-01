package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.MbedSimCompilerWorkflow;
import de.fhg.iais.roberta.codegen.MicrobitCompilerWorkflow;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbedBoardValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MicrobitSimValidatorVisitor;

public class MicrobitFactory extends AbstractMbedFactory {
    public MicrobitFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new MicrobitCompilerWorkflow(this.pluginProperties, this.helperMethodGenerator);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return new MbedSimCompilerWorkflow(this.pluginProperties);
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return new MicrobitSimValidatorVisitor(brickConfiguration);
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(ConfigurationAst brickConfiguration) {
        return new MbedBoardValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(ConfigurationAst brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }
}
