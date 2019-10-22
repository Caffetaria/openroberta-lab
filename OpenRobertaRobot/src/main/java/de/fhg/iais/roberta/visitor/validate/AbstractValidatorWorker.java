package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;

public abstract class AbstractValidatorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();
        AbstractProgramValidatorVisitor visitor = getVisitor(builder, project);
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.accept(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }

        int errorCounter = visitor.getErrorCount();
        if ( errorCounter > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToErrorCounter(errorCounter);
        }
    }

    protected abstract AbstractProgramValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project);

    protected abstract String getBeanName();
}
