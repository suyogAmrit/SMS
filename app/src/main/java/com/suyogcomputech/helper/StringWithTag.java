package com.suyogcomputech.helper;

/**
 * Created by Pintu on 9/2/2016.
 */
public class StringWithTag {
    public String string;
    public Object tag;

    public StringWithTag(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}
