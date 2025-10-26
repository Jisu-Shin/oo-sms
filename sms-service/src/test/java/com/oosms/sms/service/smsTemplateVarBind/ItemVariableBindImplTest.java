package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.common.exception.ReplacementValueNotFoundException;
import com.oosms.sms.service.dto.BindingDto;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.sms.client.ItemApiServiceForVar;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemVariableBindImplTest {

    @Mock
    ItemApiServiceForVar itemApiServiceForVar;

    @InjectMocks
    ItemVariableBindImpl itemVariableBind;

    @Mock
    BindingDto bindingDto;

    @Test
    public void 공연변수() throws Exception {
        //given
        String itemName = "2NE1 콘서트";
        String itemPrice = "200,000";

        ItemGetResponseDto itemGetResponseDto = new ItemGetResponseDto();
        itemGetResponseDto.setName(itemName);
        itemGetResponseDto.setPrice(itemPrice);

        when(itemApiServiceForVar.getItem(any())).thenReturn(itemGetResponseDto);

        List<TemplateVariable> templateVariableList = new ArrayList<>();
        templateVariableList.add(TemplateVariable.create("itemName","공연명", TemplateVariableType.ITEM));
        templateVariableList.add(TemplateVariable.create("itemPrice","공연가격", TemplateVariableType.ITEM));

        //when
        Map<String, String> resultMap = itemVariableBind.getValues(templateVariableList, bindingDto);

        //then
        assertEquals(itemName, resultMap.get("공연명"));
        assertEquals(itemPrice, resultMap.get("공연가격"));

        System.out.println("resultMap.get(\"공연가격\") = " + resultMap.get("공연가격"));
    }

    @Test
    public void 공연변수값이_없는경우() throws Exception {
        //given
        ItemGetResponseDto itemGetResponseDto = new ItemGetResponseDto();
        when(itemApiServiceForVar.getItem(any())).thenReturn(itemGetResponseDto);

        List<TemplateVariable> templateVariableList = new ArrayList<>();
        templateVariableList.add(TemplateVariable.create("itemPlace","공연장소", TemplateVariableType.ITEM));

        //when
        assertThrows(ReplacementValueNotFoundException.class, ()->itemVariableBind.getValues(templateVariableList, bindingDto));

        //then
    }

}