package edu.wpi.cs.dss.serverless.classifications.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HierarchyEntryTest {

    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String PARENT_ID_PARAM = "parentId";
    private static final String TYPE_NAME_PARAM = "typeName";

    private HierarchyEntry hierarchyEntry;

    @Before
    public void init() {
        hierarchyEntry = new HierarchyEntry(
                ID_PARAM,
                NAME_PARAM,
                PARENT_ID_PARAM,
                TYPE_NAME_PARAM
        );
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, hierarchyEntry.getId());
        assertEquals(NAME_PARAM, hierarchyEntry.getName());
        assertEquals(PARENT_ID_PARAM, hierarchyEntry.getParentId());
        assertEquals(TYPE_NAME_PARAM, hierarchyEntry.getTypeName());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(hierarchyEntry);
        assertEquals(expected, hierarchyEntry.toString());
    }

    @Test
    public void testEquals() {
        assertNotEquals(hierarchyEntry, "");
        assertNotEquals(hierarchyEntry, null);
        assertEquals(hierarchyEntry, hierarchyEntry);
    }

    @Test
    public void testHashCode() {
        assertEquals(hierarchyEntry.hashCode(), hierarchyEntry.hashCode());
    }
}
