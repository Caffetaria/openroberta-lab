package de.fhg.iais.roberta.syntax.check.hardware.edison;

import java.util.ArrayList;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;

public class UsedHardwareCollectorVisitorTest extends AstTest {

    @Test
    public void TestAllHelperMethods() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = UnitTestHelper.getAst(testFactory, "/collector/all_helper_methods.xml");
        ConfigurationAst edisonConfig = HelperEdisonForXmlTest.makeConfig();
        UsedHardwareBean.Builder builder = new Builder();
        EdisonUsedHardwareCollectorVisitor checker = new EdisonUsedHardwareCollectorVisitor(builder, phrases, edisonConfig);
    }
}