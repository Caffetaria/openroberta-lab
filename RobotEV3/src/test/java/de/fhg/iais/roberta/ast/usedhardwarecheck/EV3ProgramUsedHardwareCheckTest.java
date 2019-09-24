package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;

public class EV3ProgramUsedHardwareCheckTest extends AstTest {

    private static ConfigurationAst makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", "B", motorBproperties);

        Map<String, String> motorDproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorD = new ConfigurationComponent("MEDIUM", true, "D", "D", motorDproperties);

        ConfigurationComponent touchSensor = new ConfigurationComponent("TOUCH", false, "S1", "1", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S2", "2", Collections.emptyMap());

        final ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorD, touchSensor, ultrasonicSensor));
        return builder.build();
    }

    private static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    private void runTest(String pathToXml, String sensorResult, String actorResult) throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrasesOfPhrases = UnitTestHelper.getAst(testFactory, pathToXml);
        UsedHardwareBean.Builder builder = new Builder();
        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(builder, phrasesOfPhrases, makeConfiguration());
        //        for ( ArrayList<Phrase<Void>> phrases : phrasesOfPhrases ) {
        //            for ( Phrase<Void> phrase : phrases ) {
        //                phrase.visit(checkVisitor);
        //            }
        //        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals(sensorResult, bean.getUsedSensors().toString());
        Assert.assertEquals(actorResult, bean.getUsedActors().toString());
    }

    @Test
    public void testIt() throws Exception {
        runTest("/visitors/hardware_check.xml", "[]", "[]");
        runTest("/visitors/hardware_check1.xml", "[]", "[]");
        runTest("/visitors/hardware_check2.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [3, COLOR, COLOUR]]", "[UsedActor [B, LARGE]]");
        runTest("/visitors/hardware_check3.xml", "[UsedSensor [1, TOUCH, DEFAULT], UsedSensor [4, ULTRASONIC, DISTANCE]]", "[UsedActor [B, LARGE]]");
        runTest(
            "/visitors/hardware_check4.xml",
            "[UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE], UsedSensor [1, TOUCH, DEFAULT]]",
            "[UsedActor [B, LARGE], UsedActor [A, LARGE]]");
        runTest("/ast/control/wait_stmt.xml", "[]", "[]");
        runTest("/ast/control/wait_stmt1.xml", "[UsedSensor [1, TOUCH, PRESSED]]", "[]");
        runTest("/ast/control/wait_stmt2.xml", "[]", "[]");
        runTest("/ast/control/wait_stmt3.xml", "[UsedSensor [1, INFRARED, DISTANCE]]", "[]");
        runTest("/visitors/hardware_check5.xml", "[]", "[UsedActor [B, LARGE]]");
        runTest(
            "/visitors/hardware_check6.xml",
            "[UsedSensor [3, COLOR, COLOUR], UsedSensor [4, INFRARED, DISTANCE], UsedSensor [4, ULTRASONIC, DISTANCE]]",
            "[]");
        runTest("/ast/methods/method_return_3.xml", "[]", "[]");
        runTest("/visitors/hardware_check7.xml", "[UsedSensor [3, COLOR, COLOUR], UsedSensor [3, COLOR, AMBIENTLIGHT], UsedSensor [4, COLOR, LIGHT]]", "[]");
        runTest("/visitors/hardware_check8.xml", "[]", "[UsedActor [D, MEDIUM]]");
    }
}
