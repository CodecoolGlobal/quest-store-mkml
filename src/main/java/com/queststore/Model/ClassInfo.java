package com.queststore.Model;

public class ClassInfo {

    private Class klass;
    private Integer mentorsCount;
    private Integer studentsCount;

    public ClassInfo(Class klass, Integer mentorsCount, Integer studentsCount) {
        this.klass = klass;
        this.mentorsCount = mentorsCount;
        this.studentsCount = studentsCount;
    }

    public Class getKlass() {
        return klass;
    }

    public Integer getMentorsCount() {
        return mentorsCount;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }
}
