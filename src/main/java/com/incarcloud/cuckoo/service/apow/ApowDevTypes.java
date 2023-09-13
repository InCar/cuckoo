package com.incarcloud.cuckoo.service.apow;

public enum ApowDevTypes {
    Unknown(0), // 未知
    ThermoHygroMeter(1), // 温湿度计
    SmokeDetector(2), // 烟感
    Camera(3), // 摄像头
    ElectricMeter(10), // 电表
    DCDC(11), // 直流变换器
    ACDC(12), // 逆变器
    Battery(13), // 电池
    PhotoVoltaic(14), // 光伏
    RemoteController(15), // 遥控
    RemoteSwitch(16), // 遥控开关
    AcCharger(17), // 交流充电桩
    EV(18); // 电动汽车

    private final int value;
    ApowDevTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
