package org.example.junmin.util;

import java.util.*;
import java.util.stream.Collectors;

public class KeywordExtractor {

    public static List<String> topKeywords(List<String> words, int topN) {

        Map<String, Long> freqMap = words.stream()
                .filter(w -> w != null && w.trim().length() > 1) // 过滤单字噪声
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        return freqMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
