package com.incarcloud.cuckoo.service.im2t;

public class ScriptActionCourse extends ScriptAction{
    private float fCourse = 0.0f;

    public ScriptActionCourse(int sec, String args){
        super(sec);
        args = parseComment(args);
        fCourse = Float.parseFloat(args);
    }

    public float getCourse(){
        return fCourse;
    }
}
