package com.oosms.booking.service;

import com.oosms.booking.domain.Item;
import com.oosms.booking.mapper.ItemMapper;
import com.oosms.booking.repository.ItemSearch;
import com.oosms.booking.repository.JpaItemRepository;
import com.oosms.common.dto.ItemGetResponseDto;
import com.oosms.common.dto.ItemUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService 테스트")
class ItemServiceTest {

    @Mock
    private JpaItemRepository jpaItemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    private Item musicalCats = Item.builder()
            .id(1L)
            .name("뮤지컬 캣츠")
            .price(50000)
            .stockQuantity(100).build();

    private Item musicalPhantom = Item.builder()
            .id(2L)
            .name("뮤지컬 오페라의 유령")
            .price(70000)
            .stockQuantity(200).build();

    @Test
    @DisplayName("유효한 Item 저장 시 ID를 반환한다")
    void saveItem_ValidItem_ReturnsId() {
        // given

        // 중복 검증: 중복 없음
        when(jpaItemRepository.findBySearch(any(ItemSearch.class)))
                .thenReturn(Collections.emptyList());
        
        // Item이 저장될 때 ID가 설정되도록 모킹
        when(jpaItemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedItem, "id" ,1L);
            return savedItem;
        });

        // when
        Long savedId = itemService.saveItem(musicalCats);

        // then
        assertThat(savedId).isNotNull();
        assertThat(savedId).isEqualTo(1L);
        verify(jpaItemRepository).findBySearch(any(ItemSearch.class));
        verify(jpaItemRepository).save(musicalCats);
    }

    @Test
    @DisplayName("저장된 Item은 조회가 가능하다")
    void saveItem_ValidItem_ItemIsPersisted() {
        // given
        // 중복 검증: 중복 없음
        when(jpaItemRepository.findBySearch(any(ItemSearch.class)))
                .thenReturn(Collections.emptyList());
        
        when(jpaItemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedItem, "id" ,1L);
            return savedItem;
        });

        // when
        Long savedId = itemService.saveItem(musicalCats);

        // then
        verify(jpaItemRepository).save(musicalCats);
        assertThat(musicalCats.getId()).isEqualTo(savedId);
        assertThat(musicalCats.getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(musicalCats.getPrice()).isEqualTo(50000);
        assertThat(musicalCats.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("최소값(0, 0)으로 Item 저장이 가능하다")
    void saveItem_MinimumValues_ReturnsId() {
        // given
        // todo 최소값으로 저장하는게 맞나?

        // 중복 검증: 중복 없음
        when(jpaItemRepository.findBySearch(any(ItemSearch.class)))
                .thenReturn(Collections.emptyList());
        
        when(jpaItemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedItem, "id", 2L);
            return savedItem;
        });

        // when
        Long savedId = itemService.saveItem(musicalCats);

        // then
        assertThat(savedId).isNotNull();
        assertThat(savedId).isEqualTo(2L);
        verify(jpaItemRepository).save(musicalCats);
    }

    @Test
    @DisplayName("null Item 저장 시 예외가 발생한다")
    void saveItem_NullItem_ThrowsException() {
        // given
        Item item = null;

        // when & then
        assertThatThrownBy(() -> itemService.saveItem(item))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("중복된 Item 저장 시 예외가 발생한다")
    void saveItem_DuplicateItem_ThrowsException() {
        // given
        Item existingItem = Item.builder()
                .name("뮤지컬 캣츠")
                .price(50000)
                .stockQuantity(100).build();

        Item newItem = Item.builder()
                .name("뮤지컬 캣츠")
                .price(50000)
                .stockQuantity(100).build();
        
        // 중복 검증: 중복 있음
        when(jpaItemRepository.findBySearch(any(ItemSearch.class)))
                .thenReturn(List.of(existingItem));

        // when & then
        assertThatThrownBy(() -> itemService.saveItem(newItem))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 공연을 저장할 수 없습니다");
        
        verify(jpaItemRepository).findBySearch(any(ItemSearch.class));
    }

    // ========== findAll() 테스트 ==========

    @Test
    @DisplayName("Item이 있을 때 모든 Item을 DTO로 반환한다")
    void findAll_WithItems_ReturnsAllItems() {
        // given
        Item item1 = Item.builder()
                .id(1L)
                .name("뮤지컬 캣츠")
                .price(50000)
                .stockQuantity(100).build();

        Item item2 = Item.builder()
                .id(2L)
                .name("오페라의 유령")
                .price(70000)
                .stockQuantity(50).build();

        List<Item> items = Arrays.asList(item1, item2);

        ItemGetResponseDto dto1 = new ItemGetResponseDto();
        dto1.setId(1L);
        dto1.setName("뮤지컬 캣츠");
        dto1.setPrice("50000");
        dto1.setStockQuantity(100);

        ItemGetResponseDto dto2 = new ItemGetResponseDto();
        dto2.setId(2L);
        dto2.setName("오페라의 유령");
        dto2.setPrice("70000");
        dto2.setStockQuantity(50);

        when(jpaItemRepository.findAll()).thenReturn(items);
        when(itemMapper.toDto(item1)).thenReturn(dto1);
        when(itemMapper.toDto(item2)).thenReturn(dto2);

        // when
        List<ItemGetResponseDto> result = itemService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("오페라의 유령");
        verify(jpaItemRepository).findAll();
    }

    @Test
    @DisplayName("Item이 없을 때 빈 리스트를 반환한다")
    void findAll_EmptyRepository_ReturnsEmptyList() {
        // given
        when(jpaItemRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ItemGetResponseDto> result = itemService.findAll();

        // then
        assertThat(result).isEmpty();
        verify(jpaItemRepository).findAll();
    }

    @Test
    @DisplayName("여러 Item이 있을 때 순서대로 반환한다")
    void findAll_MultipleItems_ReturnsCorrectOrder() {
        // given
        Item item1 = Item.builder()
                .id(1L)
                .name("첫번째 공연").build();

        Item item2 = Item.builder()
                .id(2L)
                .name("두번째 공연").build();

        Item item3 = Item.builder()
                .id(3L)
                .name("세번째 공연").build();

        List<Item> items = Arrays.asList(item1, item2, item3);

        ItemGetResponseDto dto1 = new ItemGetResponseDto();
        dto1.setId(1L);
        dto1.setName("첫번째 공연");

        ItemGetResponseDto dto2 = new ItemGetResponseDto();
        dto2.setId(2L);
        dto2.setName("두번째 공연");

        ItemGetResponseDto dto3 = new ItemGetResponseDto();
        dto3.setId(3L);
        dto3.setName("세번째 공연");

        when(jpaItemRepository.findAll()).thenReturn(items);
        when(itemMapper.toDto(item1)).thenReturn(dto1);
        when(itemMapper.toDto(item2)).thenReturn(dto2);
        when(itemMapper.toDto(item3)).thenReturn(dto3);

        // when
        List<ItemGetResponseDto> result = itemService.findAll();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("첫번째 공연");
        assertThat(result.get(1).getName()).isEqualTo("두번째 공연");
        assertThat(result.get(2).getName()).isEqualTo("세번째 공연");
    }

    // ========== findById() 테스트 ==========

    @Test
    @DisplayName("존재하는 ID로 조회 시 Item을 DTO로 반환한다")
    void findById_ExistingId_ReturnsItem() {
        // given
        Long itemId = musicalCats.getId();
        ItemGetResponseDto dto = new ItemGetResponseDto();
        dto.setId(itemId);
        dto.setName("뮤지컬 캣츠");
        dto.setPrice("50000");
        dto.setStockQuantity(100);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(musicalCats));
        when(itemMapper.toDto(musicalCats)).thenReturn(dto);

        // when
        ItemGetResponseDto result = itemService.findById(itemId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(itemId);
        assertThat(result.getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(result.getPrice()).isEqualTo("50000");
        assertThat(result.getStockQuantity()).isEqualTo(100);
        verify(jpaItemRepository).findById(itemId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
    void findById_NonExistingId_ThrowsException() {
        // given
        Long nonExistingId = 999L;
        when(jpaItemRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.findById(nonExistingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 공연이 없습니다");
        
        verify(jpaItemRepository).findById(nonExistingId);
    }

    @Test
    @DisplayName("유효한 ID로 조회 시 올바르게 매핑된 DTO를 반환한다")
    void findById_ValidId_ReturnsMappedDto() {
        // given
        Long itemId = musicalPhantom.getId();

        ItemGetResponseDto dto = new ItemGetResponseDto();
        dto.setId(itemId);
        dto.setName("뮤지컬 오페라의 유령");
        dto.setPrice("70000");
        dto.setStockQuantity(200);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(musicalPhantom));
        when(itemMapper.toDto(musicalPhantom)).thenReturn(dto);

        // when
        ItemGetResponseDto result = itemService.findById(itemId);

        // then
        assertThat(result.getId()).isEqualTo(musicalPhantom.getId());
        assertThat(result.getName()).isEqualTo(musicalPhantom.getName());
        assertThat(result.getPrice()).isEqualTo(String.valueOf(musicalPhantom.getPrice()));
        assertThat(result.getStockQuantity()).isEqualTo(musicalPhantom.getStockQuantity());
        verify(itemMapper).toDto(musicalPhantom);
    }

    // ========== updateItem() 테스트 ==========

    @Test
    @DisplayName("존재하는 Item을 수정하면 업데이트된 ID를 반환한다")
    void updateItem_ExistingItem_ReturnsUpdatedId() {
        // given
        Long itemId = 1L;
        Item item = musicalCats;

        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(itemId);
        requestDto.setName("뮤지컬 캣츠 (수정)");
        requestDto.setPrice(60000);
        requestDto.setStockQuantity(80);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        Long updatedId = itemService.updateItem(requestDto);

        // then
        assertThat(updatedId).isEqualTo(itemId);
        assertThat(item.getName()).isEqualTo("뮤지컬 캣츠 (수정)");
        assertThat(item.getPrice()).isEqualTo(60000);
        assertThat(item.getStockQuantity()).isEqualTo(80);
        verify(jpaItemRepository).findById(itemId);
    }

    @Test
    @DisplayName("존재하지 않는 Item 수정 시 예외가 발생한다")
    void updateItem_NonExistingItem_ThrowsException() {
        // given
        Long nonExistingId = 999L;
        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(nonExistingId);
        requestDto.setName("새로운 이름");
        requestDto.setPrice(50000);
        requestDto.setStockQuantity(100);

        when(jpaItemRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.updateItem(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 공연이 없습니다");
        
        verify(jpaItemRepository).findById(nonExistingId);
    }

    @Test
    @DisplayName("Item의 이름만 수정할 수 있다")
    void updateItem_NameOnly_UpdatesOnlyName() {
        // given
        Long itemId = 1L;
        Item item = musicalCats;
        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(itemId);
        requestDto.setName("새로운 이름");
        requestDto.setPrice(50000);
        requestDto.setStockQuantity(100);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        itemService.updateItem(requestDto);

        // then
        assertThat(item.getName()).isEqualTo("새로운 이름");
        assertThat(item.getPrice()).isEqualTo(50000);
        assertThat(item.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("Item의 가격만 수정할 수 있다")
    void updateItem_PriceOnly_UpdatesOnlyPrice() {
        // given
        Long itemId = 1L;
        Item item = musicalCats;

        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(itemId);
        requestDto.setName("뮤지컬 캣츠");
        requestDto.setPrice(70000);
        requestDto.setStockQuantity(100);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        itemService.updateItem(requestDto);

        // then
        assertThat(item.getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(item.getPrice()).isEqualTo(70000);
        assertThat(item.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("Item의 재고만 수정할 수 있다")
    void updateItem_StockOnly_UpdatesOnlyStock() {
        // given
        Long itemId = 1L;
        Item item = musicalCats;

        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(itemId);
        requestDto.setName("뮤지컬 캣츠");
        requestDto.setPrice(50000);
        requestDto.setStockQuantity(150);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        itemService.updateItem(requestDto);

        // then
        assertThat(item.getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(item.getPrice()).isEqualTo(50000);
        assertThat(item.getStockQuantity()).isEqualTo(150);
    }

    @Test
    @DisplayName("Item의 모든 필드를 수정할 수 있다")
    void updateItem_AllFields_UpdatesAllFields() {
        // given
        Long itemId = 1L;
        Item item = musicalCats;

        ItemUpdateRequestDto requestDto = new ItemUpdateRequestDto();
        requestDto.setId(itemId);
        requestDto.setName("오페라의 유령");
        requestDto.setPrice(80000);
        requestDto.setStockQuantity(50);

        when(jpaItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        itemService.updateItem(requestDto);

        // then
        assertThat(item.getName()).isEqualTo("오페라의 유령");
        assertThat(item.getPrice()).isEqualTo(80000);
        assertThat(item.getStockQuantity()).isEqualTo(50);
    }
}
