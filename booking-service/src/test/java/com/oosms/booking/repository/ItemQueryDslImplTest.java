package com.oosms.booking.repository;

import com.oosms.booking.domain.Item;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("ItemQueryDslImpl 테스트")
class ItemQueryDslImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ItemQueryDslImpl itemRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        Item item1 = new Item();
        item1.setName("뮤지컬 캣츠");
        item1.setPrice(50000);
        item1.setStockQuantity(100);
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setName("오페라의 유령");
        item2.setPrice(70000);
        item2.setStockQuantity(50);
        entityManager.persist(item2);

        Item item3 = new Item();
        item3.setName("레미제라블");
        item3.setPrice(50000);
        item3.setStockQuantity(80);
        entityManager.persist(item3);

        Item item4 = new Item();
        item4.setName("시카고");
        item4.setPrice(60000);
        item4.setStockQuantity(120);
        entityManager.persist(item4);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("이름으로 Item을 검색할 수 있다")
    void findBySearch_WithName_ReturnsMatchingItems() {
        // given
        ItemSearch search = new ItemSearch();
        search.setName("뮤지컬 캣츠");
        search.setPrice(50000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("뮤지컬 캣츠");
        assertThat(result.get(0).getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("가격으로 Item을 검색할 수 있다")
    void findBySearch_WithPrice_ReturnsMatchingItems() {
        // given
        ItemSearch search = new ItemSearch();
        search.setPrice(50000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Item::getPrice)
                .containsOnly(50000);
    }

    @Test
    @DisplayName("이름과 가격으로 Item을 검색할 수 있다")
    void findBySearch_WithNameAndPrice_ReturnsMatchingItems() {
        // given
        ItemSearch search = new ItemSearch();
        search.setName("레미제라블");
        search.setPrice(50000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("레미제라블");
        assertThat(result.get(0).getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("이름이 null이면 가격으로만 검색한다")
    void findBySearch_WithNullName_SearchesByPriceOnly() {
        // given
        ItemSearch search = new ItemSearch();
        search.setName(null);
        search.setPrice(70000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("오페라의 유령");
        assertThat(result.get(0).getPrice()).isEqualTo(70000);
    }

    @Test
    @DisplayName("매칭되는 Item이 없으면 빈 리스트를 반환한다")
    void findBySearch_NoMatch_ReturnsEmptyList() {
        // given
        ItemSearch search = new ItemSearch();
        search.setName("존재하지 않는 공연");
        search.setPrice(99999);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("검색 결과는 최대 100개로 제한된다")
    void findBySearch_ManyResults_LimitsTo100() {
        // given
        // 100개 이상의 동일 가격 Item 생성
        for (int i = 0; i < 105; i++) {
            Item item = new Item();
            item.setName("테스트 공연 " + i);
            item.setPrice(10000);
            item.setStockQuantity(50);
            entityManager.persist(item);
        }
        entityManager.flush();
        entityManager.clear();

        ItemSearch search = new ItemSearch();
        search.setPrice(10000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSizeLessThanOrEqualTo(100);
    }

    @Test
    @DisplayName("존재하는 이름과 존재하지 않는 가격 조합은 빈 결과를 반환한다")
    void findBySearch_ExistingNameWithWrongPrice_ReturnsEmptyList() {
        // given
        ItemSearch search = new ItemSearch();
        search.setName("뮤지컬 캣츠");
        search.setPrice(99999); // 잘못된 가격

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러 Item이 동일한 가격을 가질 때 모두 검색된다")
    void findBySearch_MultipleItemsWithSamePrice_ReturnsAll() {
        // given
        ItemSearch search = new ItemSearch();
        search.setPrice(50000);

        // when
        List<Item> result = itemRepository.findBySearch(search);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Item::getName)
                .containsExactlyInAnyOrder("뮤지컬 캣츠", "레미제라블");
    }
}
