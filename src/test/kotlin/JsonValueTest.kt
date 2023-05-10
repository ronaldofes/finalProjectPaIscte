import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonValueTest {

    class MockVisitor : Visitor {
        val stringValues = mutableListOf<String>()
        val numberValues = mutableListOf<Number>()
        var booleanValue: Boolean? = null
        var nullVisited = false
        var arrayStartVisited = false
        var arrayEndVisited = false

        override fun visitString(value: String) {
            stringValues.add(value)
        }

        override fun visitNumber(value: Number) {
            numberValues.add(value)
        }

        override fun visitBoolean(value: Boolean) {
            booleanValue = value
        }

        override fun visitNull() {
            nullVisited = true
        }


        override fun visitArrayStart() {
            arrayStartVisited = true
        }

        override fun visitArrayEnd() {
            arrayEndVisited = true
        }

        override fun visitObjectStart() {

        }

        override fun visitObjectEnd() {

        }

        override fun visitProperty(property: String) {

        }

    }

    @Test
    fun testJsonStringAccept() {
        val jsonString = JsonValue.JsonString("&%$#")
        val mockVisitor = MockVisitor()
        jsonString.accept(mockVisitor)
        assertEquals(listOf("&%$#"), mockVisitor.stringValues)
    }

    @Test
    fun testJsonNumberAccept() {
        val jsonNumber = JsonValue.JsonNumber(42)
        val mockVisitor = MockVisitor()
        jsonNumber.accept(mockVisitor)
        assertEquals(listOf(42), mockVisitor.numberValues)

        val jsonNumber2 = JsonValue.JsonNumber(6.999)
        val mockVisitor2 = MockVisitor()
        jsonNumber2.accept(mockVisitor2)
        assertEquals(listOf(6.999), mockVisitor2.numberValues)
    }

    @Test
    fun testJsonBooleanAccept() {
        val jsonBoolean = JsonValue.JsonBoolean(true)
        val mockVisitor = MockVisitor()
        jsonBoolean.accept(mockVisitor)
        assertEquals(true, mockVisitor.booleanValue)

        val jsonBoolean2 = JsonValue.JsonBoolean(false)
        val mockVisitor2 = MockVisitor()
        jsonBoolean2.accept(mockVisitor2)
        assertEquals(false, mockVisitor2.booleanValue)
    }

    @Test
    fun testJsonNullAccept() {
        val jsonNull = JsonValue.JsonNull()
        val mockVisitor = MockVisitor()
        jsonNull.accept(mockVisitor)
        assertTrue(mockVisitor.nullVisited)
    }

    @Test
    fun testJsonArrayAccept() {
        val jsonArray = JsonValue.JsonArray(mutableListOf(JsonValue.JsonString("Galaxy"), JsonValue.JsonNumber(42)))
        val mockVisitor = MockVisitor()
        jsonArray.accept(mockVisitor)
        assertEquals(listOf("Galaxy"), mockVisitor.stringValues)
        assertEquals(listOf(42), mockVisitor.numberValues)
        assertTrue(mockVisitor.arrayStartVisited)
        assertTrue(mockVisitor.arrayEndVisited)
    }

    @Test
    fun testAsJsonObject() {
        val jsonObject =
            JsonObject(mutableMapOf("name" to JsonValue.JsonString("Andre"), "ects" to JsonValue.JsonNumber(6.0)))
        val jsonArray = JsonValue.JsonArray(mutableListOf(jsonObject))

        assertEquals(jsonObject, jsonObject.asJsonObject())
        assertEquals(jsonObject, jsonArray.asJsonObject())

        val jsonNumber = JsonValue.JsonNumber(42)
        try {
            jsonNumber.asJsonObject()
            fail("Expected UnsupportedOperationException")
        } catch (e: UnsupportedOperationException) {
            assertEquals("Cannot convert JsonNumber to JsonObject", e.message)
        }
    }
}
