package com.oosms.cust.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cust extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cust_id")
    private Long id;

    private String name;

    @Column(length = 12)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CustSmsConsentType smsConsentType;

    @Builder
    private Cust(Long id, String name, String phoneNumber, CustSmsConsentType smsConsentType) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.smsConsentType = smsConsentType;
    }

    //==생성메서드==
    public static Cust createCust(String name, String phoneNumber, CustSmsConsentType smsConsentType){
        Cust cust = Cust.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .smsConsentType(smsConsentType).build();
        return cust;
    }

    public void update(String phoneNumber, CustSmsConsentType type) {
        this.phoneNumber = phoneNumber;
        this.smsConsentType = type;
    }

    @Override
    public String toString() {
        return "Cust{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", smsConsentType=" + smsConsentType +
                '}';
    }
}
