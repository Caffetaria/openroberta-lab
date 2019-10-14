package de.fhg.iais.roberta.visitor.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;

public class SenseboxConfigurationValidatorWorker extends ArduinoConfigurationValidatorWorker {
    public SenseboxConfigurationValidatorWorker() {
        super(Stream.of("0", "1", "2", "3", "4", "5", "6", "7", "8").collect(Collectors.toList()));
    }

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project) {
        return new SenseboxBrickValidatorVisitor(builder, project.getConfigurationAst(), project.getSSID(), project.getPassword());
    }
}
