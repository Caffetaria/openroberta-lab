package de.fhg.iais.roberta.syntax.lang.expr.eval.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

public class ExprlyUnParser<T> {
    private String se;
    private final Phrase<T> e;
    private final static Map<FunctionNames, String> fnames;
    private final static Map<MathConst.Const, String> cnames;
    private final static Map<Binary.Op, String> bnames;
    private final static Map<Unary.Op, String> unames;

    /**
     * Class constructor, creates an instance of {@link ExprlyUnParser} for
     * the phrase pased as parameter
     *
     * @param Phrase that will be UnParsed
     **/
    public ExprlyUnParser(Phrase<T> ast) {
        this.se = "";
        this.e = ast;
    }

    /**
     * @return textual representation of expression
     */
    public String getString() {
        return this.se;
    }

    /**
     * Method to UnParse the expression in the class
     * Note: The expression will have redundant parenthesis
     * Note: It only works reliably on semantically correct expressions,
     * wrong expressions could lead to unwanted results
     *
     * @return UnParsed expression
     */
    public String UnParse() {
        this.se = "";
        this.se += visitAST(this.e);
        return getString();
    }

    /**
     * @param NumberConst Expression
     * @return Textual representation of the NumberConst
     */
    public String visitNumConst(NumConst<T> numConst) {
        return numConst.getValue();
    }

    /**
     * @param MathConst Expression
     * @return Textual representation of the MathConst
     */
    public String visitMathConst(MathConst<T> mathConst) {
        return ExprlyUnParser.cnames.get(mathConst.getMathConst());
    }

    /**
     * @param BoolConst Expression
     * @return Textual representation of the BoolConst
     */
    public String visitBoolConst(BoolConst<T> boolConst) {
        return Boolean.toString(boolConst.getValue());
    }

    /**
     * @param StringConst Expression
     * @return Textual representation of the StringConst
     */
    public String visitStringConst(StringConst<T> stringConst) {
        return "\"" + stringConst.getValue() + "\"";
    }

    /**
     * @param ColorConst Expression
     * @return Textual representation of the ColorConst
     */
    public String visitColorConst(ColorConst<T> colorConst) {
        return colorConst.getHexValueAsString();
    }

    /**
     * @param RgbColor Expression
     * @return Textual representation of the RGB Color
     */
    public String visitRgbColor(RgbColor<T> rgbColor) {
        return "(" + visitAST(rgbColor.getR()) + "," + visitAST(rgbColor.getG()) + "," + visitAST(rgbColor.getB()) + "," + visitAST(rgbColor.getA()) + ")";
    }

    /**
     * @param ConnectConst Expression
     * @return Textual representation of the ConnectConst
     */
    public String visitConnectConst(ConnectConst<T> connectConst) {

        return "connect " + connectConst.getDataValue() + "," + connectConst.getValue();
    }

    /**
     * @param Var Expression
     * @return Textual representation of The Var value
     */
    public String visitVar(Var<T> var) {

        return var.getValue();
    }

    /**
     * Method to UnParse Unary expressions
     *
     * @param Unary Expression
     * @return Textual representation of the operation
     */
    public String visitUnary(Unary<T> unary) throws UnsupportedOperationException {
        return ExprlyUnParser.unames.get(unary.getOp()) + "(" + visitAST(unary.getExpr()) + ")";
    }

    /**
     * Method to UnParse Binary expressions
     *
     * @param Binary Expression
     * @return Textual representation of the operation
     */
    public String visitBinary(Binary<T> binary) throws UnsupportedOperationException {
        return "(" + visitAST(binary.getLeft()) + ")" + ExprlyUnParser.bnames.get(binary.getOp()) + "(" + visitAST(binary.getRight()) + ")";
    }

    /**
     * Method to UnParse List expressions
     *
     * @param List Expression
     * @return Textual representation of List
     */
    public String visitExprList(ExprList<T> list) {
        // Get list of expressions in the list
        List<Expr<T>> eList = list.get();
        String t = "[";
        for ( Expr<T> e : eList ) {
            t = t + visitAST(e) + ",";
        }
        if ( t.length() == 1 ) {
            return "[]";
        }
        return t.substring(0, t.length() - 1) + "]";
    }

    /**
     * @param Function Expression
     * @return Textual representation of function
     */
    public String visitFunctionExpr(FunctionExpr<T> funct) {
        return visitAST(funct.getFunction());
    }

    /**
     * Method to UnParse MathNumPropFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public String visitMathNumPropFunct(MathNumPropFunct<T> mathNumPropFunct) {
        return ExprlyUnParser.fnames.get(mathNumPropFunct.getFunctName()) + "(" + visitAST(mathNumPropFunct.getParam().get(0)) + ")";

    }

    /**
     * Method to UnParse MathOnListFunctions.
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathOnListFunct(MathOnListFunct<T> mathOnListFunct) {
        return ExprlyUnParser.fnames.get(mathOnListFunct.getFunctName()) + "(" + visitAST(mathOnListFunct.getParam().get(0)) + ")";
    }

    /**
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathRandomFloatFunct(MathRandomFloatFunct<T> mathRandomFloatFunct) {
        return "randFloat()";
    }

    /**
     * Method to UnParse MathRandomIntFunctions
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathRandomIntFunct(MathRandomIntFunct<T> mathRandomIntFunct) {
        // Get arguments
        List<Expr<T>> args = mathRandomIntFunct.getParam();
        return "randInt" + "(" + visitAST(args.get(0)) + "," + visitAST(args.get(1)) + ")";
    }

    /**
     * Method to UnParse MathSingleFunctions
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathSingleFunct(MathSingleFunct<T> mathSingleFunct) {
        // Get parameters
        List<Expr<T>> args = mathSingleFunct.getParam();
        String fname = ExprlyUnParser.fnames.get(mathSingleFunct.getFunctName());
        if ( fname.equals("pow") ) {
            return "(" + visitAST(args.get(0)) + ")" + "^" + "(" + visitAST(args.get(1)) + ")";
        } else {
            fname += "(";
            for ( int i = 0; i < args.size(); i++ ) {
                fname += visitAST(args.get(i));
                if ( i != args.size() - 1 ) {
                    fname += ",";
                }
            }
            fname += ")";
            return fname;
        }
    }

    /**
     * Method to UnParse any expression
     *
     * @param ast, expression to unparse
     * @return Textual representation of the AST
     */
    public String visitAST(Phrase<T> ast) throws UnsupportedOperationException {
        if ( ast instanceof Binary<?> ) {
            return visitBinary((Binary<T>) ast);
        }
        if ( ast instanceof Unary<?> ) {
            return visitUnary((Unary<T>) ast);
        }
        if ( ast instanceof MathConst<?> ) {
            return visitMathConst((MathConst<T>) ast);
        }
        if ( ast instanceof NumConst<?> ) {
            return visitNumConst((NumConst<T>) ast);
        }
        if ( ast instanceof BoolConst<?> ) {
            return visitBoolConst((BoolConst<T>) ast);
        }
        if ( ast instanceof StringConst<?> ) {
            return visitStringConst((StringConst<T>) ast);
        }
        if ( ast instanceof ColorConst<?> ) {
            return visitColorConst((ColorConst<T>) ast);
        }
        if ( ast instanceof RgbColor<?> ) {
            return visitRgbColor((RgbColor<T>) ast);
        }
        if ( ast instanceof ConnectConst<?> ) {
            return visitConnectConst((ConnectConst<T>) ast);
        }
        if ( ast instanceof Var<?> ) {
            return visitVar((Var<T>) ast);
        }
        if ( ast instanceof ExprList<?> ) {
            return visitExprList((ExprList<T>) ast);
        }
        if ( ast instanceof FunctionExpr<?> ) {
            return visitFunctionExpr((FunctionExpr<T>) ast);
        }
        if ( ast instanceof MathNumPropFunct<?> ) {
            return visitMathNumPropFunct((MathNumPropFunct<T>) ast);
        }
        if ( ast instanceof MathOnListFunct<?> ) {
            return visitMathOnListFunct((MathOnListFunct<T>) ast);
        }
        if ( ast instanceof MathRandomFloatFunct<?> ) {
            return visitMathRandomFloatFunct((MathRandomFloatFunct<T>) ast);
        }
        if ( ast instanceof MathRandomIntFunct<?> ) {
            return visitMathRandomIntFunct((MathRandomIntFunct<T>) ast);
        }
        if ( ast instanceof MathSingleFunct<?> ) {
            return visitMathSingleFunct((MathSingleFunct<T>) ast);
        }
        throw new UnsupportedOperationException("Expression " + ast.toString() + "cannot be checked");
    }

    // Fill the HashMap of Function Names
    static {
        HashMap<FunctionNames, String> names = new HashMap<FunctionNames, String>();
        names.put(FunctionNames.MAX, "max");
        names.put(FunctionNames.MIN, "min");
        names.put(FunctionNames.EVEN, "isEven");
        names.put(FunctionNames.ODD, "isOdd");
        names.put(FunctionNames.PRIME, "isPrime");
        names.put(FunctionNames.WHOLE, "isWhole");
        names.put(FunctionNames.SUM, "sum");
        names.put(FunctionNames.AVERAGE, "avg");
        names.put(FunctionNames.MEDIAN, "median");
        names.put(FunctionNames.STD_DEV, "sd");
        names.put(FunctionNames.ROOT, "sqrt");
        names.put(FunctionNames.ABS, "abs");
        names.put(FunctionNames.LN, "ln");
        names.put(FunctionNames.LOG10, "log10");
        names.put(FunctionNames.EXP, "exp");
        names.put(FunctionNames.SIN, "sin");
        names.put(FunctionNames.COS, "cos");
        names.put(FunctionNames.TAN, "tan");
        names.put(FunctionNames.ASIN, "asin");
        names.put(FunctionNames.ACOS, "acos");
        names.put(FunctionNames.ATAN, "atan");
        names.put(FunctionNames.POWER, "pow");
        names.put(FunctionNames.ROUNDUP, "ceil");
        names.put(FunctionNames.ROUNDDOWN, "floor");
        fnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Math Const Names
    static {
        HashMap<MathConst.Const, String> names = new HashMap<MathConst.Const, String>();
        names.put(MathConst.Const.GOLDEN_RATIO, "phi");
        names.put(MathConst.Const.PI, "pi");
        names.put(MathConst.Const.E, "e");
        names.put(MathConst.Const.SQRT2, "sqrt2");
        names.put(MathConst.Const.SQRT1_2, "sqrt_1_2");
        names.put(MathConst.Const.INFINITY, "inf");
        cnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Binary Operation Names
    static {
        HashMap<Binary.Op, String> names = new HashMap<Binary.Op, String>();
        names.put(Binary.Op.ADD, "+");
        names.put(Binary.Op.MINUS, "-");
        names.put(Binary.Op.MULTIPLY, "*");
        names.put(Binary.Op.DIVIDE, "/");
        names.put(Binary.Op.MOD, "%");
        names.put(Binary.Op.AND, "&&");
        names.put(Binary.Op.OR, "||");
        names.put(Binary.Op.EQ, "==");
        names.put(Binary.Op.NEQ, "!=");
        names.put(Binary.Op.GT, ">");
        names.put(Binary.Op.LT, "<");
        names.put(Binary.Op.GTE, ">=");
        names.put(Binary.Op.LTE, "<=");
        bnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Unary Operation Names
    static {
        HashMap<Unary.Op, String> names = new HashMap<Unary.Op, String>();
        names.put(Unary.Op.PLUS, "+");
        names.put(Unary.Op.NEG, "-");
        names.put(Unary.Op.NOT, "!");
        unames = Collections.unmodifiableMap(names);
    }
}
