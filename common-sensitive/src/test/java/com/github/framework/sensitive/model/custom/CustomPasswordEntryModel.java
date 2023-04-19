package com.github.framework.sensitive.model.custom;

import com.github.framework.sensitive.annotation.SensitiveEntry;


public class CustomPasswordEntryModel {

    @SensitiveEntry
    private CustomPasswordModel entry;

    public CustomPasswordModel getEntry() {
        return entry;
    }

    public void setEntry(final CustomPasswordModel entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "CustomPasswordEntryModel{" + "entry=" + entry + '}';
    }

}
