package com.alex.makarov.controller.requesthanders;

import com.alex.makarov.controller.matching.MatchingEngine;
import com.alex.makarov.controller.matching.ProcessingStatus;
import com.alex.makarov.dataobjects.MatchingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.alex.makarov.controller.matching.ProcessingStatus.*;

@RestController
public class MatchingController {

    @Autowired
    private MatchingEngine matchingEngine;

    // it make sense to make an enum out of it if number of actions will grow further
    private static final String MATCH_ACTION = "match";

    /*
     * According to https://hackernoon.com/restful-api-designing-guidelines-the-best-practices-60e1d954e7c9
     * My favorite resource regarding REST api design
     */
    @RequestMapping("/data/files")
    public MatchingResult matchTwoFile(@RequestParam String action, @RequestParam String aResourceUri, @RequestParam String bResourceUri) {
        if (action.equals(MATCH_ACTION)) {
            return matchingEngine.match(aResourceUri, bResourceUri);
        }

        return new MatchingResult(ACTION_DOESNOT_EXISTS);
    }

}
