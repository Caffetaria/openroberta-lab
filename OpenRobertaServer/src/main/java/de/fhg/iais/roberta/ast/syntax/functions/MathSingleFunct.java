package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>math_trig</b>, <b>math_round</b> and <b>math_single</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(Functions, List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Functions} contains all allowed functions.
 */
public class MathSingleFunct<V> extends Function<V> {
    private final Functions functName;
    private final List<Expr<V>> param;

    private MathSingleFunct(Functions name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.MATH_SINGLE_FUNCT, properties, comment);
        Assert.isTrue(name != null && param != null);
        this.functName = name;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathSingleFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function,
     * @param param list of parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathSingleFunct}
     */
    public static <V> MathSingleFunct<V> make(Functions name, List<Expr<V>> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathSingleFunct<V>(name, param, properties, comment);
    }

    /**
     * @return name of the function
     */
    public Functions getFunctName() {
        return this.functName;
    }

    /**
     * @return list of parameters for the function
     */
    public List<Expr<V>> getParam() {
        return this.param;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMathSingleFunct(this);
    }

    @Override
    public String toString() {
        return "MathSingleFunct [" + this.functName + ", " + this.param + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, "OP", getFunctName().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, "NUM", getParam().get(0));
        return jaxbDestination;
    }

}
