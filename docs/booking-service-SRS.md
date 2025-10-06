# Booking Service 모듈 소프트웨어 요구사항 명세서 (SRS)

## 1. 개요

### 1.1 목적
이 문서는 OO-SMS 시스템의 booking-service 모듈에 대한 소프트웨어 요구사항을 정의합니다. booking-service는 공연 예약 및 예약 항목 관리를 담당하는 핵심 서비스입니다.

### 1.2 범위
- 예약 항목(Item) 관리 (생성, 조회, 수정, 검색)
- 예약(Booking) 생성 및 취소
- 예약 조회 및 검색
- 재고 관리 (자동 증감)
- QueryDSL을 활용한 동적 검색

---

## 2. 시스템 구조

### 2.1 아키텍처 계층
```
┌─────────────────────────────────┐
│   API Layer (Controller)        │
│   - ItemApiController           │
│   - BookingApiController        │
├─────────────────────────────────┤
│   Service Layer                 │
│   - ItemService                 │
│   - BookingService              │
├─────────────────────────────────┤
│   Repository Layer              │
│   - JpaItemRepository           │
│   - JpaBookingRepository        │
│   - QueryDSL (Custom Query)     │
└─────────────────────────────────┘
```

### 2.2 주요 컴포넌트
- **ItemService**: 예약 항목 관리 서비스 (CRUD, 중복 검증)
- **BookingService**: 예약 생성/취소 서비스
- **ItemMapper**: Item 엔티티-DTO 변환
- **BookingMapper**: Booking 엔티티-DTO 변환
- **ItemQueryDslImpl**: Item 동적 검색 구현체
- **BookingQueryDslImpl**: Booking 동적 검색 구현체

### 2.3 도메인 엔티티
- **Item**: 예약 가능한 공연 항목 (콘서트, 뮤지컬 등)
- **Booking**: 고객의 예약 정보
- **Category**: 공연 카테고리 (계층 구조 지원)
- **Seat**: 좌석 정보 (향후 구현 예정)
- **BookingStatus**: 예약 상태 (BOOK, CANCEL)
- **Concert**: 콘서트 상세 정보 (Item 상속)

---

## 3. 기능 요구사항

### 3.1 예약 항목(Item) 관리

#### 3.1.1 예약 항목 생성 (REQ-001)
**설명**: 새로운 예약 항목을 생성합니다.

**입력**:
```json
{
  "name": "뮤지컬 캣츠",
  "price": 50000,
  "stockQuantity": 100
}
```

**처리 과정**:
1. 중복 검증: `ItemService.validateDuplication()` 호출
   - ItemSearch로 동일한 이름과 가격 조회
   - 중복 존재 시 `IllegalStateException` 발생
2. Item 엔티티 저장
3. 생성된 ID 반환

**출력**: Long (생성된 Item ID)

**예외 상황**:
- 중복된 공연: `IllegalStateException` - "중복된 공연을 저장할 수 없습니다"
- null 입력: `NullPointerException`
- 유효하지 않은 값: 도메인 객체에서 검증

**API 엔드포인트**: `POST /api/items`

---

#### 3.1.2 예약 항목 전체 조회 (REQ-002)
**설명**: 등록된 모든 예약 항목을 조회합니다.

**입력**: 없음

**처리 과정**:
1. JpaItemRepository.findAll() 호출
2. ItemMapper로 DTO 변환
3. 리스트 반환

**출력**: List<ItemGetResponseDto>
```json
[
  {
    "id": 1,
    "name": "뮤지컬 캣츠",
    "price": 50000,
    "stockQuantity": 100
  }
]
```

**예외 상황**: 없음 (빈 리스트 반환 가능)

**API 엔드포인트**: `GET /api/items`

---

#### 3.1.3 예약 항목 단건 조회 (REQ-003)
**설명**: 특정 ID의 예약 항목을 조회합니다.

**입력**: id (Long, Path Variable)

**처리 과정**:
1. JpaItemRepository.findById() 호출
2. Optional 처리 (없으면 예외)
3. ItemMapper로 DTO 변환

**출력**: ItemGetResponseDto

**예외 상황**:
- 존재하지 않는 ID: `IllegalArgumentException` - "해당 공연이 없습니다."

**API 엔드포인트**: `GET /api/items/{id}`

---

#### 3.1.4 예약 항목 수정 (REQ-004)
**설명**: 기존 예약 항목의 정보를 수정합니다.

**입력**:
```json
{
  "id": 1,
  "name": "뮤지컬 캣츠 (수정)",
  "price": 60000,
  "stockQuantity": 80
}
```

**처리 과정**:
1. ID로 기존 Item 조회
2. Item이 없으면 예외 발생
3. `item.update()` 메서드로 변경 감지(Dirty Checking) 수행
4. 트랜잭션 커밋 시 자동 업데이트

**출력**: Long (수정된 Item ID)

**예외 상황**:
- 존재하지 않는 ID: `IllegalArgumentException` - "해당 공연이 없습니다."

**API 엔드포인트**: `POST /api/items/{id}`

---

#### 3.1.5 예약 항목 동적 검색 (REQ-005)
**설명**: 이름과 가격으로 예약 항목을 동적 검색합니다. (QueryDSL 사용)

**입력**:
- ItemSearch 객체
  - name: 공연명 (선택, null 가능)
  - price: 가격 (필수)

**처리 과정**:
1. BooleanBuilder로 동적 쿼리 생성
2. name이 null이 아니면: `item.name.eq(name)` AND 조건 추가
3. price는 필수: `item.price.eq(price)` 조건 추가
4. limit(100) 제한
5. QueryDSL fetch() 실행

**출력**: List<Item> (최대 100개)

**예외 상황**: 없음 (빈 리스트 반환 가능)

**API 엔드포인트**: 내부 사용 (Service Layer)

---

### 3.2 예약(Booking) 관리

#### 3.2.1 예약 생성 (REQ-006)
**설명**: 새로운 예약을 생성하고 재고를 자동으로 감소시킵니다.

**입력**:
```json
{
  "custId": 1,
  "itemId": 1,
  "count": 2
}
```

**처리 과정**:
1. itemId로 Item 조회
   - Item이 없으면 예외 발생
2. `Booking.createBooking()` 정적 팩토리 메서드 호출
   - custId, item, count 전달
   - 예약 상태: `BookingStatus.BOOK`
   - 예약 시간: `LocalDateTime.now()`
   - **재고 감소**: `item.removeStock(count)` 자동 호출
3. Booking 저장
4. 생성된 Booking ID 반환

**출력**: Long (생성된 Booking ID)

**예외 상황**:
- 존재하지 않는 Item: `IllegalArgumentException` - "해당 공연이 없습니다: {itemId}"
- 재고 부족: `IllegalArgumentException` - "need more stock"

**API 엔드포인트**: `POST /api/bookings`

---

#### 3.2.2 예약 취소 (REQ-007)
**설명**: 기존 예약을 취소하고 재고를 자동으로 복구합니다.

**입력**: bookingId (Long, Path Variable)

**처리 과정**:
1. bookingId로 Booking 조회
   - Booking이 없으면 예외 발생
2. `booking.cancel()` 메서드 호출
   - 예약 상태: `BookingStatus.CANCEL`로 변경
   - **재고 복구**: `item.addStock(count)` 자동 호출
3. 변경 감지로 자동 업데이트
4. Booking ID 반환

**출력**: Long (취소된 Booking ID)

**예외 상황**:
- 존재하지 않는 Booking: `IllegalArgumentException` - "해당 예약이 없습니다: {bookingId}"
- TODO: 공연 일주일 전 취소 불가 (향후 구현 예정)

**API 엔드포인트**: `POST /api/bookings/{id}/cancel`

---

#### 3.2.3 예약 조건 검색 (REQ-008)
**설명**: 조건에 맞는 예약 목록을 조회합니다. (QueryDSL 사용)

**입력**: BookingSearch 객체 (Query Parameter)
- custId: 고객 ID (선택)
- status: 예약 상태 (선택)
- 기타 동적 조건

**처리 과정**:
1. BookingQueryDslImpl.findAll() 호출
2. 동적 쿼리로 조건 검색
3. BookingMapper로 DTO 변환

**출력**: List<BookingListResponseDto>
```json
[
  {
    "bookId": 1,
    "custName": "홍길동",
    "itemName": "뮤지컬 캣츠",
    "bookingStatus": "BOOK"
  }
]
```

**예외 상황**: 없음 (빈 리스트 반환 가능)

**API 엔드포인트**: `GET /api/bookings/search`

---

### 3.3 재고 관리

#### 3.3.1 재고 증가 (REQ-009)
**설명**: Item의 재고를 증가시킵니다. (주로 예약 취소 시 사용)

**입력**: quantity (int, 증가할 수량)

**처리 과정**:
```java
public void addStock(int quantity) {
    this.stockQuantity += quantity;
}
```

**출력**: void (엔티티 상태 변경)

**예외 상황**: 없음

---

#### 3.3.2 재고 감소 (REQ-010)
**설명**: Item의 재고를 감소시킵니다. (주로 예약 생성 시 사용)

**입력**: quantity (int, 감소할 수량)

**처리 과정**:
```java
public void removeStock(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0) {
        throw new IllegalArgumentException("need more stock");
    }
    this.stockQuantity = restStock;
}
```

**출력**: void (엔티티 상태 변경)

**예외 상황**:
- 재고 부족: `IllegalArgumentException` - "need more stock"
- TODO: 커스텀 예외 `NotEnoughStockException` 생성 예정

---

### 3.4 예약 상태 관리

#### 3.4.1 BookingStatus Enum (REQ-011)
**예약 상태 종류**:
- `BOOK("예매 완료")`: 정상 예약 상태
- `CANCEL("예매 취소")`: 취소된 예약 상태

**향후 추가 예정**:
- `FAIL_CANCEL`: 취소 불가 상태 (공연 일주일 전 등)

---

### 3.5 카테고리 관리

#### 3.5.1 계층형 카테고리 구조 (REQ-012)
**설명**: 공연 카테고리의 계층 구조를 지원합니다.

**구조**:
- 부모-자식 관계 (self-referencing)
- 다대다 관계: Category ↔ Item

**주요 메서드**:
```java
public void addChildCategory(Category child) {
    this.child.add(child);
    child.setParent(this);
}
```

---

### 3.6 총 예약 가격 계산 (REQ-013)
**설명**: 예약의 총 가격을 계산합니다.

**계산식**: `총 가격 = 예약 수량 × 항목 가격`

**처리 과정**:
```java
public int getTotalPrice() {
    return count * item.getPrice();
}
```

**출력**: int (총 가격)

---

## 4. 비기능 요구사항

### 4.1 성능 요구사항
- **NREQ-001**: 단일 예약 항목 조회는 100ms 이내에 응답해야 합니다.
- **NREQ-002**: 예약 생성은 500ms 이내에 완료되어야 합니다.
- **NREQ-003**: 예약 항목 검색 시 최대 100개로 결과를 제한합니다.
- **NREQ-004**: QueryDSL을 활용한 동적 쿼리 최적화

### 4.2 안정성 요구사항
- **NREQ-005**: 조회 메서드는 `@Transactional(readOnly = true)` 적용
- **NREQ-006**: 생성/수정/삭제 메서드는 `@Transactional` 적용
- **NREQ-007**: 예약 생성/취소 시 재고 변경은 원자적으로 처리
- **NREQ-008**: Dirty Checking을 활용한 안전한 엔티티 업데이트

### 4.3 확장성 요구사항
- **NREQ-009**: 상속 전략(SINGLE_TABLE)을 통한 Item 확장 지원
- **NREQ-010**: QueryDSL을 통한 복잡한 검색 요구사항 대응
- **NREQ-011**: MapStruct를 활용한 DTO 변환 자동화

### 4.4 데이터 요구사항
- **NREQ-012**: 모든 날짜/시간은 `LocalDateTime`으로 저장
- **NREQ-013**: 예약 상태는 Enum으로 관리하여 타입 안정성 보장
- **NREQ-014**: 연관 관계는 지연 로딩(LAZY) 전략 사용
- **NREQ-015**: 양방향 연관관계 사용 시 연관관계 편의 메서드 제공

### 4.5 유지보수성 요구사항
- **NREQ-016**: 정적 팩토리 메서드를 통한 객체 생성 캡슐화
- **NREQ-017**: Builder 패턴을 활용한 복잡한 객체 생성
- **NREQ-018**: 도메인 로직을 엔티티 내부에 캡슐화

---

## 5. 데이터베이스 스키마

### 5.1 Item 테이블
```sql
CREATE TABLE item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    stock_quantity INT NOT NULL,
    dtype VARCHAR(31)  -- 상속 구분자 (SINGLE_TABLE 전략)
);
```

### 5.2 Booking 테이블
```sql
CREATE TABLE booking (
    booking_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cust_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    count INT NOT NULL,
    book_dt TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item(item_id)
);
```

### 5.3 Category 테이블
```sql
CREATE TABLE category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES category(category_id)
);
```

### 5.4 category_item 테이블 (다대다 중간 테이블)
```sql
CREATE TABLE category_item (
    category_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, item_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (item_id) REFERENCES item(item_id)
);
```

### 5.5 Seat 테이블 (향후 구현 예정)
```sql
CREATE TABLE seat (
    seat_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    seat_number VARCHAR(255),
    grade VARCHAR(50),
    seat_price INT
);
```

---

## 6. 제약사항

### 6.1 기술적 제약사항
- Spring Boot 3.x 기반
- JPA/Hibernate ORM 사용
- QueryDSL을 통한 동적 쿼리 처리
- MapStruct 매퍼 사용
- 상속 전략: SINGLE_TABLE

### 6.2 비즈니스 제약사항
- 동일한 이름과 가격을 가진 항목은 중복 저장 불가
- 재고가 0 미만이 되는 예약 불가
- 예약 취소 시 자동으로 재고 복구
- Seat(좌석) 기능은 향후 구현 예정
- 예약 취소 시점 제한은 향후 구현 예정

### 6.3 외부 의존성
- custId는 cust-service에서 관리하는 유효한 고객 ID
- 고객 정보 검증은 별도 서비스에서 수행

---

## 7. 검증 방법

### 7.1 단위 테스트 (Mock 테스트)
- **Service 계층**: Mock을 사용한 독립적인 단위 테스트
  - ItemServiceTest: saveItem, findAll, findById, updateItem
  - BookingServiceTest: book, cancelBooking, findBooking
- **테스트 커버리지**: 90% 이상 목표

### 7.2 통합 테스트 (@DataJpaTest)
- **Repository 계층**: 실제 DB 연동 테스트
  - ItemQueryDslImplTest: 동적 검색 로직 검증
  - BookingQueryDslImplTest: 동적 검색 로직 검증
- **H2 인메모리 DB 사용**

### 7.3 API 통합 테스트
- API 레벨 통합 테스트
- 전체 워크플로우 테스트
- 예약 생성 → 재고 감소 → 예약 취소 → 재고 복구 시나리오

---

## 8. 향후 개선 사항

### 8.1 기능 개선
- [ ] Seat(좌석) 기능 구현
- [ ] 공연 일주일 전 취소 불가 로직 구현
- [ ] 예약 상태에 `FAIL_CANCEL` 추가
- [ ] NotEnoughStockException 커스텀 예외 생성
- [ ] 예약 검색 시 페이징 처리

### 8.2 성능 개선
- [ ] 캐싱 전략 도입 (자주 조회되는 공연 정보)
- [ ] 동시성 제어를 위한 낙관적 락(Optimistic Lock) 도입
- [ ] N+1 문제 해결을 위한 fetch join 최적화

### 8.3 보안 개선
- [ ] API 인증/인가 처리
- [ ] custId 검증 로직 추가
- [ ] 입력 값 검증 강화 (Validation 어노테이션)

---

**문서 버전**: 1.0  
**작성일**: 2025년 10월 6일  
**기반 코드**: booking-service 모듈 실제 구현체 분석