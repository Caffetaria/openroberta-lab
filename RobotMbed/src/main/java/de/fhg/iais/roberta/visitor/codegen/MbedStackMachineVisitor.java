package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.*;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbedStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IMbedVisitor<V> {

    private MbedStackMachineVisitor(Configuration configuration, ArrayList<ArrayList<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        MbedStackMachineVisitor<Void> astVisitor = new MbedStackMachineVisitor<>(brickConfiguration, phrasesSet);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, astVisitor.opArray).put(C.FUNCTION_DECLARATION, astVisitor.fctDecls);
        return generatedCode.toString(2);
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = mk(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION);

        return app(o);
    }

    @Override
    public V visitDisplayTextAction(DisplayTextAction<V> displayTextAction) {
        displayTextAction.getMsg().visit(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION).put(C.MODE, displayTextAction.getMode().toString().toLowerCase());

        return app(o);
    }

    @Override
    public V visitImage(Image<V> image) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 5; i++ ) {
            ArrayList<Integer> a = new ArrayList<Integer>();
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.getImage()[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = mk(C.EXPR).put(C.EXPR, image.getKind().getName().toLowerCase());
        o.put(C.VALUE, jsonImage);
        return app(o);
    }

    @Override
    public V visitPredefinedImage(PredefinedImage<V> predefinedImage) {
        final String image = predefinedImage.getImageName().getImageString();
        JSONArray a =
            new JSONArray(
                Arrays.stream(image.split("\\\\n")).map(x -> new JSONArray(Arrays.stream(x.split(",")).mapToInt(Integer::parseInt).toArray())).toArray());

        JSONObject o = mk(C.EXPR).put(C.EXPR, C.IMAGE).put(C.VALUE, a);
        return app(o);

    }

    @Override
    public V visitDisplayImageAction(DisplayImageAction<V> displayImageAction) {
        displayImageAction.getValuesToDisplay().visit(this);
        JSONObject o = mk(C.SHOW_IMAGE_ACTION).put(C.MODE, displayImageAction.getDisplayImageMode().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o = mk(C.STATUS_LIGHT_ACTION).put(C.NAME, "calliope").put(C.PORT, "internal");
        return app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().visit(this);
        toneAction.getDuration().visit(this);
        JSONObject o = mk(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String freq = playNoteAction.getFrequency();
        String duration = playNoteAction.getDuration();
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = mk(C.TONE_ACTION);
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
        String port = motorOnAction.getUserDefinedPort();

        JSONObject o = mk(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        return null;
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction) {
        serialWriteAction.getValue().visit(this);
        JSONObject o = mk(C.SERIAL_WRITE_ACTION);
        return app(o);
    }

    @Override
    public V visitPinWriteValueAction(PinWriteValueAction<V> pinWriteValueAction) {
        pinWriteValueAction.getValue().visit(this);
        String pin = pinWriteValueAction.getPort();
        String mode = pinWriteValueAction.getMode();
        JSONObject o = mk(C.WRITE_PIN_ACTION).put(C.PIN, pin).put(C.MODE, mode.toLowerCase());
        return app(o);
    }

    @Override
    public V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction) {
        imageInvertFunction.getImage().visit(this);
        JSONObject o = mk(C.EXPR).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, C.IMAGE_INVERT_ACTION);
        return app(o);
    }

    @Override
    public V visitGestureSensor(GestureSensor<V> gestureSensor) {
        String mode = gestureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GESTURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        String mode = temperatureSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        String port = keysSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, port).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.LIGHT).put(C.MODE, C.AMBIENTLIGHT).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "calliope");
        } else {
            o = mk(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "calliope");
        }
        return app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        String port = sensorGetSample.getPort();
        String mode = sensorGetSample.getMode();

        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        String mode = compassSensor.getMode();
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitLedOnAction(LedOnAction<V> ledOnAction) {
        ledOnAction.getLedColor().visit(this);
        JSONObject o = mk(C.LED_ON_ACTION);
        return app(o);
    }

    @Override
    public V visitPinGetValueSensor(PinGetValueSensor<V> pinValueSensor) {
        String port = pinValueSensor.getPort();
        String mode = pinValueSensor.getMode();

        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "calliope");

        return app(o);
    }

    @Override
    public V visitPinSetPullAction(PinSetPullAction<V> pinSetPullAction) {
        return null;
    }

    @Override
    public V visitDisplaySetBrightnessAction(DisplaySetBrightnessAction<V> displaySetBrightnessAction) {
        displaySetBrightnessAction.getBrightness().visit(this);
        JSONObject o = mk(C.DISPLAY_SET_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetBrightnessAction(DisplayGetBrightnessAction<V> displayGetBrightnessAction) {
        JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.DISPLAY).put(C.MODE, C.BRIGHTNESS).put(C.NAME, "calliope");
        return app(o);
    }

    @Override
    public V visitDisplaySetPixelAction(DisplaySetPixelAction<V> displaySetPixelAction) {
        displaySetPixelAction.getX().visit(this);
        displaySetPixelAction.getY().visit(this);
        displaySetPixelAction.getBrightness().visit(this);
        JSONObject o = mk(C.DISPLAY_SET_PIXEL_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitDisplayGetPixelAction(DisplayGetPixelAction<V> displayGetPixelAction) {
        displayGetPixelAction.getX().visit(this);
        displayGetPixelAction.getY().visit(this);
        JSONObject o = mk(C.DISPLAY_GET_PIXEL_BRIGHTNESS_ACTION);
        return app(o);
    }

    @Override
    public V visitSingleMotorOnAction(SingleMotorOnAction<V> singleMotorOnAction) {

        return null;
    }

    @Override
    public V visitSingleMotorStopAction(SingleMotorStopAction<V> singleMotorStopAction) {

        return null;
    }

    @Override
    public V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
        return null;
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        return null;
    }

    @Override
    public V visitBothMotorsOnAction(BothMotorsOnAction<V> bothMotorsOnAction) {
        bothMotorsOnAction.getSpeedA().visit(this);
        bothMotorsOnAction.getSpeedB().visit(this);
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
        JSONObject o =
            mk(C.BOTH_MOTORS_ON_ACTION).put(C.PORT_A, bothMotorsOnAction.getPortA().toLowerCase()).put(C.PORT_B, bothMotorsOnAction.getPortB().toLowerCase());

        return app(o);
    }

    @Override
    public V visitBothMotorsStopAction(BothMotorsStopAction<V> bothMotorsStopAction) {
        JSONObject o = mk(C.MOTOR_STOP).put(C.PORT, "ab");
        return app(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (((x - in_min) * (out_max - out_min)) / (in_max - in_min)) + out_min;
    }

    @Override
    public V visitSwitchLedMatrixAction(SwitchLedMatrixAction<V> switchLedMatrixAction) {
        return null;
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        return null;
    }

    @Override
    public V visitRadioSendAction(RadioSendAction<V> radioSendAction) {
        return null;
    }

    @Override
    public V visitRadioReceiveAction(RadioReceiveAction<V> radioReceiveAction) {
        return null;
    }

    @Override
    public V visitRadioSetChannelAction(RadioSetChannelAction<V> radioSetChannelAction) {
        return null;
    }

    @Override
    public V visitRadioRssiSensor(RadioRssiSensor<V> radioRssiSensor) {
        return null;
    }

    @Override
    public V visitHumiditySensor(HumiditySensor<V> humiditySensor) {
        return null;
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        return null;
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        return null;
    }
}
