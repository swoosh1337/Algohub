package edu.wpi.cs.dss.serverless.classifications.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "id")
public class HierarchyEntry {
    String id;
    String name;
    String parentId;
    String typeName;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
