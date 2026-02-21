package tech.maze.commons.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaginationUtilsTest {
  @Test
  @DisplayName("Should normalize valid pagination values")
  void shouldNormalizeValidPaginationValues() {
    final Pagination pagination = PaginationUtils.normalize(2L, 25L, 50);

    assertEquals(2, pagination.page());
    assertEquals(25, pagination.limit());
  }

  @Test
  @DisplayName("Should normalize negative page and zero limit")
  void shouldNormalizeNegativePageAndZeroLimit() {
    final Pagination pagination = PaginationUtils.normalize(-5L, 0L, 50);

    assertEquals(0, pagination.page());
    assertEquals(50, pagination.limit());
  }

  @Test
  @DisplayName("Should fallback to minimum limit when defaultLimit is invalid")
  void shouldFallbackToMinimumLimitWhenDefaultLimitIsInvalid() {
    final Pagination pagination = PaginationUtils.normalize(1L, 0L, 0);

    assertEquals(1, pagination.page());
    assertEquals(1, pagination.limit());
  }

  @Test
  @DisplayName("Should clamp very large values to Integer.MAX_VALUE")
  void shouldClampVeryLargeValues() {
    final Pagination pagination = PaginationUtils.normalize(Long.MAX_VALUE, Long.MAX_VALUE, 50);

    assertEquals(Integer.MAX_VALUE, pagination.page());
    assertEquals(Integer.MAX_VALUE, pagination.limit());
  }
}
