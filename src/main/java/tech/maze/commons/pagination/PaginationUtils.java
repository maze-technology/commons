package tech.maze.commons.pagination;

/**
 * Helpers to normalize pagination inputs from external requests.
 */
public final class PaginationUtils {
  private PaginationUtils() {}

  /**
   * Normalizes raw pagination values.
   *
   * @param page requested page (0-based)
   * @param limit requested page size
   * @param defaultLimit fallback limit when input is invalid
   * @return normalized pagination values
   */
  public static Pagination normalize(int page, int limit, int defaultLimit) {
    final int sanitizedPage = Math.max(0, page);
    final int resolvedDefaultLimit = Math.max(1, defaultLimit);
    final int sanitizedLimit = limit > 0 ? limit : resolvedDefaultLimit;

    return new Pagination(
        sanitizedPage,
        sanitizedLimit
    );
  }
}
