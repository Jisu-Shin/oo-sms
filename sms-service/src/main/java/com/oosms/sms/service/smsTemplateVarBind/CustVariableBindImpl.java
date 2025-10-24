package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.common.exception.ReplacementValueNotFoundException;
import com.oosms.sms.service.smsTemplateVarBind.dto.BindingDto;
import com.oosms.sms.client.CustApiServiceForVar;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.CustListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CustVariableBindImpl implements VariableBinder {

    private final CustApiServiceForVar custApiService;

    private static final Map<String, Function<CustListResponseDto, String>> CUST_VALUE_MAP = Map.of(
            "고객명",CustListResponseDto::getName,
            "고객전화번호",CustListResponseDto::getPhoneNumber
    );

    @Override
    public Map<String, String> getValues(List<TemplateVariable> tmpltVarList, BindingDto bindingDto) {
        Map<String, String> replacements = new HashMap<>();
        CustListResponseDto cust = custApiService.getCust(bindingDto.getCustId());

        for (TemplateVariable tmpltVar : tmpltVarList) {
            String koText = tmpltVar.getKoText();

            Function<CustListResponseDto, String> function = CUST_VALUE_MAP.get(koText);
            if (function == null){
                throw new ReplacementValueNotFoundException(koText);
            }

            replacements.put(koText, function.apply(cust));
        }

        return replacements;
    }
}
