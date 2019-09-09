package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.transformer.CodeGeneratorSetupBean;

/**
 * A visitor that keeps track of all methods visited at any point in the AST, that need an additional helper method definition.
 */
public class UsedMethodCollectorVisitor implements ICollectorVisitor {

    CodeGeneratorSetupBean.Builder builder;

    public UsedMethodCollectorVisitor(CodeGeneratorSetupBean.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        this.builder.addUsedFunction(mathNumPropFunct.getFunctName());
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        this.builder.addUsedFunction(mathOnListFunct.getFunctName());
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        if ( mathSingleFunct.getFunctName() == FunctionNames.POW10 ) {
            this.builder.addUsedFunction(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            this.builder.addUsedFunction(mathSingleFunct.getFunctName());
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.builder.addUsedFunction(FunctionNames.LISTS_REPEAT);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.builder.addUsedFunction(FunctionNames.POWER);
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.builder.addUsedFunction(FunctionNames.RANDOM);
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.builder.addUsedFunction(FunctionNames.RANDOM_DOUBLE);
        return null;
    }
}
