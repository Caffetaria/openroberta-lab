package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class Ev3StackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IEv3Visitor<V> {

    public Ev3StackMachineVisitor(ConfigurationAst configuration, ArrayList<ArrayList<Phrase<Void>>> phrases, ILanguage language) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        String color = "";
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
            case "#000000":
                color = "BLACK";
                break;
            case "#0057A6":
                color = "BLUE";
                break;
            case "#00642E":
                color = "GREEN";
                break;
            case "#F7D117":
                color = "YELLOW";
                break;
            case "#B30006":
                color = "RED";
                break;
            case "#FFFFFF":
                color = "WHITE";
                break;
            case "#532115":
                color = "BROWN";
                break;
            case "#585858":
                color = "NONE";
                break;
            default:
                colorConst.addInfo(NepoInfo.error("SIM_BLOCK_NOT_SUPPORTED"));
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        final JSONObject o = this.mk(C.EXPR).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, color);
        return this.app(o);
    }

    @Override
    public V visitShowPictureAction(ShowPictureAction<V> showPictureAction) {
        final String image = showPictureAction.getPicture().toString();
        final JSONObject o = this.mk(C.SHOW_IMAGE_ACTION).put(C.IMAGE, image).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        final JSONObject o = this.mk(C.CLEAR_DISPLAY_ACTION);

        return this.app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        final JSONObject o = this.mk(C.STATUS_LIGHT_ACTION).put(C.NAME, "ev3").put(C.PORT, "internal");
        return this.app(o);
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        final JSONObject o = this.mk(C.TONE_ACTION);
        return this.app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        final String freq = playNoteAction.getFrequency();
        final String duration = playNoteAction.getDuration();
        this.app(this.mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        this.app(this.mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        final JSONObject o = this.mk(C.TONE_ACTION);
        return this.app(o);
    }

    @Override
    public V visitPlayFileAction(PlayFileAction<V> playFileAction) {
        final String image = playFileAction.getFileName().toString();
        final JSONObject o = this.mk(C.PLAY_FILE_ACTION).put(C.FILE, image).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitVolumeAction(VolumeAction<V> volumeAction) {
        JSONObject o;
        if ( volumeAction.getMode() == VolumeAction.Mode.GET ) {
            o = this.mk(C.GET_VOLUME);
        } else {
            volumeAction.getVolume().accept(this);
            o = this.mk(C.SET_VOLUME_ACTION);
        }
        return this.app(o);
    }

    @Override
    public V visitSetLanguageAction(SetLanguageAction<V> setLanguageAction) {
        final String language = this.getLanguageString(setLanguageAction.getLanguage());
        final JSONObject o = this.mk(C.SET_LANGUAGE_ACTION).put(C.LANGUAGE, language);
        return this.app(o);
    }

    @Override
    public V visitSayTextAction(SayTextAction<V> sayTextAction) {
        sayTextAction.getMsg().accept(this);
        final BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( sayTextAction.getSpeed().getKind().equals(emptyBlock) ) {
            final NumConst<V> n = NumConst.make("30");
            n.accept(this);
        } else {
            sayTextAction.getSpeed().accept(this);
        }
        if ( sayTextAction.getPitch().getKind().equals(emptyBlock) ) {
            final NumConst<V> n = NumConst.make("50");
            n.accept(this);
        } else {
            sayTextAction.getPitch().accept(this);
        }
        final JSONObject o = this.mk(C.SAY_TEXT_ACTION);

        return this.app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        final String port = motorGetPowerAction.getUserDefinedPort();
        final JSONObject o = this.mk(C.MOTOR_GET_POWER).put(C.PORT, port.toLowerCase());
        return this.app(o);
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        final MotorDuration<V> duration = driveAction.getParam().getDuration();
        this.appendDuration(duration);
        final ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        final IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = this.getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        final JSONObject o = this.mk(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3");
        if ( duration != null ) {
            this.app(o);
            return this.app(this.mk(C.STOP_DRIVE).put(C.NAME, "ev3"));
        } else {
            return this.app(o);
        }
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        final MotorDuration<V> duration = turnAction.getParam().getDuration();
        this.appendDuration(duration);
        final ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        final IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        ITurnDirection turnDirection = turnAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = this.getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }
        final JSONObject o = this.mk(C.TURN_ACTION).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.NAME, "ev3");
        if ( duration != null ) {
            this.app(o);
            return this.app(this.mk(C.STOP_DRIVE).put(C.NAME, "ev3"));
        } else {
            return this.app(o);
        }

    }

    @Override
    public V visitCurveAction(CurveAction<V> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        final MotorDuration<V> duration = curveAction.getParamLeft().getDuration();
        this.appendDuration(duration);
        final ConfigurationComponent leftMotor = this.configuration.getFirstMotor(SC.LEFT);
        final IDriveDirection leftMotorRotationDirection = DriveDirection.get(leftMotor.getProperty(SC.MOTOR_REVERSE));
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = this.getDriveDirection(curveAction.getDirection() == DriveDirection.FOREWARD);
        }
        final JSONObject o = this.mk(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "ev3");
        if ( duration != null ) {
            this.app(o);
            return this.app(this.mk(C.STOP_DRIVE).put(C.NAME, "ev3"));
        } else {
            return this.app(o);
        }
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        final JSONObject o = this.mk(C.STOP_DRIVE).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        motorOnAction.getParam().getSpeed().accept(this);
        final String port = motorOnAction.getUserDefinedPort();
        final JSONObject o = this.mk(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase());
        if ( isDuration ) {
            final String durationType = motorOnAction.getParam().getDuration().getType().toString().toLowerCase();
            motorOnAction.getParam().getDuration().getValue().accept(this);
            o.put(C.MOTOR_DURATION, durationType);
            this.app(o);
            return this.app(this.mk(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        } else {
            this.app(this.mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
            return this.app(o);
        }
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        final String port = motorSetPowerAction.getUserDefinedPort();
        motorSetPowerAction.getPower().accept(this);
        final JSONObject o = this.mk(C.MOTOR_SET_POWER).put(C.PORT, port.toLowerCase());
        return this.app(o);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        final String port = motorStopAction.getUserDefinedPort();
        final JSONObject o = this.mk(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return this.app(o);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        showTextAction.getY().accept(this);
        showTextAction.getX().accept(this);
        showTextAction.getMsg().accept(this);
        final JSONObject o = this.mk(C.SHOW_TEXT_ACTION).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        final String mode = lightAction.getMode().toString().toLowerCase();
        final String color = lightAction.getColor().toString().toLowerCase();
        final JSONObject o = this.mk(C.LIGHT_ACTION).put(C.MODE, mode).put(C.COLOR, color);
        return this.app(o);
    }

    @Override
    public V visitTouchSensor(TouchSensor<V> touchSensor) {
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitColorSensor(ColorSensor<V> colorSensor) {
        final String mode = colorSensor.getMode();
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
        final String mode = encoderSensor.getMode().toLowerCase();
        final String port = encoderSensor.getPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = this.mk(C.ENCODER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ENCODER_SENSOR_SAMPLE).put(C.PORT, port).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return this.app(o);
    }

    @Override
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
        final String mode = temperatureSensor.getMode();
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TEMPERATURE).put(C.PORT, mode.toLowerCase()).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        final String mode = keysSensor.getPort().toLowerCase();
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, mode).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.LIGHT).put(C.PORT, C.AMBIENTLIGHT).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        final String port = timerSensor.getPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = this.mk(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        }
        return this.app(o);
    }

    @Override
    public V visitPinTouchSensor(PinTouchSensor<V> sensorGetSample) {
        final String port = sensorGetSample.getPort();
        final String mode = sensorGetSample.getMode();

        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitSoundSensor(SoundSensor<V> soundSensor) {
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitCompassSensor(CompassSensor<V> compassSensor) {
        final String mode = compassSensor.getMode();
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COMPASS).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return this.app(o);
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        final String mode = gyroSensor.getMode().toLowerCase();
        final String port = gyroSensor.getPort().toLowerCase();
        JSONObject o;
        if ( mode.equals(C.RESET) ) {
            o = this.mk(C.GYRO_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "ev3");
        } else {
            o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.MODE, mode).put(C.NAME, "ev3");
        }
        return this.app(o);
    }

    @Override
    public V visitAccelerometer(AccelerometerSensor<V> accelerometerSensor) {
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
        final String mode = ultrasonicSensor.getMode();
        final JSONObject o = this.mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.MODE, mode.toLowerCase()).put(C.NAME, "ev3");
        return this.app(o);
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de-DE";
            case ENGLISH:
                return "en-US";
            case FRENCH:
                return "fr-FR";
            case SPANISH:
                return "es-ES";
            case ITALIAN:
                return "it-IT";
            case DUTCH:
                return "nl-NL";
            case POLISH:
                return "pl-PL";
            case RUSSIAN:
                return "ru-RU";
            case PORTUGUESE:
                return "pt-BR";
            case JAPANESE:
                return "ja-JP";
            case CHINESE:
                return "zh-CN";
            default:
                return "";
        }
    }

    @Override
    protected void appendDuration(MotorDuration<V> duration) {
        if ( duration != null ) {
            duration.getValue().accept(this);
        } else {
            this.app(this.mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 0));
        }
    }

    @Override
    public V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction) {
        return null;
    }

    @Override
    public V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction) {
        return null;
    }

    @Override
    public V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction) {
        return null;
    }

    @Override
    public V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction) {
        return null;
    }

}
