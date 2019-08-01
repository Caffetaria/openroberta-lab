package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class ArduinoConfigurationValidatorVisitor extends AbstractConfigurationValidatorVisitor {

    private List<String> freePins;
    private String incorrectPin;
    private String failingBlock;
    private Key resultKey;
    int errorCount;

    @Override
    public String getName() {
        return "arduinoConfigValidator";
    }

    @Override
    public void execute(Project project) {
        this.incorrectPin = null;
        this.failingBlock = null;
        this.resultKey = null;
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> {
            checkConfigurationBlock(v);
        });
    }

    @Override
    public Map<String, String> getResult() {
        Map<String, String> result = new HashMap<>();
        result.put("BLOCK", this.failingBlock);
        result.put("PIN", this.incorrectPin);
        return result;
    }

    @Override
    public Key getResultKey() {
        return this.resultKey;
    }

    public void setFreePins(List<String> freePins) {
        this.freePins = freePins;
    }

    public void checkConfigurationBlock(ConfigurationComponent configurationComponent) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        /*Map<String, List<String>> inputToPinsMapping,*/
        String blockType = configurationComponent.getComponentType();
        List<String> blockPins = new ArrayList<>();
        componentProperties
            .forEach(
                (k, v) -> {
                    //if ( inputToPinsMapping.containsKey(k) ) {
                    //List<String> allowedPins = inputToPinsMapping.get(k);
                    if ( /*!(allowedPins.contains(v) &&*/ !this.freePins.contains(v) ) {
                        //System.err.println("Pin " + v + " is not allowed for " + k + " input/output");
                        //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        this.errorCount++;
                        this.incorrectPin = v;
                        this.failingBlock = blockType;
                        this.resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS;
                        configurationComponent.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        //throw new DbcException("Pin " + v + " is not allowed for " + k + " input/output");
                    } else {
                        blockPins.add(v);
                        this.freePins.removeIf(s -> s.equals(v));
                    }
                    /*} else {
                        System.err.println("Input not allowed " + k);
                        block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                        errorCount++;
                    }*/
                });
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            //System.err.println("Pins must be unique");
            //block.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            this.errorCount++;
            this.incorrectPin = "NON_UNIQUE";
            this.resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS;
            configurationComponent.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            //throw new DbcException("Pins must be unique");
        }
    }
}
