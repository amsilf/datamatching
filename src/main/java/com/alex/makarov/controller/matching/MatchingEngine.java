package com.alex.makarov.controller.matching;

import com.alex.makarov.dataobjects.MatchingResult;

public interface MatchingEngine {

    MatchingResult match(String aPath, String bPath);

}
