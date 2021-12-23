package edu.wpi.cs.dss.serverless.problemInstances.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "id")
public class ProblemInstance {
    String id;
    String authorId;
    String problemType;
    String algorithmId;
    Integer datasetSize;
    String datasetFilename;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
