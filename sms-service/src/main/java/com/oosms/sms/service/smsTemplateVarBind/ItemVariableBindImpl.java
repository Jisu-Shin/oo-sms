package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.common.dto.CustListResponseDto;
import com.oosms.common.exception.ReplacementValueNotFoundException;
import com.oosms.sms.client.ItemApiServiceForVar;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.sms.service.smsTemplateVarBind.dto.BindingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ItemVariableBindImpl implements VariableBinder{

    public final ItemApiServiceForVar itemApiServiceForVar;

    private static final Map<String, Function<ItemGetResponseDto, String>> ITEM_VALUE_MAP = Map.of(
            "공연명",ItemGetResponseDto::getName,
            "공연가격",ItemGetResponseDto::getPrice
    );

    @Override
    public Map<String, String> getValues(List<TemplateVariable> tmpltVarList, BindingDto bindingDto) {
        Map<String, String> replacements = new HashMap<>();
        ItemGetResponseDto item = itemApiServiceForVar.getItem(bindingDto.getItemId());

        for (TemplateVariable tmpltVar : tmpltVarList) {
            String koText = tmpltVar.getKoText();
            Function<ItemGetResponseDto, String> function = ITEM_VALUE_MAP.get(koText);

            if (function == null) {
                throw new ReplacementValueNotFoundException(koText);
            }

            replacements.put(koText, function.apply(item));
        }

        return replacements;
    }

}
