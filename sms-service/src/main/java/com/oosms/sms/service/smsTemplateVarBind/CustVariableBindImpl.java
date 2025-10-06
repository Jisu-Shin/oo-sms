package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.sms.service.smsTemplateVarBind.dto.BindingDto;
import com.oosms.sms.client.CustApiServiceForVar;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.CustListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustVariableBindImpl implements VariableBinder {

    private final CustApiServiceForVar custApiService;

    @Override
    public Map<String, String> getValues(List<TemplateVariable> tmpltVarList, BindingDto bindingDto) {
        Map<String, String> replacements = new HashMap<>();
        CustListResponseDto cust = custApiService.getCust(bindingDto.getCustId());

        for (TemplateVariable tmpltVar : tmpltVarList) {
            if (tmpltVar.getKoText().equals("고객명")) {
                replacements.put("고객명", cust.getName());
            }
            else {
                throw new IllegalStateException("템플릿 변수 치환값이 없습니다: " + tmpltVar.getKoText());
            }
        }

        return replacements;
    }
}
