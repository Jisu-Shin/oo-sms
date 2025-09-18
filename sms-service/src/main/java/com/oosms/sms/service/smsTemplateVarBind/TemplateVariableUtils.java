package com.oosms.sms.service.smsTemplateVarBind;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TemplateVariableUtils {

    private static final Pattern PATTERN_VAR = Pattern.compile("#\\{(.*?)\\}");

    /**
     * 템플릿내용에서 #{변수}로 감싸진 변수명을 추출한다
     * @param templateContent 템플릿내용
     * @return
     */
    public static List<String> extractVariabels(String templateContent) {
        List<String> varList = new ArrayList<>();

        Matcher matcher = PATTERN_VAR.matcher(templateContent);

        //when
        while(matcher.find()) {
            varList.add(matcher.group(1));
        }

        return varList;
    }

    /**
     * 템플릿 내용에서 변수 치환
     * @param templateContent 템플릿 내용
     * @param replacements 치환할 변수 Map<변수명, 변수값>
     * @return
     */
    public static String replaceVariables(String templateContent, Map<String, String> replacements) {
        String result = templateContent;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            result = result.replaceAll("#\\{" + entry.getKey() + "\\}", entry.getValue());
        }

        return result;
    }
}
