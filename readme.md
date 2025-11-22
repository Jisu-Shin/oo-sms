# OO-SMS 🎭

Spring Boot 기반의 공연 예약 및 SMS 발송 관리 시스템

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시스템 아키텍처](#-시스템-아키텍처)
- [프로젝트 구조](#-프로젝트-구조)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [개발 가이드](#-개발-가이드)

## 🎯 프로젝트 개요

OO-SMS는 공연 예약과 SMS 발송을 통합 관리하는 멀티모듈 Spring Boot 애플리케이션입니다.
공연 예약부터 고객 관리, SMS 템플릿 관리 및 발송까지 전체 프로세스를 지원합니다.

### 핵심 특징

- **멀티모듈 아키텍처**: 도메인별로 분리된 독립적인 서비스 모듈
- **템플릿 기반 SMS**: 재사용 가능한 템플릿과 동적 변수 바인딩
- **SMS 발송 필터링**: 시간, 고객 동의, 광고 횟수 제한 필터링
- **동적 검색**: QueryDSL 기반 유연한 검색 기능

## ✨ 주요 기능

### SMS Service
- 📱 SMS 발송 및 이력 조회
- 📝 템플릿 관리 (생성, 수정, 삭제)
- 🔄 템플릿 변수 동적 바인딩
- 🚦 SMS 발송 필터링 (시간, 동의, 광고 횟수)

### Booking Service
- 🎫 공연 예약 생성 및 취소
- 🎭 예약 항목(공연) 관리
- 📦 실시간 재고 관리
- 🔍 동적 예매 내역 검색

### Customer Service
- 👥 고객 정보 관리
- 📞 고객 SMS 동의 관리

### View Service
- 🖥️ 웹 UI 제공
- 📱 반응형 디자인

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.4.8
- **Language**: Java 17
- **Build Tool**: Gradle 8.14.3
- **ORM**: Spring Data JPA, Hibernate
- **Query**: QueryDSL 5.0.0
- **Database**: H2 (개발/테스트)
- **Mapper**: MapStruct 1.5.5
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Test**: JUnit 5, Jacoco

### Frontend
- Mustache
- HTML/CSS/JavaScript

### DevOps
- Lombok
- Spring Boot DevTools

## 🏗 시스템 아키텍처

```
┌─────────────────────────────────────────────────┐
│              View Service (UI Layer)            │
│                   Mustache                      │
└────────────────┬────────────────────────────────┘
                 │
    ┌────────────┼────────────┬───────────────┐
    │            │            │               │
    ▼            ▼            ▼               ▼
┌─────────┐ ┌─────────┐ ┌──────────┐ ┌──────────┐
│   SMS   │ │ Booking │ │ Customer │ │  Common  │
│ Service │ │ Service │ │ Service  │ │  Module  │
└────┬────┘ └────┬────┘ └────┬─────┘ └────┬─────┘
     │           │           │             │
     └───────────┴───────────┴─────────────┘
                      │
              ┌───────┴───────┐
              │   H2 Database │
              └───────────────┘
```

### 모듈 간 의존성

```
view-service
    ├── sms-service
    │   ├── common
    │   └── cust-service
    ├── booking-service
    │   └── common
    ├── cust-service
    │   └── common
    └── common
```

## 📁 프로젝트 구조

```
oo-sms/
├── common/                      # 공통 모듈
│   ├── dto/                     # 공통 DTO (Request/Response)
│   ├── exception/               # 공통 예외 처리
│   ├── config/                  # 공통 설정
│   └── util/                    # 유틸리티 클래스
│
├── sms-service/                 # SMS 서비스 모듈
│   ├── api/                     # REST API 컨트롤러
│   ├── service/                 # 비즈니스 로직
│   ├── repository/              # 데이터 접근 계층
│   ├── domain/                  # 도메인 엔티티 & Enum
│   ├── mapper/                  # MapStruct 매퍼
│   ├── config/                  # SMS 설정
│   └──  client/                 # 외부 API 호출 클라이언트
│
├── booking-service/             # 예약 서비스 모듈
│   ├── api/                     # REST API 컨트롤러
│   ├── service/                 # 비즈니스 로직
│   ├── repository/              # 데이터 접근 계층
│   ├── domain/                  # 도메인 엔티티 & Enum
│   ├── mapper/                  # MapStruct 매퍼
│   └──  config/                 # Booking 설정
│
├── cust-service/                # 고객 서비스 모듈
│   ├── api/                     # REST API 컨트롤러
│   ├── service/                 # 비즈니스 로직
│   ├── repository/              # 데이터 접근 계층
│   ├── domain/                  # 도메인 엔티티 & Enum
│   ├── mapper/                  # MapStruct 매퍼
│   └──  config/                 # Customer 설정
│
├── view-service/                # 뷰 서비스 모듈
│   ├── controller/              # 웹 컨트롤러
│   ├── client/                  # 각 서비스 API 호출 클라이언트
│   ├── templates/               # Mustache 템플릿
│   └── static/                  # 정적 리소스 (CSS, JS)
│
└── docs/                        # 문서
    ├── sms-service-SRS.md       # SMS 서비스 요구사항 명세서
    ├── booking-service-SRS.md   # 예약 서비스 요구사항 명세서
    ├── sms-service-tdd.md       # SMS 서비스 TDD 문서
    ├── tdd.md                   # TDD 가이드
    └── images/                  # 문서 이미지
```

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Gradle 8.14.3 이상

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/yourusername/oo-sms.git
cd oo-sms
```

2. **프로젝트 빌드**
```bash
./gradlew clean build
```

3. **테스트 실행**
```bash
./gradlew test
```

4. **애플리케이션 실행**
```bash
./gradlew bootRun
```

5. **웹 브라우저에서 접속**
```
http://localhost:8080
```

### H2 데이터베이스 콘솔

개발 중 H2 데이터베이스 콘솔에 접속하여 데이터를 확인할 수 있습니다.

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (비워두기)
```

## 📚 API 문서

애플리케이션 실행 후 Swagger UI를 통해 API 문서를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui.html
```

### 주요 API 엔드포인트

#### SMS Service
- `POST /api/sms` - SMS 발송
- `GET /api/sms` - SMS 이력 조회
- `POST /api/sms-templates` - 템플릿 생성
- `GET /api/sms-templates` - 템플릿 목록 조회
- `PUT /api/sms-templates/{id}` - 템플릿 수정
- `DELETE /api/sms-templates/{id}` - 템플릿 삭제
- `POST /api/template-variables` - 템플릿 변수 생성
- `GET /api/template-variables` - 템플릿 변수 목록 조회

#### Booking Service
- `POST /api/bookings` - 예약 생성
- `POST /api/bookings/{id}/cancel` - 예약 취소
- `GET /api/bookings/search` - 예약 검색
- `POST /api/items` - 공연 항목 생성
- `GET /api/items` - 공연 항목 목록 조회
- `GET /api/items/{id}` - 공연 항목 조회
- `POST /api/items/{id}` - 공연 항목 수정

#### Customer Service
- `POST /api/custs` - 고객 생성
- `GET /api/custs` - 고객 목록 조회
- `GET /api/custs/{id}` - 고객 정보 조회

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## 👥 개발자

- 프로젝트 관리자 - [@Jisu-Shin](https://github.com/Jisu-Shin)

## 🙏 감사의 말

이 프로젝트는 Spring Boot와 JPA를 활용한 멀티모듈 아키텍처 학습을 목적으로 개발되었습니다.

---

⭐️ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!