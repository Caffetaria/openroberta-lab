package de.fhg.iais.roberta.visitor.codegen;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class NxtCxxGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(NxtCxxGeneratorWorker.class);

    @Override
    public String getName() {
        return "NxtCxxGenerator";
    }

    @Override
    public void execute(Project project) {
        NxtNxcVisitor visitor = new NxtNxcVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), 1);
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
