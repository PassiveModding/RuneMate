package com.passive.api.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class JavaFixes {
    public static String EnsureStringIsNotNull(String text) {
        if (text == null) {
            text = "";
        }

        return text;
    }

    public static <T> Set<T> findDuplicates(Collection<T> collection) {

        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for(T t : collection) {
            if(!uniques.add(t)) {
                duplicates.add(t);
            }
        }

        return duplicates;
    }
}
