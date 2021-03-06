package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ClearDisplayActionTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void clearDisplay() throws Exception {
        String a = "publicvoidrun()throwsException{hal.clearDisplay();}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_ClearDisplay.xml");
    }
}
