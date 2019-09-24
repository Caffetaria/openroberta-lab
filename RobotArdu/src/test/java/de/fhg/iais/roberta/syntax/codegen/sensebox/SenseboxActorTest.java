package de.fhg.iais.roberta.syntax.codegen.sensebox;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.util.Util1;

public class SenseboxActorTest {

    @Test
    public void sdCardOledOffTest() throws Exception {
        ConfigurationAst config =
            this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_write_sdcard_display_clear_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource(
                "/ast/actions/sensebox_write_sdcard_display_clear_test.ino",
                "/ast/actions/sensebox_write_sdcard_display_clear_test.xml",
                config);
    }

    @Test
    public void buzzerLedRgbTest() throws Exception {
        ConfigurationAst config =
            this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_buzzer_led_rgb_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource("/ast/actions/sensebox_buzzer_led_rgb_test.ino", "/ast/actions/sensebox_buzzer_led_rgb_test.xml", config);
    }

    @Test
    public void serialOledSendDataTest() throws Exception {
        ConfigurationAst config =
            this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_serial_oled_upload_test_config.xml"));
        this.senseboxHelper
            .compareExistingAndGeneratedSource(
                "/ast/actions/sensebox_serial_oled_upload_test.ino",
                "/ast/actions/sensebox_serial_oled_upload_test.xml",
                config);
    }

    @Test
    public void plottingAccelerometerValuesTest() throws Exception {
        ConfigurationAst config = this.senseboxHelper.regenerateConfiguration(Util1.readResourceContent("/ast/actions/sensebox_plotting_test_config.xml"));
        this.senseboxHelper.compareExistingAndGeneratedSource("/ast/actions/sensebox_plotting_test.ino", "/ast/actions/sensebox_plotting_test.xml", config);
    }
}
