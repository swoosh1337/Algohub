package edu.wpi.cs.dss.serverless.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GenericResponse {

    private final String error;
    private final Integer statusCode;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
