package com.queststore.Model;

public class ClassInfo {

    private Integer id;
    private Class klass;
    private Integer mentorsCount;
    private Integer studentsCount;
    private boolean isActive;

    public ClassInfo(Integer id, Class klass, Integer mentorsCount, Integer studentsCount, boolean isActive) {
        this.id = id;
        this.klass = klass;
        this.mentorsCount = mentorsCount;
        this.studentsCount = studentsCount;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
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

    public boolean getIsActive() {
        return isActive;
    }
}
