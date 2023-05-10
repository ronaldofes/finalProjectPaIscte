import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JsonObjectTest {

    private lateinit var jsonObject: JsonObject

    @BeforeEach
    fun setUp() {
        jsonObject = JsonObject()
    }

    @Test
    fun testAddProperty() {
        jsonObject.addProperty("name", JsonValue.JsonString("Martin"))
        assertEquals("Martin", jsonObject.getString("name"))
    }

    @Test
    fun testSetString() {
        jsonObject.setString("name", "Dave Farley")
        assertEquals("Dave Farley", jsonObject.getString("name"))
    }

    @Test
    fun testSetNumber() {
        jsonObject.setNumber("ects", 6.0)
        assertEquals(6.0, jsonObject.getNumber("ects"))
    }

    @Test
    fun testSetBoolean() {
        jsonObject.setBoolean("international", false)
        assertEquals(false, jsonObject.getBoolean("international"))
    }

    @Test
    fun testSetNull() {
        jsonObject.setNull("data-exame")
        assertEquals(null, jsonObject.getNull("data-exame"))
    }

    @Test
    fun testSetArray() {
        val jsonArray = mutableListOf<JsonValue>()
        jsonArray.add(JsonValue.JsonString("numero"))
        jsonArray.add(JsonValue.JsonString("nome"))
        jsonObject.setArray("inscritos", jsonArray)
        assertEquals(jsonArray, jsonObject.getArray("inscritos"))
    }

    @Test
    fun testGetProperties() {
        jsonObject.setString("uc", "PA")
        jsonObject.setNumber("ects", 6.0)
        val expected = mapOf("uc" to JsonValue.JsonString("PA"), "ects" to JsonValue.JsonNumber(6.0))
        assertEquals(expected, jsonObject.getPropertiesJson())
    }

    @Test
    fun testHasProperty() {
        jsonObject.setString("nome", "Martin Fowler")
        assertEquals(true, jsonObject.hasProperty("nome"))
    }

    @Test
    fun testRemoveProperty() {
        jsonObject.setString("nome", "Martin Fowler")
        jsonObject.removeProperty("nome")
        assertEquals(false, jsonObject.hasProperty("name"))
    }

    @Test
    fun testClearProperties() {
        jsonObject.setString("nome", "Dave")
        jsonObject.setBoolean("internacional", true)
        jsonObject.setNull("uc")
        jsonObject.clearProperties()
        assertEquals(0, jsonObject.getPropertiesJson().size)
    }

    @Test
    fun testToJsonString() {
        jsonObject.setString("nome", "Andre")
        jsonObject.setNumber("numero", 26503)
        val expected = "{\"nome\":\"Andre\",\"numero\":26503}"
        assertEquals(expected, jsonObject.toJsonString())
    }

    @Test
    fun testPrimitiveTypes() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("int", 1)
        jsonObject.addProperty("double", 2.5)
        jsonObject.addProperty("float", 3.14f)
        jsonObject.addProperty("long", 1234567890L)
        jsonObject.addProperty("short", 32767.toShort())
        jsonObject.addProperty("byte", 127.toByte())
        jsonObject.addProperty("boolean", true)

        val expectedJson = """
        {"int":1,"double":2.5,"float":3.14,"long":1234567890,"short":32767,"byte":127,"boolean":true}
    """.trimIndent()

        assertEquals(expectedJson, jsonObject.toJsonString())
    }

}