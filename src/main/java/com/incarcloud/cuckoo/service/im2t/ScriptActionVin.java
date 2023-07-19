package com.incarcloud.cuckoo.service.im2t;

public class ScriptActionVin extends ScriptAction{
    private final String vin;

    public ScriptActionVin(int sec, String args){
        super(sec);
        args = parseComment(args);
        this.vin = args;
    }

    public String getVin(){
        return vin;
    }
}
