package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.ActorPort;
import de.fhg.iais.roberta.mode.sensor.nao.Part;
import de.fhg.iais.roberta.mode.sensor.nao.Side;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class SensorTouched<V> extends Action<V> {

    private final Part part;
    private final Side side;

    private SensorTouched(Part part, Side side, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SENSOR_TOUCHED"), properties, comment);
        Assert.notNull(part, "Missing sensor in SensorTouched block!");
        Assert.notNull(side, "Missing side in SensorTouched block!");
        this.part = part;
        this.side = side;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SensorTouched}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SensorTouched}
     */
    private static <V> SensorTouched<V> make(Part part, Side side, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SensorTouched<V>(part, side, properties, comment);
    }

    public Part getPart() {
        return this.part;
    }

    public Side getSide() {
        return this.side;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitSensorTouched(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String part = helper.extractField(fields, BlocklyConstants.PART);
        String side = helper.extractField(fields, BlocklyConstants.SIDE);

        return SensorTouched.make(Part.get(part), Side.get(side), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PART, this.part.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SIDE, this.side.toString());

        return jaxbDestination;
    }
}