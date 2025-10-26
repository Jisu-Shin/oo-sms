package com.oosms.sms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsTmpltVarRel extends BaseTimeEntity {

    @EmbeddedId
    private SmsTmpltVarRelId smsTmpltVarRelId;

    @MapsId("smsTmpltId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sms_tmplt_id")
    private SmsTemplate smsTemplate;

    @MapsId("tmpltVarId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tmplt_var_id")
    private TemplateVariable templateVariable;

    private SmsTmpltVarRel(SmsTmpltVarRelId smsTmpltVarRelId) {
        this.smsTmpltVarRelId = smsTmpltVarRelId;
    }

    // == 생성 메서드 ==
    public static SmsTmpltVarRel create(SmsTemplate smsTemplate, TemplateVariable templateVariable) {
        SmsTmpltVarRelId id = new SmsTmpltVarRelId(smsTemplate.getId(), templateVariable.getId());

        SmsTmpltVarRel smsTmpltVarRel = new SmsTmpltVarRel(id);
        smsTmpltVarRel.setSmsTemplate(smsTemplate);
        smsTmpltVarRel.setTemplateVariable(templateVariable);

        return smsTmpltVarRel;
    }

    // == 연관관계 메서드 ==
    private void setSmsTemplate(SmsTemplate smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    private void setTemplateVariable(TemplateVariable templateVariable) {
        this.templateVariable = templateVariable;
    }

}
