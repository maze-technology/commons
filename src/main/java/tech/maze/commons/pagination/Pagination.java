package tech.maze.commons.pagination;

/**
 * Normalized pagination bounds for repository queries.
 */
public record Pagination(
    int page,
    int limit
) {}
