package com.incarcloud.cuckoo.service.im2t;

import java.util.regex.Pattern;

public class ScriptActionPos extends ScriptAction{
    // E114.5028 N30.4812
    private final static Pattern rgxPos = Pattern.compile("(E|W)(.*)\\s+(N|S)(.*)", Pattern.CASE_INSENSITIVE);
    private double dLat = 0.0;
    private double dLng = 0.0;

    public ScriptActionPos(int sec, String args){
        super(sec);
        args = parseComment(args);
        var matcher = rgxPos.matcher(args);
        if(matcher.find()){
            if(matcher.group(1).equalsIgnoreCase("E")){
                dLng = +1.0;
            }
            else if(matcher.group(1).equalsIgnoreCase("W")){
                dLng = -1.0;
            }
            else{
                throw new RuntimeException("Invalid script: " + args);
            }
            dLng *= Double.parseDouble(matcher.group(2));

            if(matcher.group(3).equalsIgnoreCase("N")){
                dLat = +1.0;
            }
            else if(matcher.group(3).equalsIgnoreCase("S")){
                dLat = -1.0;
            }
            else{
                throw new RuntimeException("Invalid script: " + args);
            }
            dLat *= Double.parseDouble(matcher.group(4));
        }
        else{
            throw new RuntimeException("Invalid script: " + args);
        }
    }

    public double getLat(){
        return dLat;
    }

    public double getLng(){
        return dLng;
    }
}
