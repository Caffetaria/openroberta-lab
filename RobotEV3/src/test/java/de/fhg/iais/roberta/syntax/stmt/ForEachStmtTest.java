package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ForEachStmtTest extends AstTest {

    @Test
    public void forEachStmt() throws Exception {
        String a =
            "\nArrayList<PickColor>variablenName=newArrayList<>(Arrays.<PickColor>asList(PickColor.NONE,PickColor.RED,PickColor.BLUE));publicvoidrun()throwsException{for(PickColorvariablenName2:variablenName){hal.drawText(String.valueOf(variablenName2),0,0);}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/stmt/forEach_stmt.xml");
    }

}