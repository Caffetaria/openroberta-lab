package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class ArduinoConfigurationValidatorWorker extends AbstractValidatorWorker {

    private final List<String> freePins;
    private List<String> currentFreePins;
    private String incorrectPin;
    private String failingBlock;
    private Key resultKey;
    private int errorCount;

    public ArduinoConfigurationValidatorWorker(List<String> freePins) {
        this.freePins = Collections.unmodifiableList(freePins);
    }

    @Override
    public void execute(Project project) {
        this.currentFreePins = new ArrayList<>(this.freePins);
        this.incorrectPin = null;
        this.failingBlock = null;
        this.resultKey = null;
        this.errorCount = 0;
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkConfigurationBlock(v));

        if ( this.resultKey != null ) {
            project.setResult(this.resultKey);
            project.addResultParam("BLOCK", this.failingBlock);
            project.addResultParam("PIN", this.incorrectPin);
            project.addToErrorCounter(this.errorCount);
        }
        super.execute(project);
    }

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project) {
        return new ArduinoBrickValidatorVisitor(builder, project.getConfigurationAst());
    }

    @Override
    protected String getBeanName() {
        return "ProgramValidator";
    }

    private void checkConfigurationBlock(ConfigurationComponent configurationComponent) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        String blockType = configurationComponent.getComponentType();
        List<String> blockPins = new ArrayList<>();
        componentProperties.forEach((k, v) -> {
            if ( k.equals("INPUT") || k.equals("OUTPUT") ) {
                if ( !this.currentFreePins.contains(v) ) {
                    this.errorCount++;
                    this.incorrectPin = v;
                    this.failingBlock = blockType;
                    this.resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS;
                    configurationComponent.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                } else {
                    blockPins.add(v);
                    this.currentFreePins.removeIf(s -> s.equals(v));
                }
            }
        });
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            this.errorCount++;
            this.incorrectPin = "NON_UNIQUE";
            this.resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS;
            configurationComponent.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
        }
    }
}
