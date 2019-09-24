package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathOnListsTest extends AstTest {

    @Test
    public void mathOnListSum() throws Exception {
        String a = "BlocklyMethods.sumOnList(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_sum.xml");
    }

    @Test
    public void mathOnListMin() throws Exception {
        String a = "Collections.min(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_min.xml");
    }

    @Test
    public void mathOnListMax() throws Exception {
        String a = "Collections.max(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_max.xml");
    }

    @Test
    public void mathOnListAverage() throws Exception {
        String a = "BlocklyMethods.averageOnList(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_average.xml");
    }

    @Test
    public void mathOnListMedian() throws Exception {
        String a = "BlocklyMethods.medianOnList(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_median.xml");
    }

    @Test
    public void mathOnListStandardDeviatioin() throws Exception {
        String a = "BlocklyMethods.standardDeviatioin(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_std_dev.xml");
    }

    @Test
    public void mathOnListRandom() throws Exception {
        String a = "BlocklyMethods.randOnList(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_random.xml");
    }

    @Test
    public void mathOnListMode() throws Exception {
        String a = "BlocklyMethods.modeOnList(newArrayList<>(Arrays.asList((float) 5, (float) 3, (float) 2)))}";

        UnitTestHelper.checkGeneratedSourceEqualityWithSourceAsString(testFactory, a, "/syntax/math/math_on_list_mode.xml");
    }

}
