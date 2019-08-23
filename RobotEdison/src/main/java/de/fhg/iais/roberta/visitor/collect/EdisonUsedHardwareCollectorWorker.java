package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class EdisonUsedHardwareCollectorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(EdisonUsedHardwareCollectorWorker.class);

    @Override
    public String getName() {
        return "EdisonUsedHardwareCollector";
    }

    @Override
    public void execute(Project project) {
        final EdisonUsedHardwareCollectorVisitor visitor =
            new EdisonUsedHardwareCollectorVisitor(project.getProgramAst().getTree(), project.getConfigurationAst());
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }
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
