package com.incarcloud.cuckoo.service.im2t;

import java.util.regex.Pattern;

public class ScriptActionSpeed extends ScriptAction {
    private final static Pattern rgxSpeed = Pattern.compile("(\\d+\\.\\d+)km/h", Pattern.CASE_INSENSITIVE);
    private float fSpeed = 0.0f;

    public ScriptActionSpeed(int sec, String args){
        super(sec);
        args = parseComment(args);
        var matcher = rgxSpeed.matcher(args);
        if(matcher.find()){
            fSpeed = Float.parseFloat(matcher.group(1));
        }
        else{
            throw new RuntimeException("Invalid script: " + args);
        }
    }

    public float getSpeed(){
        return fSpeed;
    }
}
