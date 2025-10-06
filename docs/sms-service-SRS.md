# SMS Service 모듈 소프트웨어 요구사항 명세서 (SRS)

## 1. 개요

### 1.1 목적
이 문서는 OO-SMS 시스템의 sms-service 모듈에 대한 소프트웨어 요구사항을 정의합니다. sms-service는 문자 발송 및 문자 템플릿 관리를 담당하는 핵심 서비스입니다.

### 1.2 범위
- SMS 발송 및 관리
- SMS 템플릿 관리 (생성, 수정, 삭제, 조회)
- 템플릿 변수 관리 및 바인딩
- SMS 발송 이력 조회
- SMS 필터링 (시간, 고객동의, 광고횟수 제한)
- 외부 서비스 연동 (고객 정보, 공연 정보)
- 환경별 필터링 규칙 적용

---

## 2. 시스템 구조

### 2.1 아키텍처 계층
```
┌─────────────────────────────────┐
│   API Layer (Controller)        │
│   - SmsApiController            │
│   - SmsTemplateApiController    │
│   - TemplateVariableController  │
├─────────────────────────────────┤
│   Service Layer                 │
│   - SmsService                  │
│   - SmsTemplateService          │
│   - TemplateVariableService     │
├─────────────────────────────────┤
│   Filter & Binding Layer        │
│   - SmsFilter                   │
│   - SmsTmpltVarBinder           │
│   - VariableBinder (interface)  │
├─────────────────────────────────┤
│   Factory Layer                 │
│   - SmsFactory                  │
├─────────────────────────────────┤
│   Repository Layer              │
│   - JpaSmsRepository            │
│   - JpaSmsTemplateRepository    │
│   - JpaTemplateVariableRepo     │
│   - QueryDSL (Custom Query)     │
└─────────────────────────────────┘
```

### 2.2 주요 컴포넌트
- **SmsService**: SMS 발송 및 조회 서비스
- **SmsTemplateService**: SMS 템플릿 관리 서비스 (CRUD)
- **TemplateVariableService**: 템플릿 변수 관리 서비스
- **SmsFactory**: SMS 객체 생성 팩토리
- **SmsFilter**: SMS 발송 필터링 처리 (시간/동의/광고)
- **SmsTmpltVarBinder**: 템플릿 변수 바인딩 처리
- **CustApiService**: 고객 정보 외부 서비스 연동
- **ItemApiService**: 공연 정보 외부 서비스 연동

### 2.3 도메인 엔티티
- **Sms**: SMS 발송 정보 (수신번호, 내용, 발송시간, 결과)
- **SmsTemplate**: SMS 템플릿 (내용, 유형)
- **TemplateVariable**: 템플릿 변수 (한글명, 영문명, 유형)
- **SmsTmpltVarRel**: 템플릿-변수 관계 (다대다 중간 엔티티)
- **SmsTmpltVarRelId**: 템플릿-변수 복합키

---

## 3. 기능 요구사항

### 3.1 SMS 발송 기능

#### 3.1.1 SMS 발송 (REQ-001)
**설명**: 고객 리스트와 템플릿을 이용하여 SMS를 발송합니다.

**입력**:
```json
{
  "custIdList": [
    {
      "custId": 1,
      "phoneNumber": "01012345678", 
      "custSmsConsentType": "ALL_ALLOW"
    }
  ],
  "sendDt": "202509061400",
  "templateId": 1,
  "itemId": 100
}
```

**처리 과정**:
1. 입력값 검증
   - 고객 리스트 null/empty 체크
   - 전화번호 형식 검증
   - 템플릿 존재 여부 확인
2. SMS 템플릿 조회 (`JpaSmsTemplateRepository`)
3. 고객 리스트 스트림 처리
4. 각 고객별 `SmsFactory.createSms()` 호출
   - 템플릿 변수 바인딩
   - SMS 필터링 수행
   - SmsResult 설정
5. 생성된 SMS 리스트 저장
6. 발송 결과 반환

**출력**: boolean (전체 발송 성공 여부)

**예외 상황**:
- 템플릿 없음: `IllegalArgumentException` - "템플릿을 찾을 수 없습니다"
- custIdList null/empty: 유효성 검증 실패

**API 엔드포인트**: `POST /api/sms`

---

#### 3.1.2 SMS 필터링 규칙 (REQ-002)
**설명**: SMS 발송 전 필터링을 수행하여 발송 가능 여부를 판단합니다.

**SmsResult 종류**:
- `SUCCESS`: 발송 성공
- `NOT_SEND_TIME`: 발송 불가 시간 (야간시간대 등)
- `CUST_REJECT`: 고객 거절 (동의 거부)
- `AD_COUNT_OVER`: 광고 개수 초과

**필터링 순서**:
1. **인증 문자 예외 처리**
   - SmsType이 `VERIFICATION`이면 모든 필터 통과
   - 바로 `SUCCESS` 반환
   
2. **시간 필터링** (`SmsFilter.timeFilter()`)
   - 환경별 적용 (`@Profile` 사용)
   - **운영환경(prod)**: 오전 8시 ~ 오후 8시만 발송 가능
   - **개발/테스트환경**: 시간 제한 없음
   - 불가 시간이면 `NOT_SEND_TIME` 반환

3. **고객 동의 필터링** (`SmsFilter.custConsentFilter()`)
   - `ALL_ALLOW`: 모든 유형 발송 가능
   - `ALL_DENY`: 인증 문자만 발송 가능 (이미 통과했으므로)
   - `ADVERTISE_DENY`: 광고성 문자 거부
     - SmsType이 `ADVERTISING`이면 `CUST_REJECT` 반환
   
4. **광고 횟수 제한 필터링** (`SmsFilter.adCountFilter()`)
   - SmsType이 `ADVERTISING`일 때만 적용
   - 고객의 오늘 광고 SMS 발송 횟수 조회
   - 2건 이상이면 `AD_COUNT_OVER` 반환

**출력**: SmsResult (필터링 결과)

**예외 상황**: 없음 (필터링 실패 시에도 SMS 객체는 생성되고 결과만 실패로 표시)

---

#### 3.1.3 SMS 발송 이력 조회 (REQ-003)
**설명**: 특정 기간의 SMS 발송 이력을 조회합니다.

**입력**: 
- startDt: 시작일시 (yyyyMMddHHmm)
- endDt: 종료일시 (yyyyMMddHHmm)

**처리 과정**:
1. DateTimeFormatter로 문자열을 LocalDateTime 변환
2. QueryDSL을 사용하여 기간 내 SMS 조회
3. Mapper로 DTO 변환

**출력**: List<SmsGetResponseDto>
```json
[
  {
    "smsId": 1,
    "sendPhoneNumber": "01012345678",
    "smsContent": "안녕하세요 홍길동님",
    "sendDt": "202509061400",
    "smsType": "INFORMATIONAL", 
    "smsResult": "SUCCESS"
  }
]
```

**예외 상황**:
- 잘못된 날짜 형식: `DateTimeParseException`

**API 엔드포인트**: `GET /api/sms?startDt={startDt}&endDt={endDt}`

---

### 3.2 SMS 템플릿 관리 기능

#### 3.2.1 SMS 템플릿 생성 (REQ-004)
**설명**: 새로운 SMS 템플릿을 생성합니다.

**입력**:
```json
{
  "templateContent": "안녕하세요 #{고객명}님, #{공연명} 공연 예매가 완료되었습니다.",
  "smsType": "INFORMATIONAL"
}
```

**처리 과정**:
1. 템플릿 내용에서 변수 추출 (`#{변수명}` 패턴)
   - 정규표현식: `#\{([^}]+)\}`
2. 추출된 변수명으로 TemplateVariable 조회
3. 변수 유효성 검증 (등록된 템플릿 변수인지 확인)
4. SmsTemplate 엔티티 생성 및 저장
5. 템플릿-변수 관계(SmsTmpltVarRel) 생성 및 저장
6. 생성된 템플릿 ID 반환

**출력**: Long (생성된 Template ID)

**예외 상황**:
- 존재하지 않는 변수: `IllegalArgumentException` - "등록되지 않은 템플릿 변수입니다"
- 빈 템플릿 내용: 유효성 검증 실패

**API 엔드포인트**: `POST /api/sms-templates`

---

#### 3.2.2 SMS 템플릿 수정 (REQ-005)
**설명**: 기존 SMS 템플릿을 수정합니다.

**입력**:
```json
{
  "id": 1,
  "templateContent": "수정된 #{고객명}님 메시지",
  "smsType": "ADVERTISING"
}
```

**처리 과정**:
1. ID로 기존 템플릿 조회
2. 템플릿이 없으면 예외 발생
3. 기존 템플릿-변수 관계 전체 삭제
4. 새로운 템플릿 내용에서 변수 추출
5. 변수 유효성 검증
6. 새로운 템플릿-변수 관계 생성
7. 템플릿 엔티티 업데이트 (Dirty Checking)
8. 수정된 템플릿 ID 반환

**출력**: Long (수정된 Template ID)

**예외 상황**:
- 존재하지 않는 템플릿: `IllegalArgumentException` - "템플릿을 찾을 수 없습니다"
- 존재하지 않는 변수: `IllegalArgumentException` - "등록되지 않은 템플릿 변수입니다"

**API 엔드포인트**: `PUT /api/sms-templates/{id}`

---

#### 3.2.3 SMS 템플릿 삭제 (REQ-006)
**설명**: SMS 템플릿을 삭제합니다.

**입력**: id (Long, Path Variable)

**처리 과정**:
1. ID로 템플릿 조회
2. 템플릿이 없으면 예외 발생
3. 템플릿-변수 관계 전체 삭제 (SmsTmpltVarRel)
4. 템플릿 삭제
5. 삭제된 템플릿 ID 반환

**출력**: Long (삭제된 Template ID)

**예외 상황**:
- 존재하지 않는 템플릿: `IllegalArgumentException` - "템플릿을 찾을 수 없습니다"

**API 엔드포인트**: `DELETE /api/sms-templates/{id}`

---

#### 3.2.4 SMS 템플릿 목록 조회 (REQ-007)
**설명**: 전체 SMS 템플릿 목록을 조회합니다.

**입력**: 없음

**처리 과정**:
1. JpaSmsTemplateRepository.findAll() 호출
2. Mapper로 DTO 변환

**출력**: List<SmsTemplateGetResponseDto>
```json
[
  {
    "id": 1,
    "templateContent": "안녕하세요 #{고객명}님",
    "smsType": "INFORMATIONAL",
    "variableList": ["고객명"]
  }
]
```

**예외 상황**: 없음 (빈 리스트 반환 가능)

**API 엔드포인트**: `GET /api/sms-templates`

---

### 3.3 템플릿 변수 관리 기능

#### 3.3.1 템플릿 변수 생성 (REQ-008)
**설명**: 새로운 템플릿 변수를 생성합니다.

**입력**:
```json
{
  "enText": "custName",
  "koText": "고객명",
  "variableType": "고객"
}
```

**처리 과정**:
1. TemplateVariable 엔티티 생성
2. 저장
3. 생성된 변수 ID 반환

**출력**: Long (생성된 Variable ID)

**예외 상황**:
- 중복된 한글명: `DataIntegrityViolationException`
- 빈 값: 유효성 검증 실패

**API 엔드포인트**: `POST /api/template-variables`

---

#### 3.3.2 템플릿 변수 목록 조회 (REQ-009)
**설명**: 전체 템플릿 변수 목록을 조회합니다.

**입력**: 없음

**처리 과정**:
1. JpaTemplateVariableRepository.findAll() 호출
2. Mapper로 DTO 변환

**출력**: List<TemplateVariableGetResponseDto>
```json
[
  {
    "id": 1,
    "enText": "custName",
    "koText": "고객명",
    "variableType": "고객"
  }
]
```

**예외 상황**: 없음 (빈 리스트 반환 가능)

**API 엔드포인트**: `GET /api/template-variables`

---

### 3.4 템플릿 변수 바인딩

#### 3.4.1 템플릿 변수 바인딩 (REQ-010)
**설명**: 템플릿의 변수를 실제 값으로 치환합니다.

**입력**:
- templateContent: 템플릿 내용
- custId: 고객 ID
- itemId: 공연 ID

**처리 과정**:
1. 템플릿에서 변수 추출 (정규표현식)
2. 변수를 variableType별로 그룹핑
3. 각 variableType별 VariableBinder 호출
   - **고객 변수**: CustVariableBinder
     - CustApiService로 고객 정보 조회
     - 변수명에 따라 값 매핑 (고객명 → name)
   - **공연 변수**: ItemVariableBinder
     - ItemApiService로 공연 정보 조회
     - 변수명에 따라 값 매핑 (공연명 → name, 공연가격 → price)
4. 각 변수를 실제 값으로 치환
5. 최종 변환된 문자열 반환

**출력**: String (변수가 치환된 SMS 내용)

**예시**:
- 입력: "안녕하세요 #{고객명}님, #{공연명} 예매 완료"
- 출력: "안녕하세요 홍길동님, 뮤지컬 캣츠 예매 완료"

**예외 상황**:
- 존재하지 않는 고객/공연: 외부 서비스에서 예외 발생
- 변수 타입 미지원: `IllegalArgumentException`

---

### 3.5 외부 서비스 연동

#### 3.5.1 고객 서비스 연동 (REQ-011)
**설명**: 고객 정보를 조회합니다.

**연동 방식**: RestTemplate을 통한 HTTP 통신

**API 엔드포인트**: `GET {custServiceUrl}/api/custs/{custId}`

**응답**:
```json
{
  "custId": 1,
  "name": "홍길동",
  "phoneNumber": "01012345678"
}
```

---

#### 3.5.2 공연 서비스 연동 (REQ-012)
**설명**: 공연 정보를 조회합니다.

**연동 방식**: RestTemplate을 통한 HTTP 통신

**API 엔드포인트**: `GET {bookingServiceUrl}/api/items/{itemId}`

**응답**:
```json
{
  "id": 1,
  "name": "뮤지컬 캣츠",
  "price": 50000
}
```

---

### 3.6 광고성 SMS 발송 제한 조회

#### 3.6.1 오늘 광고 SMS 발송 횟수 조회 (REQ-013)
**설명**: 특정 고객의 오늘 광고성 SMS 발송 횟수를 조회합니다.

**입력**:
- custId: 고객 ID
- phoneNumber: 전화번호

**처리 과정**:
1. 오늘 날짜 범위 계산 (00:00 ~ 23:59)
2. QueryDSL로 조건 검색
   - custId 일치
   - phoneNumber 일치
   - smsType = ADVERTISING
   - sendDt가 오늘 범위 내
   - smsResult = SUCCESS
3. 결과 카운트 반환

**출력**: int (발송 횟수)

**예외 상황**: 없음 (0 반환 가능)

---

### 3.7 SMS 유형 및 동의 유형

#### 3.7.1 SMS 유형 (REQ-014)
**SmsType Enum**:
- `INFORMATIONAL("정보성")`: 정보성 문자
- `ADVERTISING("광고성")`: 광고성 문자  
- `VERIFICATION("인증")`: 인증 문자

---

#### 3.7.2 고객 SMS 동의 유형 (REQ-015)
**CustSmsConsentType Enum**:
- `ALL_ALLOW("전체 허용")`: 모든 SMS 수신 가능
- `ALL_DENY("전체 거부")`: 인증 문자만 수신 가능
- `ADVERTISE_DENY("광고 거부")`: 정보성, 인증 문자만 수신 가능

---

## 4. 비기능 요구사항

### 4.1 성능 요구사항
- **NREQ-001**: 대량 SMS 발송 시 스트림 처리를 통한 메모리 효율성 확보
- **NREQ-002**: 템플릿 변수 바인딩 시 외부 서비스 캐싱을 통한 성능 최적화 (향후 개선)
- **NREQ-003**: QueryDSL을 활용한 복잡한 조회 쿼리 최적화
- **NREQ-004**: SMS 발송 이력 조회 시 인덱스 활용

### 4.2 안정성 요구사항
- **NREQ-005**: 트랜잭션 처리를 통한 데이터 일관성 보장
- **NREQ-006**: 필터링 실패 시에도 SMS 객체는 저장 (결과만 실패로 표시)
- **NREQ-007**: 외부 서비스 호출 실패 시 예외 처리 및 로그 기록
- **NREQ-008**: 조회 메서드는 `@Transactional(readOnly = true)` 적용

### 4.3 확장성 요구사항
- **NREQ-009**: 새로운 SMS 유형 추가 시 코드 변경 최소화 (Enum 추가만)
- **NREQ-010**: 새로운 필터링 규칙 추가 시 기존 코드에 영향 없음
- **NREQ-011**: 새로운 템플릿 변수 유형 추가 시 VariableBinder 인터페이스 확장
- **NREQ-012**: 환경별 설정을 통한 필터링 규칙 적용 (`@Profile` 활용)

### 4.4 유지보수성 요구사항
- **NREQ-013**: MapStruct를 활용한 DTO 변환의 자동화
- **NREQ-014**: Enum을 활용한 타입 안전성 보장
- **NREQ-015**: 유틸리티 클래스(SmsContentUtil)를 통한 공통 로직 재사용
- **NREQ-016**: 정규표현식 패턴 상수화를 통한 유지보수성 향상

### 4.5 데이터 요구사항
- **NREQ-017**: 모든 날짜/시간은 `LocalDateTime`으로 저장
- **NREQ-018**: 복합키(SmsTmpltVarRelId)를 통한 다대다 관계 표현
- **NREQ-019**: 연관 관계는 지연 로딩(LAZY) 전략 사용

---

## 5. 데이터베이스 스키마

### 5.1 Sms 테이블
```sql
CREATE TABLE sms (
    sms_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cust_id BIGINT NOT NULL,
    send_phone_number VARCHAR(20) NOT NULL,
    sms_content TEXT NOT NULL,
    send_dt TIMESTAMP NOT NULL,
    sms_type VARCHAR(50) NOT NULL,
    sms_result VARCHAR(50) NOT NULL,
    INDEX idx_cust_send_dt (cust_id, send_dt),
    INDEX idx_phone_send_dt (send_phone_number, send_dt)
);
```

### 5.2 SmsTemplate 테이블
```sql
CREATE TABLE sms_template (
    sms_template_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_content TEXT NOT NULL,
    sms_type VARCHAR(50) NOT NULL
);
```

### 5.3 TemplateVariable 테이블
```sql
CREATE TABLE template_variable (
    tmplt_var_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ko_text VARCHAR(100) NOT NULL UNIQUE,
    en_text VARCHAR(100) NOT NULL,
    variable_type VARCHAR(50) NOT NULL
);
```

### 5.4 SmsTmpltVarRel 테이블 (다대다 중간 테이블)
```sql
CREATE TABLE sms_tmplt_var_rel (
    sms_template_id BIGINT NOT NULL,
    tmplt_var_id BIGINT NOT NULL,
    PRIMARY KEY (sms_template_id, tmplt_var_id),
    FOREIGN KEY (sms_template_id) REFERENCES sms_template(sms_template_id),
    FOREIGN KEY (tmplt_var_id) REFERENCES template_variable(tmplt_var_id)
);
```

---

## 6. 제약사항

### 6.1 기술적 제약사항
- Spring Boot 3.x 기반
- JPA/Hibernate ORM 사용
- QueryDSL을 통한 복잡한 쿼리 처리
- MapStruct 매퍼 사용
- 스트림 API를 통한 함수형 프로그래밍
- RestTemplate을 통한 외부 서비스 연동

### 6.2 비즈니스 제약사항
- 템플릿 변수는 사전 정의된 것만 사용 가능
- SMS 유형별 발송 제한 규칙 준수
- 고객 동의 상태에 따른 발송 제한
- 광고성 SMS는 고객당 하루 2건 제한
- 인증 문자는 모든 제한 규칙 예외
- 운영 환경에서는 오전 8시 ~ 오후 8시만 발송 가능

### 6.3 외부 의존성
- cust-service: 고객 정보 조회
- booking-service: 공연 정보 조회
- 외부 서비스 가용성에 따른 영향 존재

---

## 7. 검증 방법

### 7.1 단위 테스트
- 각 서비스 클래스별 독립적인 단위 테스트
- Mock을 사용한 의존성 격리
- SmsFilter 각 필터별 단위 테스트
- VariableBinder 각 바인더별 단위 테스트
- 테스트 커버리지 90% 이상 목표

### 7.2 통합 테스트
- API 레벨 통합 테스트
- 데이터베이스 연동 테스트
- 외부 서비스 연동 테스트 (MockServer 활용)
- 전체 워크플로우 테스트 (템플릿 생성 → 변수 바인딩 → SMS 발송)

### 7.3 시나리오 테스트
- 정상 발송 시나리오
- 시간 제한 시나리오 (야간 발송 시도)
- 고객 거절 시나리오 (동의 거부 고객)
- 광고 횟수 초과 시나리오

---

## 8. 향후 개선 사항

### 8.1 기능 개선
- [ ] 템플릿 미리보기 기능
- [ ] SMS 발송 예약 기능 (특정 시간에 발송)
- [ ] SMS 발송 취소 기능
- [ ] 대량 발송 시 배치 처리
- [ ] SMS 발송 통계 대시보드

### 8.2 성능 개선
- [ ] 외부 서비스 호출 캐싱 전략 도입
- [ ] 비동기 SMS 발송 처리
- [ ] 페이징 처리 (발송 이력 조회)
- [ ] 인덱스 최적화

### 8.3 보안 개선
- [ ] API 인증/인가 처리
- [ ] 전화번호 암호화 저장
- [ ] 민감 정보 마스킹 (로그)
- [ ] Rate Limiting (API 호출 제한)

### 8.4 운영 개선
- [ ] SMS 발송 실패 재시도 로직
- [ ] 발송 실패 알림 기능
- [ ] 모니터링 및 알람 시스템 구축
- [ ] 로그 레벨별 관리

---

**문서 버전**: 3.0  
**작성일**: 2025년 10월 6일  
**최종 수정일**: 2025년 10월 6일  
**기반 코드**: sms-service 모듈 실제 구현체 완전 분석