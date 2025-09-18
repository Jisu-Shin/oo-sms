package com.oosms.sms.service.filter.customerSmsFilter;

import com.oosms.sms.domain.CustSmsConsentType;
import com.oosms.sms.domain.SmsType;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CustConsentFilter implements CustomerSmsFilter {
    /*
    if-else 문은 고객 SMS 동의 유형이 새롭게 추가될 때마다 OCP 위반
    유형이 추가될 떄마다 매번 수정하지 않게 Map 사용.
    Map만 수정하면 새로운 조합 추가 가능

    ===== 징점 =====
    OCP 준수 : enum 추가되더라도 분기 코드를 수정할 필요 없음
    SRP 명확 : 정책은 정책대로, 로직은 로직대로 관리
    확장성 : Map만 수정하면 새로운 조합 추가 가능
    테스트 용이 : 정책만 따로 단위 테스트 가능
     */
    private final Map<CustSmsConsentType, Set<SmsType>> allowMap = new HashMap<>();
    public CustConsentFilter() {
        allowMap.put(CustSmsConsentType.ALL_ALLOW, EnumSet.allOf(SmsType.class));
        allowMap.put(CustSmsConsentType.ALL_DENY, EnumSet.of(SmsType.VERIFICATION)); // 인증 문자는 항상 허용.
        allowMap.put(CustSmsConsentType.ADVERTISE_DENY, EnumSet.of(SmsType.INFORMAITONAL, SmsType.VERIFICATION));
    }

    public boolean isSendable(CustSmsConsentType custSmsConsentType, SmsType smsType) {
        Set<SmsType> allowedType = allowMap.getOrDefault(custSmsConsentType, EnumSet.noneOf(SmsType.class));
        return allowedType.contains(smsType);
    }
}
