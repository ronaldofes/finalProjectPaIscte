import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VisitorNumbersNameIntegrationest {

    val json = JsonObject().apply {
        addProperty("uc", JsonValue.JsonString("PA"))
        addProperty("ects", JsonValue.JsonNumber(6.0))
        addProperty("data-exame", JsonValue.JsonNull())
        addProperty(
            "inscritos", JsonValue.JsonArray(
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
                    })
            )
        )
    }

    @Test
    fun `should get all values stored in properties with the identifier "numero"`() {
        val visitor = SearchVisitorWithNumbers("numero")
        assertEquals(
            mutableListOf(
                JsonValue.JsonNumber(101101),
                JsonValue.JsonNumber(101102),
                JsonValue.JsonNumber(26503)
            ), visitor.visit(json)
        )
    }

    @Test
    fun `should get all objects that have the properties numero and nome`() {
        val mapExpectedResult = mutableListOf<Map<String, JsonValue>>(
            mapOf(Pair("numero", JsonValue.JsonNumber(101101))),
            mapOf(Pair("nome", JsonValue.JsonString("Dave Farley"))),
            mapOf(Pair("numero", JsonValue.JsonNumber(101102))),
            mapOf(Pair("nome", JsonValue.JsonString("Martin Fowler"))),
            mapOf(Pair("numero", JsonValue.JsonNumber(26503))),
            mapOf(Pair("nome", JsonValue.JsonString("André Santos")))
        )
        val visitor = SearchObjectVisitorNumberName(setOf("numero", "nome"))
        assertEquals(6, visitor.visit(json).size)
        assertTrue(
            visitor.visit(json)[0] == mapExpectedResult[0] && visitor.visit(json)[1] == mapExpectedResult[1]
                    && visitor.visit(json)[2] == mapExpectedResult[2] && visitor.visit(json)[3] == mapExpectedResult[3]
                    && visitor.visit(json)[4] == mapExpectedResult[4] && visitor.visit(json)[5] == mapExpectedResult[5]
        )
    }


    @Test
    fun `should all numbers be Integers`() {
        val visitor = IntegerValidationVisitor("numero")
        assertTrue(visitor.visit(json))
    }

}
