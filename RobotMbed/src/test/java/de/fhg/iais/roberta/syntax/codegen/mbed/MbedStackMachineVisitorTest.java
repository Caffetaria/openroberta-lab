package de.fhg.iais.roberta.syntax.codegen.mbed;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.codegen.MbedStackMachineGeneratorWorker;

public class MbedStackMachineVisitorTest extends CalliopeAstTest {

    @Test
    public void mbedDisplayTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent("/stack_machine/display.json"),
                                    "/stack_machine/display.xml", new MbedStackMachineGeneratorWorker());
    }

    @Test
    public void mbedLightTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent( "/stack_machine/light.json"), "/stack_machine/light.xml", new MbedStackMachineGeneratorWorker());
    }

    @Test
    public void mbedMoveTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent( "/stack_machine/move.json"), "/stack_machine/move.xml", new MbedStackMachineGeneratorWorker());
    }

    @Test
    public void mbedSoundTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent( "/stack_machine/sound.json"), "/stack_machine/sound.xml", new MbedStackMachineGeneratorWorker());
    }

    @Test
    public void mbedPinTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent( "/stack_machine/pin.json"), "/stack_machine/pin.xml", new MbedStackMachineGeneratorWorker());
    }

    @Test
    public void mbedSensorsTest() throws Exception {
        UnitTestHelper.checkWorkers(testFactory,
                                    Util1.readResourceContent( "/stack_machine/sensors.json"), "/stack_machine/sensors.xml", new MbedStackMachineGeneratorWorker());
    }

}
