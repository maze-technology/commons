package tech.maze.commons.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaginationUtilsTest {
  @Test
  @DisplayName("Should normalize valid pagination values")
  void shouldNormalizeValidPaginationValues() {
    final Pagination pagination = PaginationUtils.normalize(2, 25, 50);

    assertEquals(2, pagination.page());
    assertEquals(25, pagination.limit());
  }

  @Test
  @DisplayName("Should normalize negative page and zero limit")
  void shouldNormalizeNegativePageAndZeroLimit() {
    final Pagination pagination = PaginationUtils.normalize(-5, 0, 50);

    assertEquals(0, pagination.page());
    assertEquals(50, pagination.limit());
  }

  @Test
  @DisplayName("Should fallback to minimum limit when defaultLimit is invalid")
  void shouldFallbackToMinimumLimitWhenDefaultLimitIsInvalid() {
    final Pagination pagination = PaginationUtils.normalize(1, 0, 0);

    assertEquals(1, pagination.page());
    assertEquals(1, pagination.limit());
  }

  @Test
  @DisplayName("Should keep valid positive values")
  void shouldKeepValidPositiveValues() {
    final Pagination pagination = PaginationUtils.normalize(100, 500, 50);

    assertEquals(100, pagination.page());
    assertEquals(500, pagination.limit());
  }
}
