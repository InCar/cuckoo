package com.incarcloud.cuckoo.service.im2t;

public abstract class ScriptAction {
    private final int sec;

    public ScriptAction(int sec){
        this.sec = sec;
    }

    public int getSec(){
        return sec;
    }
}
