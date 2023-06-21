package com.incarcloud.cuckoo.service.im2t;

import com.fasterxml.jackson.databind.ObjectMapper;

class Im2tJsonMapper {
    public static final ObjectMapper s_mapper = new ObjectMapper();
    public static ObjectMapper getMapper(){
        return s_mapper;
    }
}
