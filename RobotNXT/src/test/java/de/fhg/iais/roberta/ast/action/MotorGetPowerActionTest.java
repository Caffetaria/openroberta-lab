package de.fhg.iais.roberta.ast.action;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorGetPowerActionTest extends NxtAstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-78, y=63], MotorGetPower [port=B]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorGetPower.xml");
    }

    @Test
    public void getPort() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> forest = UnitTestHelper.getAst(testFactory, "/ast/actions/action_MotorGetPower.xml");
        MotorGetPowerAction<Void> mgp = (MotorGetPowerAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("B", mgp.getUserDefinedPort());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorGetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorGetPower1.xml");
    }

}