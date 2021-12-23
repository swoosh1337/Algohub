package edu.wpi.cs.dss.serverless.users.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class UserGetAllResponseTest {

    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private UserGetAllResponse response;

    @Before
    public void init() {
        response = UserGetAllResponse.builder()
                .users(Collections.emptyList())
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(ERROR_PARAM, response.getError());
        assertEquals(0, response.getUsers().size());
        assertEquals(STATUS_CODE_PARAM, response.getStatusCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(response);
        assertEquals(expected, response.toString());
    }
}
