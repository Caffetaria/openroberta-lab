package de.fhg.iais.roberta.visitor.codegen;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class RaspberryPiPythonGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(RaspberryPiPythonGeneratorWorker.class);

    @Override
    public String getName() {
        return "RaspberryPiPythonGenerator";
    }

    @Override
    public void execute(Project project) {
        RaspberryPiPythonVisitor visitor =
            new RaspberryPiPythonVisitor(
                project.getConfigurationAst(),
                project.getProgramAst().getTree(),
                1,
                project.getLanguage(),
                project.getHelperMethodGenerator());
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(true);
    }

    @Override
    public Map<String, String> getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Key getResultKey() {
        // TODO Auto-generated method stub
        return null;
    }
}
