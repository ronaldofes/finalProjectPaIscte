import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Date

class JsonFactoryTest {

    enum class TypeLocation { INT, EU, PORT }

    data class Student(val number: Int, val name: String, val isInternational: Boolean)
    data class StudentLocation(val type: TypeLocation, val number: Int, val name: String, val isInternational: Boolean)
    data class Course(
        val uc: String, val ects: Double, val dataExame: Date? = null, val registered: MutableList<Student>
    )

    @Test
    fun testValueObject() {
        val student = Student(101101, "Dave Farley", true)
        val jsonObject = JsonObject.from(student)

        val expectedJson = """
        {"number":101101,"name":"Dave Farley","isInternational":true}
    """.trimIndent()

        assertEquals(expectedJson, jsonObject.toJsonString())
    }

    @Test
    fun testCollection() {
        val student = Student(101101, "Dave Farley", true)
        val course = Course("PA", 6.0, null, mutableListOf(student))
        val jsonObject = JsonObject.from(course)
        val expectedJson = """
        {"uc":"PA","registered":[{"number":101101,"name":"Dave Farley","isInternational":true}],"ects":6.0,"dataExame":null}
    """.trimIndent()

        assertEquals(expectedJson, jsonObject.toJsonString())
    }

    @Test
    fun testEnum() {
        val studentLocation = StudentLocation(TypeLocation.EU,101101, "Dave Farley", true)
        val jsonObject = JsonObject.from(studentLocation)

        val expectedJson = """
        {"type":"EU","number":101101,"name":"Dave Farley","isInternational":true}
    """.trimIndent()
        assertEquals(expectedJson, jsonObject.toJsonString())
    }
}