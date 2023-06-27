package com.incarcloud.cuckoo.service;

import java.time.Instant;

public interface IDev {
    byte[] makeDataPackage(Instant tm);
}
