$version: "2"

namespace tech.maze.dtos.commons.time

use alloy.proto#protoEnabled

@protoEnabled
structure Duration {
    @required
    seconds: Long

    @required
    nanos: Integer
}
