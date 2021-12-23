package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class ClassificationHierarchyRequest {

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
