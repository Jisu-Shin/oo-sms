package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.sms.service.dto.BindingDto;
import com.oosms.sms.domain.TemplateVariable;

import java.util.List;
import java.util.Map;

public interface VariableBinder {

    public Map<String, String> getValues(List<TemplateVariable> tmpltVarList, BindingDto bindingDto);
}
