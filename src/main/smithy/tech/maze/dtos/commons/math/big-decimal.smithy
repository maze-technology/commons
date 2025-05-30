$version: "2"

namespace tech.maze.dtos.commons.math

use alloy.proto#protoEnabled

/// Arbitrary-precision decimal number.
///
/// value = unscaledValue × 10^(−scale)
/// Example: 123.45 → unscaledValue = 12345, scale = 2
@protoEnabled
structure BigDecimal {
    /// Integer coefficient (no decimal point).
    @required
    unscaledValue: BigInteger

    /// Digits to the right of the decimal point.
    @required
    scale: Integer
}
