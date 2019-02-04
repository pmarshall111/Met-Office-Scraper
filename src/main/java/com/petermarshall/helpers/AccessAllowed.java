package com.petermarshall.helpers;

import java.util.HashSet;

public class AccessAllowed {
    private final HashSet<String> allowedClasses;
    private String creatorClass;

    public AccessAllowed(Class... allowedClasses) {
        this.allowedClasses = new HashSet<>();
        for (Class c: allowedClasses) {
            this.allowedClasses.add(c.getName());
        }
        addCreatorClass();
    }
    public AccessAllowed(Class allowedClass) {
        this.allowedClasses = new HashSet<>();
        this.allowedClasses.add(allowedClass.getName());
        addCreatorClass();
    }

    private void addCreatorClass() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String currClass = this.getClass().getName();

        for (int i = 1; i<stElements.length; i++) {
            String stackClass = stElements[i].getClassName();
            if (!stackClass.equals(currClass)) {
                this.creatorClass = stackClass;
                return;
            }
        }
    }

    public boolean callerCanUseMethod() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String currClass = this.getClass().getName();

        for (int i = 1; i<stElements.length; i++) {
            String stackClass = stElements[i].getClassName();

            if (!stackClass.equals(currClass) && !stackClass.equals(creatorClass)) {
                return this.allowedClasses.contains(stackClass);
            }
        }

        return false;
    }
}
