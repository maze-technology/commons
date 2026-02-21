package tech.maze.commons.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.protobuf.Value;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProtobufValueMapperTest {
  private final ProtobufValueMapper mapper = Mappers.getMapper(ProtobufValueMapper.class);

  @Test
  void uuidToValueMapsUuidAsString() {
    final UUID id = UUID.randomUUID();

    assertEquals(id.toString(), mapper.uuidToValue(id).getStringValue());
    assertEquals(Value.getDefaultInstance(), mapper.uuidToValue(null));
  }

  @Test
  void stringToValueMapsString() {
    assertEquals("abc", mapper.stringToValue("abc").getStringValue());
    assertEquals(Value.getDefaultInstance(), mapper.stringToValue(null));
  }

  @Test
  void toUuidParsesOnlyValidValues() {
    final UUID id = UUID.randomUUID();
    assertTrue(mapper.toUuid(Value.newBuilder().setStringValue(id.toString()).build()).isPresent());
    assertFalse(mapper.toUuid(Value.newBuilder().setStringValue("bad").build()).isPresent());
  }

  @Test
  void toUuidsKeepsDistinctValidValues() {
    final UUID id = UUID.randomUUID();
    final List<UUID> values = mapper.toUuids(List.of(
        Value.newBuilder().setStringValue(id.toString()).build(),
        Value.newBuilder().setStringValue("bad").build(),
        Value.newBuilder().setStringValue(id.toString()).build()
    ));

    assertEquals(List.of(id), values);
  }
}
