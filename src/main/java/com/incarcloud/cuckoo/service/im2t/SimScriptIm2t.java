package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.cape.mq.kafka.Blinken618;
import com.incarcloud.cuckoo.service.ISim;
import com.incarcloud.cuckoo.service.Im2tSimArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

// 这是一种简单的模拟器,只模拟一台车,但可以依据脚本执行一些动作
public class SimScriptIm2t implements ISim {
    private final static Logger s_logger = LoggerFactory.getLogger(SimSimpleIm2t.class);

    private Im2tSimArgs taskArgs;

    // 简单模拟一台车
    private final VehicleX vehicleX;

    // Kafka发送器
    private final Blinken618 blinken618;

    private AtomicBoolean atomCanStop = new AtomicBoolean(false);
    private Thread threadWorking = null;

    private ArrayList<ScriptAction> listActions = new ArrayList<ScriptAction>();
    private Instant tmStartPlay; // 模拟的开始时间
    private float fTmRatio = 1.0f; // 时间压缩比 2.0x代表模拟器以2倍现实世界的时间快速运行

    public SimScriptIm2t(Im2tSimArgs args){
        this.taskArgs = args;
        // 当前只有这一种车型
        var tm = Instant.now();
        this.tmStartPlay = tm;
        this.vehicleX = new VehicleEP33L("LS5A33LR3FB356976", tm);
        this.blinken618 = new Blinken618(args.host, args.port, args.topic);
    }

    public void setScripts(String scripts){
        this.listActions.clear();

        String[] listScripts = scripts.split("\n");
        for (String script: listScripts) {
            if(script.length() == 0) continue; //skip empty line
            var action = ScriptFactory.parse(script);
            if(action != null){
                listActions.add(action);
            }
            else{
                s_logger.warn("Invalid script: {}", script);
            }
        }

        // 时间是以累加的方式计算的
        int totalSeconds = 0;
        for(var action: listActions){
            totalSeconds += action.getSec();
            action.setSec(totalSeconds);
        }
    }

    @Override
    public void start() {
        // 如果已经在运行中,则无需再次启动
        if(threadWorking != null) return;

        atomCanStop.set(false);
        threadWorking = new Thread(this::run);
        threadWorking.start();
        s_logger.info("SimSimpleIm2t.start");
    }

    @Override
    public void stop() {
        atomCanStop.set(true);
        if(threadWorking != null){
            try {
                threadWorking.join();
            } catch (Exception e) {
                s_logger.error("SimSimpleIm2t.stop", e);
            }
            threadWorking = null;
        }
        s_logger.info("SimSimpleIm2t.stop");
    }

    private void run(){
        int count = 0;

        try {
            int countExecuted = execInitActions(); //执行初始化动作

            final int simIntervalMS = 1000*10; // 毫秒. 模拟的车辆每10秒(模拟世界的时间)发送一次数据包
            final int nIntervalRealMS = (int)(simIntervalMS / fTmRatio); // 毫秒. 考虑时间压缩系数,对应真实世界的时间间隔
            if(nIntervalRealMS < 100) s_logger.warn("time_compress倍率过高 {}", fTmRatio);
            int nX = 1; // 每几次循环发送一次数据
            int nWaitInterval = nIntervalRealMS;
            while(nWaitInterval > 1000){
                nX *= 2;
                nWaitInterval /= 2;
            }
            Instant tmStartReal = Instant.now(); // 模拟开始的真实时间

            while (!atomCanStop.get()) {
                if(count % nX == 0) {
                    Instant tmNow = Instant.now();
                    int nDiffMS = (int)((tmNow.toEpochMilli() - tmStartReal.toEpochMilli()) * fTmRatio);
                    Instant tmNowPlay = tmStartPlay.plusMillis(nDiffMS);
                    int nDiffSeconds = nDiffMS / 1000;

                    countExecuted = execActions(countExecuted, nDiffSeconds); // 执行动作
                    if(countExecuted < 0) break; // 负值代表[finish]停止执行

                    byte[] data = this.vehicleX.makeDataPackage(tmNowPlay);

                    this.blinken618.sendAsync(this.taskArgs.topic, data);
                    // 调试用途
                    // s_logger.info(new String(data));
                }
                count++;
                Thread.sleep(nWaitInterval);
            }

            s_logger.info("SimSimpleIm2t.run finished");
        }
        catch (Exception e){
            s_logger.error("SimSimpleIm2t.run", e);
        }
    }

    private int execInitActions(){
        int nCount = 0;
        for(var action : listActions){
            if(action.getSec() == 0){
                if(action instanceof ScriptActionTime){
                    // 设定模拟初始时间
                    var actTM = (ScriptActionTime)action;
                    tmStartPlay = actTM.getInitTm();
                    this.vehicleX.setTm(tmStartPlay);
                }
                else if(action instanceof ScriptActionTimeCompress){
                    // 设定时间压缩比
                    var actTmCompress = (ScriptActionTimeCompress)action;
                    fTmRatio = actTmCompress.getCompressRatio();
                }
                else if(action instanceof ScriptActionVin){
                    var actionVin = (ScriptActionVin)action;
                    this.vehicleX.setVin(actionVin.getVin());
                }
                else if(action instanceof ScriptActionPos){
                    var actionPos = (ScriptActionPos)action;
                    this.vehicleX.setPos(actionPos.getLng(), actionPos.getLat());
                }
                else if(action instanceof ScriptActionSpeed){
                    var actionSpeed = (ScriptActionSpeed)action;
                    this.vehicleX.setSpeed(actionSpeed.getSpeed());
                }
                else if(action instanceof ScriptActionCourse){
                    var actionCourse = (ScriptActionCourse)action;
                    this.vehicleX.setCourse(actionCourse.getCourse());
                }
                s_logger.info("action: {}", action.getComment());

                nCount++;
            }
            else{
                // 非0秒的动作, 表明不是初始化动作，不在此处执行
                break;
            }
        }

        return nCount;
    }

    private int execActions(int countExecuted, int nDiffSeconds){
        int count = countExecuted;
        for(int i=countExecuted;i<listActions.size();i++){
            var action = listActions.get(i);
            if(action.getSec() <= nDiffSeconds){
                if(action instanceof ScriptActionFinish){
                    return -1; // 负值代表[finish]停止执行
                }
                else if(action instanceof ScriptActionPos){
                    var actionPos = (ScriptActionPos)action;
                    this.vehicleX.setPos(actionPos.getLng(), actionPos.getLat());
                }
                else if(action instanceof ScriptActionSpeed){
                    var actionSpeed = (ScriptActionSpeed)action;
                    this.vehicleX.setSpeed(actionSpeed.getSpeed());
                }
                else if(action instanceof ScriptActionCourse){
                    var actionCourse = (ScriptActionCourse)action;
                    this.vehicleX.setCourse(actionCourse.getCourse());
                }
                s_logger.info("{} seconds: {}", nDiffSeconds, action.getComment());
            }
            else{
                break;
            }

            count++;
        }

        return count;
    }
}
