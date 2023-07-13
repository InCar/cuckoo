package com.incarcloud.cuckoo.service.im2t;

public class ScriptActionTimeCompress extends ScriptAction{
    private float fCompressRatio = 1.0f;

    public ScriptActionTimeCompress(int sec, String args){
        super(sec);
        args = parseComment(args);
        this.fCompressRatio = Float.parseFloat(args);
    }

    public float getCompressRatio(){
        return fCompressRatio;
    }
}
