package de.fhg.iais.roberta.util.test.ardu;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.UnoFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCppVisitor;

public class HelperArduinoForXmlTest extends AbstractHelperForXmlTest {

    public HelperArduinoForXmlTest() {
        super(new UnoFactory(new PluginProperties("uno", "", "", Util1.loadProperties("classpath:/uno.properties"))), makeConfiguration());
    }

    private static ConfigurationAst makeConfiguration() {
        Map<String, String> buzzerP = createMap("+", "5");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", true, "buzzer", "buzzer", buzzerP);

        Map<String, String> dropP = createMap("S", "A0");
        ConfigurationComponent drop = new ConfigurationComponent("DROP", true, "drop", "drop", dropP);

        Map<String, String> encoderP = createMap("OUTPUT", "2");
        ConfigurationComponent encoder = new ConfigurationComponent("ENCODER", true, "encoder", "encoder", encoderP);

        Map<String, String> humidityP = createMap("OUTPUT", "2");
        ConfigurationComponent humidity = new ConfigurationComponent("HUMIDITY", true, "humidity", "humidity", humidityP);

        Map<String, String> infraredP = createMap("OUTPUT", "11");
        ConfigurationComponent infrared = new ConfigurationComponent("INFRARED", true, "IR", "IR", infraredP);

        Map<String, String> keyP = createMap("PIN1", "1");
        ConfigurationComponent key = new ConfigurationComponent("KEY", true, "key", "key", keyP);

        Map<String, String> lcdP = createMap("RS", "12", "E", "11", "D4", "5", "D5", "6", "D6", "3", "D7", "2");
        ConfigurationComponent lcd = new ConfigurationComponent("LCD", true, "L", "L", lcdP);

        Map<String, String> lcdi2cP = createMap();
        ConfigurationComponent lcdi2c = new ConfigurationComponent("LCDI2C", true, "L", "L", lcdi2cP);

        Map<String, String> ledP = createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "LED", ledP);

        Map<String, String> rgbledP = createMap("RED", "5", "GREEN", "6", "BLUE", "3");
        ConfigurationComponent rgbled = new ConfigurationComponent("RGBLED", true, "RGBLED", "RGBLED", rgbledP);

        Map<String, String> lightP = createMap("OUTPUT", "A0");
        ConfigurationComponent light = new ConfigurationComponent("LIGHT", true, "light", "light", lightP);

        Map<String, String> moistureP = createMap("S", "A0");
        ConfigurationComponent moisture = new ConfigurationComponent("MOISTURE", true, "moisture", "moisture", moistureP);

        Map<String, String> motionP = createMap("OUTPUT", "7");
        ConfigurationComponent motion = new ConfigurationComponent("MOTION", true, "motion", "motion", motionP);

        Map<String, String> pulseP = createMap("PULSE", "8");
        ConfigurationComponent pulse = new ConfigurationComponent("PULSE", true, "pulse", "pulse", pulseP);

        Map<String, String> stepP = createMap("IN1", "6", "IN2", "5", "IN3", "4", "IN4", "3");
        ConfigurationComponent step = new ConfigurationComponent("STEPMOTOR", true, "step", "step", stepP);

        Map<String, String> potentiometerP = createMap("OUTPUT", "A0");
        ConfigurationComponent potentiometer = new ConfigurationComponent("POTENTIOMETER", true, "potentiometer", "potentiometer", potentiometerP);

        Map<String, String> pulseAP = createMap("S", "A0");
        ConfigurationComponent pulseA = new ConfigurationComponent("PULSE", true, "pulse", "pulse", pulseAP);

        Map<String, String> relayP = createMap("IN", "6");
        ConfigurationComponent relay = new ConfigurationComponent("RELAY", true, "relay", "relay", relayP);

        Map<String, String> rfidP = createMap("RST", "9", "SDA", "10", "SCK", "13", "MOSI", "11", "MISO", "12");
        ConfigurationComponent rfid = new ConfigurationComponent("RFID", true, "rfid", "rfid", rfidP);

        Map<String, String> temperatureP = createMap("OUTPUT", "A0");
        ConfigurationComponent temperature = new ConfigurationComponent("TEMPERATURE", true, "temperature", "temperature", temperatureP);

        Map<String, String> ultrasonicP = createMap("TRIG", "6", "ECHO", "7");
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", true, "ultrasonic", "ultrasonic", ultrasonicP);

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(
                Arrays
                    .asList(
                        buzzer,
                        drop,
                        encoder,
                        humidity,
                        infrared,
                        key,
                        lcd,
                        lcdi2c,
                        led,
                        rgbled,
                        light,
                        moisture,
                        motion,
                        pulse,
                        step,
                        potentiometer,
                        pulseA,
                        relay,
                        rfid,
                        temperature,
                        ultrasonic));
        return builder.build();
    }

    public String generateCpp(String pathToProgramXml, ConfigurationAst configuration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = ArduinoCppVisitor.generate(configuration, transformer.getTree(), true);
        return code;
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename, ConfigurationAst configuration) throws Exception {
        Assert
            .assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""), generateCpp(xmlFilename, configuration).replaceAll("\\s+", ""));
    }
}
