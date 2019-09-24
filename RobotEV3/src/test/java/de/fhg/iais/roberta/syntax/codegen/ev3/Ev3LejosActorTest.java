package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;

public class Ev3LejosActorTest {

    ConfigurationAst configuration = makeConfigurationWithMediumAndOther();

    public static ConfigurationAst makeConfigurationWithMediumAndOther() {
        Map<String, String> motorAproperties = HelperEv3ForXmlTest.createMap("MOTOR_REGULATION", "FALSE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorA = new ConfigurationComponent("OTHER", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = HelperEv3ForXmlTest.createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorB = new ConfigurationComponent("MEDIUM", true, "B", "B", motorBproperties);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(18f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB));
        ConfigurationAst configuration = builder.build();
        configuration.setRobotName("ev3lejosV1");
        return configuration;
    }

    @Test
    public void mediumAndOtherConsumerTest() throws Exception {
        this.ev3lejosHelper
            .compareExistingAndGeneratedJavaSource(
                "/ast/actions/ev3_actors_medium_other_test.java",
                "/ast/actions/ev3_actors_medium_other_test.xml",
                this.configuration);
    }
}
