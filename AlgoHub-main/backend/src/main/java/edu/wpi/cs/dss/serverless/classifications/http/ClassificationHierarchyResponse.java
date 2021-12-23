package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.wpi.cs.dss.serverless.classifications.model.HierarchyEntry;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ClassificationHierarchyResponse extends GenericResponse {

    public final List<HierarchyEntry> hierarchy;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
