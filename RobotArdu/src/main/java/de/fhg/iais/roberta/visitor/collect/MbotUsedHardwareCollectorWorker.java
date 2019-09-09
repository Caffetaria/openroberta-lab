package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class MbotUsedHardwareCollectorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(MbotUsedHardwareCollectorWorker.class);

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new Builder();
        final MbotUsedHardwareCollectorVisitor visitor =
            new MbotUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }
        UsedHardwareBean bean = builder.build();
        project.addWorkerResult("CollectedHardware", bean);
    }
}
