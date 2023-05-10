import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IntegrationTestsJsonObjectAndValue {

    @Test
    fun `test add and get properties`() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("uc", JsonValue.JsonString("PA"))
        jsonObject.addProperty("ects", JsonValue.JsonNumber(6.0))
        jsonObject.addProperty("data-exame", JsonValue.JsonNull())

        assertEquals("PA", jsonObject.getString("uc"))
        assertEquals(6.0, jsonObject.getNumber("ects"))
        assertNull(jsonObject.getNull("data-exame"))
    }

    @Test
    fun `test add and get array property`() {
        val jsonObject = JsonObject()
        val inscritos = JsonValue.JsonArray(
            mutableListOf(
                JsonObject().apply {
                    addProperty("numero", JsonValue.JsonNumber(101101))
                    addProperty("nome", JsonValue.JsonString("Dave Farley"))
                    addProperty("internacional", JsonValue.JsonBoolean(true))
                },
                JsonObject().apply {
                    addProperty("numero", JsonValue.JsonNumber(101102))
                    addProperty("nome", JsonValue.JsonString("Martin Fowler"))
                    addProperty("internacional", JsonValue.JsonBoolean(true))
                },
                JsonObject().apply {
                    addProperty("numero", JsonValue.JsonNumber(26503))
                    addProperty("nome", JsonValue.JsonString("André Santos"))
                    addProperty("internacional", JsonValue.JsonBoolean(false))
                }
            ))
        jsonObject.addProperty("inscritos", inscritos)

        assertEquals(3, jsonObject.getArray("inscritos")?.size)
        assertEquals("Dave Farley", jsonObject.getArray("inscritos")?.get(0)?.asJsonObject()?.getString("nome"))
    }

    @Test
    fun `test remove and clear properties`() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("uc", JsonValue.JsonString("PA"))
        jsonObject.addProperty("ects", JsonValue.JsonNumber(6.0))

        assertTrue(jsonObject.hasProperty("uc"))
        jsonObject.removeProperty("uc")
        assertFalse(jsonObject.hasProperty("uc"))

        assertTrue(jsonObject.hasProperty("ects"))
        jsonObject.clearProperties()
        assertFalse(jsonObject.hasProperty("ects"))
    }

    @Test
    fun `test toJsonString`() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("uc", JsonValue.JsonString("PA"))
        jsonObject.addProperty("ects", JsonValue.JsonNumber(6.0))
        jsonObject.addProperty("data-exame", JsonValue.JsonNull())

        val expectedJson = "{\"uc\":\"PA\",\"ects\":6.0,\"data-exame\":null}"
        assertEquals(expectedJson, jsonObject.toJsonString())
    }

    @Test
    fun `test array with only one JsonObject`() {
        val jsonObject = JsonObject()
        val inscritos = JsonValue.JsonArray(
            mutableListOf(
                JsonObject().apply {
                    addProperty("numero", JsonValue.JsonNumber(101102))
                    addProperty("nome", JsonValue.JsonString("Martin Fowler"))
                    addProperty("internacional", JsonValue.JsonBoolean(true))
                }
            ))
        jsonObject.addProperty("inscritos", inscritos)

        assertEquals(1, jsonObject.getArray("inscritos")?.size)
        assertEquals(101102, jsonObject.getArray("inscritos")?.get(0)?.asJsonObject()?.getNumber("numero"))
    }

}
