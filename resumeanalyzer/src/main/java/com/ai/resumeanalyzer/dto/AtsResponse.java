package com.ai.resumeanalyzer.dto;

import java.util.List;

public class AtsResponse {

    private double atsScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public AtsResponse() {}

    public AtsResponse(double atsScore, List<String> matchedSkills, List<String> missingSkills) {
        this.atsScore = atsScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    public double getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(double atsScore) {
        this.atsScore = atsScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }
}