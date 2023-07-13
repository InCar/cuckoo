package com.incarcloud.cuckoo.service.im2t;

import java.util.regex.Pattern;

public abstract class ScriptAction {
    private final static Pattern rgxComment = Pattern.compile("\\s*//(.*)", Pattern.CASE_INSENSITIVE);
    private int sec;
    private String comment = "";

    public ScriptAction(int sec){
        this.sec = sec;
    }

    public int getSec(){
        return sec;
    }
    public int setSec(int sec){
        return this.sec = sec;
    }

    public String getComment(){
        return comment;
    }

    protected String parseComment(String input){
        var matcher = rgxComment.matcher(input);
        if(matcher.find()){
            comment = matcher.group(1);
            return matcher.replaceAll("");
        }
        else{
            return input;
        }
    }
}
