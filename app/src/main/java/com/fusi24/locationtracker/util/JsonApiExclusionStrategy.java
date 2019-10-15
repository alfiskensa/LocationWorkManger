package com.fusi24.locationtracker.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonApiExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (f.getName().equals("links")){
            return true;
        }

        if (f.getName().equals("meta")){
            return true;
        }

        return f.getName().equals("document");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
