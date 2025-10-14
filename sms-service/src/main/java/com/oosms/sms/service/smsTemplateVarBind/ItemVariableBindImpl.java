package com.oosms.sms.service.smsTemplateVarBind;

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

@Component
@RequiredArgsConstructor
public class ItemVariableBindImpl implements VariableBinder{

    public final ItemApiServiceForVar itemApiServiceForVar;

    @Override
    public Map<String, String> getValues(List<TemplateVariable> tmpltVarList, BindingDto bindingDto) {
        Map<String, String> replacements = new HashMap<>();
        ItemGetResponseDto item = itemApiServiceForVar.getItem(bindingDto.getItemId());

        for (TemplateVariable tmpltVar : tmpltVarList) {
            if (tmpltVar.getKoText().equals("공연명")) {
                replacements.put("공연명", item.getName());
            }
            else if (tmpltVar.getKoText().equals("공연가격")) {
                replacements.put("공연가격", String.valueOf(item.getPrice()));
            }
            else {
                throw new ReplacementValueNotFoundException(tmpltVar.getKoText());
            }
        }

        return replacements;
    }

}
