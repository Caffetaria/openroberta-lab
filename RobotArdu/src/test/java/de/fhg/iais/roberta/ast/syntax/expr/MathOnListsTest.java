package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathOnListsTest extends AstTest {

    @Test
    public void mathOnListSum() throws Exception {
        final String a = "_getListSum({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_sum.xml");
    }

    @Test
    public void mathOnListMin() throws Exception {
        final String a = "_getListMin({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_min.xml");
    }

    @Test
    public void mathOnListMax() throws Exception {
        final String a = "_getListMax({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_max.xml");
    }

    @Test
    public void mathOnListAverage() throws Exception {
        final String a = "_getListAverage({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_average.xml");
    }

    @Test
    public void mathOnListMedian() throws Exception {
        final String a = "_getListMedian({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_median.xml");
    }

    @Test
    public void mathOnListStandardDeviation() throws Exception {
        final String a = "_getListStandardDeviation({5,3,2})";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_std_dev.xml");
    }

    @Test
    @Ignore
    public void mathOnListRandom() throws Exception {
        final String a = "null";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_random.xml");
    }

    @Test
    @Ignore
    public void mathOnListMode() throws Exception {
        final String a = "null";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_mode.xml");
    }

}