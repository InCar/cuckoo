package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.databind.ObjectMapper;

class ApowJsonMapper {
    public static final ObjectMapper s_mapper = new ObjectMapper();
    public static ObjectMapper getMapper(){
        return s_mapper;
    }
}
