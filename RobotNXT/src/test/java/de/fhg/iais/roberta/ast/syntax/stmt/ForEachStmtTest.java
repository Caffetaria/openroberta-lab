package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore
public class ForEachStmtTest extends AstTest {

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<Pickcolor>variablenName=BlocklyMethods.createListWithColour(Pickcolor.NONE,Pickcolor.RED,Pickcolor.BLUE);publicvoidrun(){for(PickcolorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/stmt/forEach_stmt.xml");
    }

}