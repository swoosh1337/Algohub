package edu.wpi.cs.dss.serverless.users.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.wpi.cs.dss.serverless.problemInstances.model.ProblemInstance;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserActivityTest {

    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String TYPENAME_PARAM = "typeName";

    private UserActivity userActivity;

    @Before
    public void init() {
        userActivity = new UserActivity(ID_PARAM, NAME_PARAM, TYPENAME_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, userActivity.getId());
        assertEquals(NAME_PARAM, userActivity.getName());
        assertEquals(TYPENAME_PARAM, userActivity.getTypeName());
    }

    @Test
    public void testHashCode() {
        assertEquals(userActivity.hashCode(), userActivity.hashCode());
    }

    @Test
    public void testEquals() {
        assertNotEquals(userActivity, "");
        assertNotEquals(userActivity, null);
        assertEquals(userActivity, userActivity);
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(userActivity);
        assertEquals(expected, userActivity.toString());
    }
}
