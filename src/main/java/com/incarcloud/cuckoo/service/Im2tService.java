package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.service.im2t.SimScriptIm2t;
import com.incarcloud.cuckoo.service.im2t.SimSimpleIm2t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// 智己(IMMOTORS)
@Service
public class Im2tService {
    private static final Logger s_logger = LoggerFactory.getLogger(Im2tService.class);
    private String scripts = null;
    private ISim simOnRunning;

    public boolean isRunning(){
        return simOnRunning != null;
    }

    public void start(Im2tSimArgs args){
        if(simOnRunning != null) return;

        // SimSimpleIm2t ss = new SimSimpleIm2t(args);
        SimScriptIm2t ss = new SimScriptIm2t(args);
        if(this.scripts == null || this.scripts.isEmpty()){
            this.scripts = this.getScripts();
        }
        ss.setScripts(this.scripts);
        ss.start();
        this.simOnRunning = ss;
    }

    public void stop(){
        if(simOnRunning == null) return;

        this.simOnRunning.stop();
        this.simOnRunning = null;
    }

    public String getScripts(){
        if(this.scripts == null){
            // initial scripts
            var sbScripts = new StringBuilder();
            sbScripts.append("+00:00 time 2023-07-01 00:00:00 // 设定初始时间\n");
            sbScripts.append("+00:00 time_compress 1.0 // 设定时间压缩比\n");
            sbScripts.append("+00:00 vin LS5A33LR3FB356976 // 设定车辆VIN\n");
            sbScripts.append("+00:00 pos E114.5028 N30.4812 // 设定初始坐标\n");
            sbScripts.append("+00:00 speed  0.0km/h // 设定初始速度\n");
            sbScripts.append("+00:00 course 90.0 // 设定朝向方位角正东\n");
            sbScripts.append("+01:16 speed  5.0km/h // 1分16秒后慢慢行驶\n");
            sbScripts.append("+00:10 speed  35.0km/h // 再过10秒速度提升\n");
            sbScripts.append("+00:00 course 45.0 // 同时转变朝向\n");
            sbScripts.append("+02:00 speed  55.0km/h // 再过2分钟后速度提升\n");
            sbScripts.append("+05:00 speed  0.0km/h  // 5分钟后停下来\n");
            sbScripts.append("+00:30 finish // 结束\n");

            this.scripts = sbScripts.toString();
        }
        return this.scripts;
    }

    public void setScripts(String scripts){
        if(scripts == null || scripts.isEmpty()) return;
        this.scripts = scripts;
    }
}
