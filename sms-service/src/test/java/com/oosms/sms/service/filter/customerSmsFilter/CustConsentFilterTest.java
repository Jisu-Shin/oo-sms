package com.oosms.sms.service.filter.customerSmsFilter;

import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.SmsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustConsentFilterTest {

    CustConsentFilter custConsentFilter = new CustConsentFilter();

    @Test
    public void 모두허용() throws Exception {
        //given

        //when
        boolean sendable = custConsentFilter.isSendable(CustSmsConsentType.ALL_ALLOW, SmsType.INFORMAITONAL);
        boolean sendable2 = custConsentFilter.isSendable(CustSmsConsentType.ALL_ALLOW, SmsType.VERIFICATION);
        boolean sendable3 = custConsentFilter.isSendable(CustSmsConsentType.ALL_ALLOW, SmsType.ADVERTISING);

        //then
        assertEquals(true, sendable);
        assertEquals(true, sendable2);
        assertEquals(true, sendable3);
    }

    @Test
    public void 고객유형_모두거부() throws Exception {
        //given

        //when
        boolean infoResult = custConsentFilter.isSendable(CustSmsConsentType.ALL_DENY, SmsType.INFORMAITONAL);
        boolean verifResult = custConsentFilter.isSendable(CustSmsConsentType.ALL_DENY, SmsType.VERIFICATION);
        boolean adverResult = custConsentFilter.isSendable(CustSmsConsentType.ALL_DENY, SmsType.ADVERTISING);

        //then
        assertEquals(false, infoResult);
        assertEquals(true, verifResult); // 인증은 항상 나가야 함
        assertEquals(false, adverResult);
    }

    @Test
    public void 고객유형_광고거부() throws Exception {
        //given

        //when
        boolean infoResult = custConsentFilter.isSendable(CustSmsConsentType.ADVERTISE_DENY, SmsType.INFORMAITONAL);
        boolean verifResult = custConsentFilter.isSendable(CustSmsConsentType.ADVERTISE_DENY, SmsType.VERIFICATION);
        boolean adverResult = custConsentFilter.isSendable(CustSmsConsentType.ADVERTISE_DENY, SmsType.ADVERTISING);

        //then
        assertEquals(true, infoResult);
        assertEquals(true, verifResult);
        assertEquals(false, adverResult);
    }

}