package com.incarcloud.cuckoo.service.im2t;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ScriptActionTime extends ScriptAction{
    private static final DateTimeFormatter s_fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

    private final Instant tm;

    public ScriptActionTime(int sec, String args){
        super(sec);
        args = parseComment(args);

        var tmLocal = LocalDateTime.parse(args, s_fmt);
        this.tm = tmLocal.atZone(ZoneId.of("UTC")).toInstant();
    }

    public Instant getInitTm(){
        return tm;
    }
}
