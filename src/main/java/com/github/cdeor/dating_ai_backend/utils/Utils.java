package com.github.cdeor.dating_ai_backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static <T> T getRandomElement(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static List<String> generatePersonalityTypes() {
        List<String> myersBriggsPersonalityTypes = new ArrayList<>();

        String[] dimensions = {
                "E,I", // Extraversion or Introversion
                "S,N", // Sensing or Intuition
                "T,F", // Thinking or Feeling
                "J,P"  // Judging or Perceiving
        };

        // Generate all combinations
        for (String e : dimensions[0].split(",")) {
            for (String s : dimensions[1].split(",")) {
                for (String t : dimensions[2].split(",")) {
                    for (String j : dimensions[3].split(",")) {
                        myersBriggsPersonalityTypes.add(e + s + t + j);
                    }
                }
            }
        }

        return myersBriggsPersonalityTypes;
    }
}
