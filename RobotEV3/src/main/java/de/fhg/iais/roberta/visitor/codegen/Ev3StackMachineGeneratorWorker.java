package de.fhg.iais.roberta.visitor.codegen;

import org.json.JSONObject;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class Ev3StackMachineGeneratorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Ev3StackMachineVisitor<Void> visitor = new Ev3StackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree(), project.getLanguage());
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }
}
