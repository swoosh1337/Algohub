package edu.wpi.cs.dss.serverless.users.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserGetAllRequestTest {

    private static final String AUTHOR_ID_PARAM = "authorId";

    private UserGetAllRequest request;

    @Before
    public void init() {
        request = new UserGetAllRequest();
        request.setAuthorId(AUTHOR_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(AUTHOR_ID_PARAM, request.getAuthorId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(request);
        assertEquals(expected, request.toString());
    }
}
