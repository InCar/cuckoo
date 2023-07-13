package com.incarcloud.cuckoo.service.im2t;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class ScriptFactory {
    private final static Logger s_logger = LoggerFactory.getLogger(ScriptFactory.class);

    private static final Pattern rgxAction = Pattern.compile("^(\\+\\d+:\\d+)(\\s+)(\\w+)(\\s*)(.*)", Pattern.CASE_INSENSITIVE);
    public static ScriptAction parse(String script) {
        // +00:00 time 2023-01-01 00:00:00 // 设定初始时间
        var matcher = rgxAction.matcher(script);
        if(matcher.find()){
            String tm = matcher.group(1);
            int sec = tm2sec(tm);
            String action = matcher.group(3);
            String args = matcher.group(5);
            if(action.equalsIgnoreCase("time")){
                return new ScriptActionTime(sec, args);
            }
            else if(action.equalsIgnoreCase("time_compress")){
                return new ScriptActionTimeCompress(sec, args);
            }
            else if(action.equalsIgnoreCase("pos")){
                return new ScriptActionPos(sec, args);
            }
            else if(action.equalsIgnoreCase("speed")){
                return null;
            }
            else if(action.equalsIgnoreCase("course")){
                return null;
            }
            else if(action.equalsIgnoreCase("finish")){
                return new ScriptActionFinish(sec, args);
            }
            else{
                throw new RuntimeException("Invalid script: " + script);
            }
        }
        else{
            throw new RuntimeException("Invalid script: " + script);
        }
    }

    private static int tm2sec(String tm){
        String[] arr = tm.replace("+", "").split(":");
        int m = Integer.parseInt(arr[0]);
        int s = Integer.parseInt(arr[1]);
        return m * 60 + s;
    }
}
