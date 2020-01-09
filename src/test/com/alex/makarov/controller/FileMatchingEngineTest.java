package alex.makarov.controller;

import com.alex.makarov.ApplicationConfiguration;
import com.alex.makarov.controller.matching.MatchingEngine;
import com.alex.makarov.dataobjects.MatchingResult;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ApplicationConfiguration.class)
public class FileMatchingEngineTest {

    private static final String X_SET_PATH = "xset.txt";
    private static final String Y_SET_PATH = "yset.txt";

    @Autowired
    private MatchingEngine matchingEngine;

    private static final Set<String> EXPECTED_EXACT_MATCH = new HashSet<String>(){{ add("x0y0"); }};

    private static final Set<String> EXPECTED_WEAK_MATCH = new HashSet<String>(){{
        add("x1y1"); add("x2y2"); add("x3y3"); add("x6y6");
    }};

    private static final Set<String> EXPECTED_X_BREAKS = new HashSet<String>(){{
        add("x4"); add("x5"); add("x7");
    }};

    private static final Set<String> EXPECTED_Y_BREAKS = new HashSet<String>(){{
        add("y4"); add("y5"); add("y7");
    }};

    @Test
    public void matchingTransactionsTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File xDataSet = new File(classLoader.getResource(X_SET_PATH).getFile());
        File yDataSet = new File(classLoader.getResource(Y_SET_PATH).getFile());

        MatchingResult matchingResult = matchingEngine.match(xDataSet.getAbsolutePath(), yDataSet.getAbsolutePath());

        assertEquals(EXPECTED_WEAK_MATCH.size(), matchingResult.getWeakMatches().size());
        for (String tId : EXPECTED_WEAK_MATCH) {
            assertNotNull(matchingResult.getWeakMatches().contains(tId));
        }

        assertEquals(EXPECTED_EXACT_MATCH.size(), matchingResult.getExactMatches().size());
        for (String tId : EXPECTED_EXACT_MATCH) {
            assertNotNull(matchingResult.getExactMatches().contains(tId));
        }

        /*
            Disclaimer:
            I spent around 6 hours to complete the task and would really appreciate feedback in exchange for a pint of a beer.
            If you are reading this, please write a constructive feedback and feel free to claim a beer:)
         */

        assertEquals(EXPECTED_X_BREAKS.size(), matchingResult.getSideABreaks().size());
        for (String tId : EXPECTED_X_BREAKS) {
            assertNotNull(matchingResult.getSideABreaks().contains(tId));
        }

        assertEquals(EXPECTED_Y_BREAKS.size(), matchingResult.getSideBBreaks().size());
        for (String tId : EXPECTED_Y_BREAKS) {
            assertNotNull(matchingResult.getSideBBreaks().contains(tId));
        }

    }

}
