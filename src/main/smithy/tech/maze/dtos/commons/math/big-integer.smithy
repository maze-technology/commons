$version: "2"

namespace tech.maze.dtos.commons.math

use alloy.proto#protoEnabled

/// Arbitrary-precision signed integer.
///
/// Canonical representation: big-endian, two's-complement bytes
/// identical to Java BigInteger.toByteArray().
@protoEnabled
structure BigInteger {
    /// Never anything but big-endian two's-complement.
    @required
    twosComp: Blob
}
