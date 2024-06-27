package dev.impl;

import java.util.List;


public class SoyFunctions {
    public static boolean includes(List list, Object item) {
        if(list == null || list.isEmpty()){
            return false;
        }

        return list.contains(item);
    }
}
