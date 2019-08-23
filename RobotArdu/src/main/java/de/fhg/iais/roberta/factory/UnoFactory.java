package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractConfigurationValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.UnoConfigurationValidatorWorker;

public class UnoFactory extends AbstractArduinoFactory {

    public UnoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public AbstractConfigurationValidatorVisitor getRobotConfigurationCheckVisitor() {
        return new UnoConfigurationValidatorWorker();
    }
}