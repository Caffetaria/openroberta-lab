package de.fhg.iais.roberta.visitor.codegen;

import java.util.Map;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class MicrobitPythonGeneratorWorker implements IWorker {

    @Override
    public String getName() {
        return "MicrobitPythonGenerator";
    }

    @Override
    public void execute(Project project) {
        MicrobitPythonVisitor astVisitor =
            new MicrobitPythonVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), 0, project.getHelperMethodGenerator());
        astVisitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        astVisitor.generateCode(true);
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
