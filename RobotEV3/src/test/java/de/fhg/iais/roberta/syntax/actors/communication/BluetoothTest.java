package de.fhg.iais.roberta.syntax.actors.communication;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3AstTest;
import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BluetoothTest extends Ev3LejosAstTest {

    @Test
    public void connection() throws Exception {
        String a = "NXTConnectionvariablenName=hal.establishConnectionTo(\"101010\");publicvoidrun()throwsException{}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BluetoothConnection.xml",
                                                                                   false);
    }

    @Test
    public void connectionWait() throws Exception {
        String a = "NXTConnectionvariablenName=hal.waitForConnection();publicvoidrun()throwsException{}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BluetoothConnectionWait.xml",
                                                                                   false);
    }

    @Test
    public void send() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.establishConnectionTo(\"\");publicvoidrun()throwsException{hal.sendMessage(\"\", variablenName2);}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BluetoothSend.xml",
                                                                                   false);
    }

    @Test
    public void recive() throws Exception {
        String a =
            "NXTConnectionvariablenName2=hal.waitForConnection();publicvoidrun()throwsException{hal.drawText(String.valueOf(hal.readMessage(variablenName2)),0,0);}";
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/actions/action_BluetoothReceive.xml",
                                                                                   false);
    }
}
