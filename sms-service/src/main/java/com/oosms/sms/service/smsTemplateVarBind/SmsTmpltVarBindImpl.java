package com.oosms.sms.service.smsTemplateVarBind;

import com.oosms.common.exception.VariableBinderNotFoundException;
import com.oosms.sms.service.smsTemplateVarBind.dto.BindingDto;
import com.oosms.sms.domain.SmsTemplate;
import com.oosms.sms.domain.SmsTmpltVarRel;
import com.oosms.sms.domain.TemplateVariable;
import com.oosms.sms.domain.TemplateVariableType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsTmpltVarBindImpl implements SmsTmpltVarBinder {

    private final Map<String, VariableBinder> variableBinderMap;

    @Override
    public String bind(SmsTemplate smsTemplate, BindingDto bindingDto) {
        List<SmsTmpltVarRel> templateVariableRelations = smsTemplate.getTmpltVarRelList();
        
        if (templateVariableRelations.isEmpty()) {
            return smsTemplate.getTemplateContent();
        }

        Map<TemplateVariableType, List<TemplateVariable>> variableBinderMap =
                groupVariablesByType(templateVariableRelations);
        
        Map<String, String> replacements = createReplacementMap(variableBinderMap, bindingDto);

        return TemplateVariableUtils.replaceVariables(smsTemplate.getTemplateContent(), replacements);
    }

    /**
     * sms템플릿에 있는 템플릿변수들을 타입(고객/아이템..등등) 에 따라 분류
     * @param templateVariableRelations
     * @return sms템플릿에 있는 템플릿 변수들을 TemplateVariableType 별로 분류해놓은 map
     */
    private Map<TemplateVariableType, List<TemplateVariable>> groupVariablesByType(
            List<SmsTmpltVarRel> templateVariableRelations) {
        
        Map<TemplateVariableType, List<TemplateVariable>> variableBinderMap = templateVariableRelations.stream()
                .collect(Collectors.groupingBy(
                        relation -> relation.getTemplateVariable().getVariableType(),
                        Collectors.mapping(SmsTmpltVarRel::getTemplateVariable, Collectors.toList())
                ));
        
        log.info("Grouped template variables by type: {} types found", variableBinderMap.size());
        return variableBinderMap;
    }

    /**
     * variableBinderMap 에 있는 key별로 치환값 가져오기
     * @param variableBinderMap sms템플릿에 있는 템플릿 변수들을 TemplateVariableType 별로 분류해놓은 map
     * @param bindingDto
     * @return
     */
    private Map<String, String> createReplacementMap(
            Map<TemplateVariableType, List<TemplateVariable>> variableBinderMap, 
            BindingDto bindingDto) {
        
        return variableBinderMap.entrySet().stream()
                .flatMap(typeEntry -> getReplacementValueByBinder(typeEntry, bindingDto))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 바인더클래스를 통해 템플릿변수의 치환값을 가져온다
     * @param typeEntry
     * @param bindingDto
     * @return
     */
    private Stream<Map.Entry<String, String>> getReplacementValueByBinder(
            Map.Entry<TemplateVariableType, List<TemplateVariable>> typeEntry, 
            BindingDto bindingDto) {
        
        TemplateVariableType variableType = typeEntry.getKey();
        List<TemplateVariable> variables = typeEntry.getValue();
        
        VariableBinder binder = variableBinderMap.get(variableType.getClassName());
        if (binder == null) {
            throw new VariableBinderNotFoundException(variableType.getClassName());
        }
        
        return binder.getValues(variables, bindingDto).entrySet().stream();
    }
}
