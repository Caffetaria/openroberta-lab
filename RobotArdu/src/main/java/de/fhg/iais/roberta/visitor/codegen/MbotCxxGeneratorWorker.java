package de.fhg.iais.roberta.visitor.codegen;

import java.util.Map;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class MbotCxxGeneratorWorker implements IWorker {

    @Override
    public String getName() {
        return "MbotCxxGenerator";
    }

    @Override
    public void execute(Project project) {
        MbotCppVisitor visitor = new MbotCppVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), 1);
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
