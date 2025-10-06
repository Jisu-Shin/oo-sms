package com.oosms.cust.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustMapperTest {

    @Test
    public void 전화번호정규식() throws Exception {
        String phoneNumber = "01012345678";
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";

        String s = phoneNumber.replaceAll(regEx, "$1-$2-$3");
        System.out.println(s);

    }

}