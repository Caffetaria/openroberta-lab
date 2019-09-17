package de.fhg.iais.roberta.syntax.check.hardware.nao;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;
import de.fhg.iais.roberta.visitor.collect.NaoUsedHardwareCollectorVisitor;

public class UsedHardwareCollectorVisitorTest {
    HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    private ConfigurationAst makeConfiguration() {
        return new ConfigurationAst.Builder().build();
    }

    @Test
    public void testLearnFace_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/learnface.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_FACE, null]]", bean.getUsedSensors().toString());

    }

    @Test
    public void testForgetFace_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/forgetface.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_FACE, null]]", bean.getUsedSensors().toString());

    }

    @Test
    public void testGetNaoMarkInfo_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/getnaomarkinfo.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, DETECT_MARK, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testDetectNaoMark_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/detectnaomark.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, DETECT_MARK, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testGetRecogniezdWordFromList_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/getrecognizedwordfromlist.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [null, NAO_SPEECH, null]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testAllMoveBlocks_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/moveblocks.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkDistance_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/walkdistance.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testTurnAction_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/turnaction.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToX_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/walktoXYTheta1.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToY_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/walktoXYTheta2.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());

        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }

    @Test
    public void testWalkToTheta_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/walktoXYTheta3.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        NaoUsedHardwareCollectorVisitor checkVisitor = new NaoUsedHardwareCollectorVisitor(builder, phrases, makeConfiguration());
        for ( ArrayList<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.visit(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[UsedSensor [NO_PORT, ULTRASONIC, DISTANCE]]", bean.getUsedSensors().toString());
    }
}
