package de.fhg.iais.roberta.visitor.validate;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;

public class Ev3ProgramValidatorWorker implements IWorker {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execute(Project project) {
        AbstractProgramValidatorVisitor visitor = new Ev3BrickValidatorVisitor(project.getConfigurationAst());
        visitor.check(project.getProgramAst().getTree());
        final int errorCounter = visitor.getErrorCount();
        Map<Key, Map<String, String>> validationResults = new HashMap<>();
        Map<String, String> detailedInfo = new HashMap<>();
        if ( errorCounter > 0 ) {
            detailedInfo.put("EV3", Key.PROGRAM_INVALID_STATEMETNS.toString());
            validationResults.put(Key.PROGRAM_INVALID_STATEMETNS, detailedInfo);
        }
        project.setValidationResults(validationResults);
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
