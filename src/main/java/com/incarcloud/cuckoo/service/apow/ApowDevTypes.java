package com.incarcloud.cuckoo.service.apow;

public enum ApowDevTypes {
    Unknown(0), // 未知
    ThermoHygroMeter(1), // 温湿度计
    SmokeDetector(2), // 烟感
    Camera(3), // 摄像头
    ElectricMeter(10), // 电表
    PhotoVoltaic(11), // 光伏
    ACDC(12), // 逆变器
    DCDC(13), // 直流变换器
    Battery(14), // 电池
    RemoteSwitch(15); // 遥控开关

    private final int value;
    ApowDevTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
