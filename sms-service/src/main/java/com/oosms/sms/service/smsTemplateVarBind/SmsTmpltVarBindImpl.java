package com.oosms.sms.service.smsTemplateVarBind;

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
        List<SmsTmpltVarRel> tmpltVarRelList = smsTemplate.getTmpltVarRelList();
        if (tmpltVarRelList.size() == 0) {
            return smsTemplate.getTemplateContent();
        }

        // TODO QueryDsl 이랑 Stream 비교해보기
        // 변수 유형별로 그룹핑
        Map<TemplateVariableType, List<TemplateVariable>> grouping = tmpltVarRelList.stream()
                .collect(Collectors.groupingBy(
                        rel -> rel.getTemplateVariable().getVariableType(),
                        Collectors.mapping(SmsTmpltVarRel::getTemplateVariable, Collectors.toList())
                ));
        log.info("@@@@@ 템플릿 변수 유형별로 그룹핑 완료");

        // 치환 맵 생성
        Map<String, String> replacements = grouping.entrySet().stream()
                .flatMap(entry -> {
                    VariableBinder binder = variableBinderMap.get(entry.getKey().getClassName());
                    if (binder == null) {
                        // todo Exception 필요
                        log.warn("VariableBinder not found for type: {}", entry.getKey());
                        return Stream.empty();
                    }
                    return binder.getValues(entry.getValue(), bindingDto).entrySet().stream();
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String messageContent = TemplateVariableUtils.replaceVariables(smsTemplate.getTemplateContent(), replacements);
        return messageContent;
    }
}
