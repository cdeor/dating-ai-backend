package com.github.cdeor.dating_ai_backend.profiles;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dating.ai.searchCriteria")
public class SearchCriteria {
    private String lookingForGender;
    private int startAge;
    private int endAge;

    public String getLookingForGender() {
        return lookingForGender;
    }

    public void setLookingForGender(String lookingForGender) {
        this.lookingForGender = lookingForGender;
    }

    public int getStartAge() {
        return startAge;
    }

    public void setStartAge(int startAge) {
        this.startAge = startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }
}
