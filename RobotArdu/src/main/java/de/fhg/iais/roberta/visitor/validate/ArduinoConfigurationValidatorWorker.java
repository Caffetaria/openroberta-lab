package de.fhg.iais.roberta.visitor.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class ArduinoConfigurationValidatorWorker extends AbstractConfigurationValidatorVisitor {

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

        Map<String, String> result = new HashMap<>();
        if ( this.incorrectPin != null && this.failingBlock != null ) {
            result.put(this.failingBlock, this.incorrectPin);
        }
        Map<Key, Map<String, String>> validationResults = new HashMap<>();
        if ( this.resultKey != null ) {
            validationResults.put(this.resultKey, result);
        }
        project.setValidationResults(validationResults);

        ArduinoBrickValidatorVisitor visitor = new ArduinoBrickValidatorVisitor(project.getConfigurationAst());
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(visitor); // TODO: REALLY REALLY BAD NAME !!!
            }
        }
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
        String blockType = configurationComponent.getComponentType();
        List<String> blockPins = new ArrayList<>();
        componentProperties.forEach((k, v) -> {
            if ( !this.freePins.contains(v) ) {
                this.errorCount++;
                this.incorrectPin = v;
                this.failingBlock = blockType;
                this.resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED_WITH_PARAMETERS;
                configurationComponent.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
            } else {
                blockPins.add(v);
                this.freePins.removeIf(s -> s.equals(v));
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
