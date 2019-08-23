package de.fhg.iais.roberta.visitor;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class WedoCodeGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(WedoCodeGeneratorWorker.class);

    @Override
    public String getName() {
        return "WedoCodeGenerator";
    }

    @Override
    public void execute(Project project) {
        WeDoStackMachineVisitor<Void> visitor = new WeDoStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
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
