# OO-SMS TDD 구현 템플릿

## 현재 작업: ItemService.saveItem()

## 1. **SRS(소프트웨어 요구사항 명세서) 작성**

### ItemService.saveItem() 요구사항
- **기능**: Item(공연) 정보를 데이터베이스에 저장
- **입력**: Item 객체 (name, price, stockQuantity 포함)
- **출력**: 저장된 Item의 ID (Long 타입)
- **제약사항**:
  - Item 객체는 null이 아니어야 함
  - name은 비어있지 않아야 함
  - price는 0 이상이어야 함
  - stockQuantity는 0 이상이어야 함
- **부수효과**: 
  - JpaItemRepository를 통해 Item이 영속화됨
  - 트랜잭션이 커밋되면 데이터베이스에 저장됨

## 2. **SRS를 잘 설명할 수 있는 예시 목록 작성**

### 정상 케이스
- **유효한 Item 저장**
  - 입력: Item("뮤지컬 캣츠", 50000, 100)
  - 기대 결과: Item이 저장되고 생성된 ID 반환 (예: 1L)

- **최소값으로 Item 저장**
  - 입력: Item("공연명", 0, 0)
  - 기대 결과: Item이 저장되고 생성된 ID 반환

### 예외 케이스
- **null Item 저장 시도**
  - 입력: null
  - 기대 결과: NullPointerException 또는 적절한 예외

- **빈 이름으로 Item 저장**
  - 입력: Item("", 10000, 50)
  - 기대 결과: 유효성 검증 실패 (도메인 객체에서 검증)

- **음수 가격으로 Item 저장**
  - 입력: Item("공연명", -1000, 50)
  - 기대 결과: 유효성 검증 실패

- **음수 재고로 Item 저장**
  - 입력: Item("공연명", 10000, -10)
  - 기대 결과: 유효성 검증 실패

## 3. **테스트 케이스 목록 작성**

### ItemServiceTest
1. ✅ `saveItem_ValidItem_ReturnsId` - 유효한 Item 저장 시 ID 반환
2. ✅ `saveItem_ValidItem_ItemIsPersisted` - 저장 후 조회 가능 확인
3. ✅ `saveItem_MinimumValues_ReturnsId` - 최소값(0, 0) Item 저장 성공
4. ✅ `saveItem_NullItem_ThrowsException` - null 입력 시 예외 발생
5. ✅ `saveItem_DuplicateItem_SavesSuccessfully` - 중복 Item도 저장 가능 (ID만 다름)

### findAll() 테스트
6. ✅ `findAll_WithItems_ReturnsAllItems` - Item이 있을 때 모든 Item 반환
7. ✅ `findAll_EmptyRepository_ReturnsEmptyList` - Item이 없을 때 빈 리스트 반환
8. ✅ `findAll_MultipleItems_ReturnsCorrectOrder` - 여러 Item이 있을 때 순서 확인

### findById() 테스트
9. ✅ `findById_ExistingId_ReturnsItem` - 존재하는 ID로 조회 시 Item 반환
10. ✅ `findById_NonExistingId_ThrowsException` - 존재하지 않는 ID로 조회 시 예외 발생
11. ✅ `findById_ValidId_ReturnsMappedDto` - DTO 매핑 확인

### updateItem() 테스트
12. 🔄 `updateItem_ExistingItem_UpdatesSuccessfully` - 존재하는 Item 업데이트 성공
13. ⬜ `updateItem_NonExistingItem_ThrowsException` - 존재하지 않는 Item 업데이트 시 예외
14. ⬜ `updateItem_ValidData_ReturnsUpdatedId` - 업데이트 후 ID 반환
15. ⬜ `updateItem_PartialUpdate_UpdatesOnlySpecifiedFields` - 일부 필드만 업데이트

### ItemTest (도메인 객체 테스트 - 별도 작성 필요)
1. ⬜ `createItem_EmptyName_ThrowsException` - 빈 이름 검증
2. ⬜ `createItem_NegativePrice_ThrowsException` - 음수 가격 검증
3. ⬜ `createItem_NegativeStock_ThrowsException` - 음수 재고 검증

## 4. **테스트 선택 및 구현(더 이상 추가할 테스트가 없을 때 까지)**

### 현재 진행 상황
- [ ] 첫 번째 테스트 작성 대기: `saveItem_ValidItem_ReturnsId`

---

## 다음 단계
1. ItemServiceTest 클래스 생성
2. 첫 번째 테스트 케이스 작성
3. 테스트 실행 (실패 확인)
4. 구현 코드 작성 (이미 존재)
5. 테스트 실행 (성공 확인)
