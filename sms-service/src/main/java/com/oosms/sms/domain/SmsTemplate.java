package com.oosms.sms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsTemplate {

    @Id
    @GeneratedValue
    @Column(name = "sms_tmplt_id")
    private Long id;

    private String templateContent;

    @Enumerated(EnumType.STRING)
    private SmsType smsType;

    //todo cascade 확인하기 / 현재는 부모를 삭제할 때 연관된 자식도 자동삭제
    @OneToMany(mappedBy = "smsTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SmsTmpltVarRel> tmpltVarRelList = new ArrayList<>();

    private SmsTemplate(String templateContent, SmsType smsType) {
        this.templateContent = templateContent;
        this.smsType = smsType;
    }

    // ==생성 메서드==
    public static SmsTemplate createSmsTemplate(String templateContent, SmsType smsType) {
        validateSmsTemplate(templateContent, smsType);
        SmsTemplate smsTemplate = new SmsTemplate(templateContent, smsType);
        return smsTemplate;
    }

    // == 비즈니스 규칙 ==
    private static void validateSmsTemplate(String templateContent, SmsType smsType) {
        if (templateContent == null || templateContent.isEmpty()) {
            throw new IllegalArgumentException("sms템플릿 내용이 없습니다");
        }

        if (smsType == null) {
            throw new IllegalArgumentException("sms타입이 없습니다");
        }
    }

    // ==연관 메서드==
    public void addRelation(TemplateVariable tmpltVar) {
        this.tmpltVarRelList.add(SmsTmpltVarRel.create(this, tmpltVar));
    }

    public void update(String templateContent, SmsType smsType) {
        this.templateContent = templateContent;
        this.smsType = smsType;
    }

    public void clearRelList() {
        this.tmpltVarRelList.clear();
    }
}
