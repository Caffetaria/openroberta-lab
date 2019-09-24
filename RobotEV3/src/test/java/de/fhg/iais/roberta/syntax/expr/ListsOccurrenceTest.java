package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsOccurrenceTest extends AstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).indexOf((float)30));hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).lastIndexOf((float)30));}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence.xml");
    }

}
