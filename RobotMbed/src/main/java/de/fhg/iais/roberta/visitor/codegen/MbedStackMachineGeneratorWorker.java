package de.fhg.iais.roberta.visitor.codegen;

import java.util.Map;

import org.json.JSONObject;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class MbedStackMachineGeneratorWorker implements IWorker {

    @Override
    public String getName() {
        return "MbedStackMachineGenerator";
    }

    @Override
    public void execute(Project project) {
        MbedStackMachineVisitor<Void> visitor = new MbedStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
        //project.setSourceCode(MbedStackMachineVisitor.generate(project.getConfigurationAst(), project.getProgramAst().getTree()));
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
