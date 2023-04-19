package com.github.framework.sensitive.model.sensitive.system;

import com.github.framework.sensitive.annotation.SensitiveEntry;

/**
 * 系统内置注解-对象
 */
public class SystemBuiltInAtEntry {

    @SensitiveEntry
    private SystemBuiltInAt entry;

    public SystemBuiltInAt getEntry() {
        return entry;
    }

    public void setEntry(final SystemBuiltInAt entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "SystemBuiltInAtEntry{" + "entry=" + entry + '}';
    }

}
