package dev.impl;

import com.atlassian.annotations.PublicSpi;

import java.util.List;

@PublicSpi
public class SoyFunctions {
    public static boolean includes(List list, Object item) {
        if(list == null || list.isEmpty()){
            return false;
        }

        return list.contains(item);
    }
}
