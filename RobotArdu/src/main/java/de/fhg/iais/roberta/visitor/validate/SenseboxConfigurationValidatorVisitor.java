package de.fhg.iais.roberta.visitor.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.transformer.Project;

public class SenseboxConfigurationValidatorVisitor extends ArduinoConfigurationValidatorVisitor {
    @Override
    public void execute(Project project) {
        super.setFreePins(Stream.of("0", "1", "2", "3", "4", "5", "6").collect(Collectors.toList()));
        super.execute(project);
    }
}
