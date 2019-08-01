package de.fhg.iais.roberta.visitor;

import java.util.Map;

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
        /*final Ev3JavaVisitor visitor =
            new Ev3JavaVisitor(project.getProgramName(), project.getProgramAst().getTree(), project.getConfigurationAst(), 4, project.getLanguage());
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }
        project.setSourceCode(visitor.getSb().toString()); */
        project.setSourceCode(WeDoStackMachineVisitor.generate(project.getConfigurationAst(), project.getProgramAst().getTree()));
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
