package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

public class SwitchLedMatrixActionTest {
    

    @Test
    public void calliopeGenerateSource_GivenXml_ShouldGenerateSameSource() throws Exception {
        this.h.compareExistingAndGeneratedSource("/action/switch_led_matrix.cpp", "/action/switch_led_matrix.xml");
    }
}
