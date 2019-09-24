package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;

public class SenseboxConfigurationValidatorWorker extends ArduinoConfigurationValidatorWorker {
    @Override
    public void execute(Project project) {
        super.setFreePins(Stream.of("0", "1", "2", "3", "4", "5", "6").collect(Collectors.toList()));
        UsedHardwareBean.Builder builder = new Builder();
        SenseboxBrickValidatorVisitor visitor =
            new SenseboxBrickValidatorVisitor(builder, project.getConfigurationAst(), project.getSSID(), project.getPassword());
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }
    }
}
