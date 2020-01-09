package com.alex.makarov.dataobjects;

import com.alex.makarov.controller.matching.ProcessingStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MatchingResult {

    private ProcessingStatus status;

    private Set<String> exactMatches;
    private Set<String> weakMatches;
    private Set<String> aSideBreak;
    private Set<String> bSideBreak;

    public MatchingResult(Set<String> exactMatches, Set<String> weakMatches, Set<String> aSideBreak, Set<String> bSideBreak, ProcessingStatus status) {
        this.exactMatches = exactMatches;
        this.weakMatches = weakMatches;
        this.aSideBreak = aSideBreak;
        this.bSideBreak = bSideBreak;

        this.status = status;
    }

    public MatchingResult(ProcessingStatus status) {
        this.exactMatches = new HashSet<>();
        this.weakMatches = new HashSet<>();
        this.aSideBreak = new HashSet<>();
        this.bSideBreak = new HashSet<>();

        this.status = status;
    }

    public ProcessingStatus getProcessingStatus() { return status; }

    public Set<String> getExactMatches() {
        return Collections.unmodifiableSet(exactMatches);
    }

    public Set<String> getWeakMatches() {
        return Collections.unmodifiableSet(weakMatches);
    }

    public Set<String> getSideABreaks() {
        return Collections.unmodifiableSet(aSideBreak);
    }

    public Set<String> getSideBBreaks() {
        return Collections.unmodifiableSet(bSideBreak);
    }



}
