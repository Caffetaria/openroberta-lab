package de.fhg.iais.roberta.ast.syntax.actors.communication;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore
public class BluetoothTest extends AstTest {

    @Test
    public void connection() throws Exception {
        String a = "NXTConnectionvariablenName=hal.establishConnectionTo(\"101010\");publicvoidrun(){}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_BluetoothConnection.xml");
    }

    @Test
    public void connectionWait() throws Exception {
        String a = "NXTConnectionvariablenName=hal.waitForConnection();publicvoidrun(){}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_BluetoothConnectionWait.xml");
    }

    @Test
    public void send() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.establishConnectionTo(\"\");publicvoidrun(){hal.sendMessage(\"\", variablenName2);}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_BluetoothSend.xml");
    }

    @Test
    public void recive() throws Exception {
        String a = "NXTConnectionvariablenName2=hal.waitForConnection();publicvoidrun(){hal.drawText(String.valueOf(hal.readMessage(variablenName2)),0,0);}";
        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/ast/actions/action_BluetoothReceive.xml");
    }
}
