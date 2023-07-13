package com.incarcloud.cuckoo.service.im2t;

public class ScriptActionFinish extends ScriptAction{

    public ScriptActionFinish(int sec, String args){
        super(sec);
        args = parseComment(args);
    }
}
