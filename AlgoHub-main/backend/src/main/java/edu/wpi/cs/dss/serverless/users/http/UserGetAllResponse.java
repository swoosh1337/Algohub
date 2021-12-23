package edu.wpi.cs.dss.serverless.users.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class UserGetAllResponse extends GenericResponse {

    private List<String> users;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
