package de.fhg.iais.roberta.syntax.check.hardware.edison;

import java.util.ArrayList;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;

public class UsedHardwareCollectorVisitorTest {
    private final HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    @Test
    public void TestAllHelperMethods() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/collector/all_helper_methods.xml");
        ConfigurationAst edisonConfig = HelperEdisonForXmlTest.makeConfig();

        EdisonUsedHardwareCollectorVisitor checker = new EdisonUsedHardwareCollectorVisitor(phrases, edisonConfig);
    }
}