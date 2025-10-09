# 멀티모듈 전환기 일지

## 9월 일지
<details markdwon="1">
<summary> 접기/펼치기 </summary>

### 9/18
<details markdown="1">
<summary> <b>완료한 일</b> </summary>

- cust-service 옮기기 완료
</details>

### 9/19

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* sms-service 옮기는 중 ..
* smsMapper, smsTemplateMapper 만들고.. common.dto에 있는 entity 코드 걷어내기 힘들구만
* cmd+shift+r 사용해서 import 패키지값 모두 변경하기...
</details>

### 9/22

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* sms-service queryDsl 의존성 라이브러리 설치
</details>

### 9/23

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* sms-service 운영코드 이동 완료
* 테스트 커버리지 향상을 위해 테스트 코드 추가 중...
  * 명령어 67% 브랜치 76%
</details>

### 9/24

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* 클로드 인텔리제이 MCP를 활용해 sms service 테스트 코드 작성 완료
</details>

</details>

---

## 10월 일지
<details markdwon="1">
<summary> 접기/펼치기 </summary>

### 10/6

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* view-service 소스코드 옮기기 완료
* MSA구조 말고 모놀리식 단일 jar로 빌드하기 
  * 동일 이름 빈 충돌 -> 각 서브 도메인 앞에 붙이기
  * jpa auditing 클래스 문제
  * @SpringBootApplication 어노테이션 충돌 문제 
  * /view 경로 제거 
  * 공연 금액 ',' 자동 넣기 , 전화번호 '-' 하이픈 제거 및 출력시 보여주기
</details>

### 10/7

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* 자동로드 devtools 라이브러리 설치
* style : 금액 "," 자동으로 넣기
* 전화번호 포맷, 공연예약일시 포맷 출력 변경
</details>

### 10/8

<details markdown="1">
<summary> <b>완료한 일</b> </summary>

* sms 템플릿 서비스 수정 및 삭제 메서드 -> 테스트코드 Mock으로 작성
* 템플릿 변수 서비스에서 수정 및 삭제 메서드 추가
  * 해당 테스트코드 Mock으로 작성
* sms 템플릿 컨트롤러 수정 및 삭제 API 생성
* 템플릿 변수 컨트롤러 수정 및 삭제 API 생성
</details>

</details>

### 해야할 일

- sms 발송내역 검색 조건
- jpa auditing
- swagger
- @valid 컨트롤러 필수값 검증
- 예외 핸들링해서 alert 창 띄우기
- 현재 모놀리식으로 변경했으니 고객명도 queryDsl 해서 보여주기


---


