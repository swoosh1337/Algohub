package edu.wpi.cs.dss.serverless.algorithms.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@NoArgsConstructor
public class AlgorithmAddRequest {

    private String name;
    private String authorId;
    private String description;
    private String classificationId;

    public AlgorithmAddRequest(String name, String description, String classification_id, String author_id) {
        this.name = name;
        this.authorId = author_id;
        this.classificationId = classification_id;
        this.description = description;
    }

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
