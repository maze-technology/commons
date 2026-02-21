package tech.maze.commons.pagination;

/**
 * Helpers to normalize pagination inputs from external requests.
 */
public final class PaginationUtils {
  private PaginationUtils() {}

  /**
   * Normalizes raw pagination values and clamps them to int bounds.
   *
   * @param page requested page (0-based)
   * @param limit requested page size
   * @param defaultLimit fallback limit when input is invalid
   * @return normalized pagination values
   */
  public static Pagination normalize(long page, long limit, int defaultLimit) {
    final long sanitizedPage = Math.max(0L, page);
    final long sanitizedLimit = Math.max(1L, limit > 0 ? limit : defaultLimit);

    return new Pagination(
        longToIntClamped(sanitizedPage),
        longToIntClamped(sanitizedLimit)
    );
  }

  private static int longToIntClamped(long value) {
    return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
  }
}
