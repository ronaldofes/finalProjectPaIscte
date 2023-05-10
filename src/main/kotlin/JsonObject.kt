import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

data class JsonObject(

    val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : JsonValue() {

    fun addProperty(name: String, value: JsonValue) {
        properties[name] = value
    }

    fun addProperty(name: String, value: Any?) {
        properties[name] =
            (if (value == null) JsonNull()
            else if (value is Number) JsonNumber(value)
            else if (value is String) JsonString(value)
            else if (value is Boolean) JsonBoolean(value)
            else JsonArray(mutableListOf()))

    }

    fun getString(name: String): String? {
        return (properties[name] as? JsonValue.JsonString)?.value
    }

    fun setString(name: String, value: String) {
        properties[name] = JsonValue.JsonString(value)
    }

    fun getNumber(name: String): Number? {
        return (properties[name] as? JsonValue.JsonNumber)?.value
    }

    fun setNumber(name: String, value: Number) {
        properties[name] = JsonValue.JsonNumber(value)
    }

    fun getBoolean(name: String): Boolean? {
        return (properties[name] as? JsonValue.JsonBoolean)?.value
    }

    fun setBoolean(name: String, value: Boolean) {
        properties[name] = JsonValue.JsonBoolean(value)
    }

    fun getNull(name: String): Any? {
        return (properties[name] as? JsonValue.JsonNull)?.value
    }

    fun setNull(name: String) {
        properties[name] = JsonValue.JsonNull()
    }

    fun getArray(name: String): List<JsonValue>? {
        return (properties[name] as? JsonValue.JsonArray)?.elements
    }

    fun setArray(name: String, value: List<JsonValue>) {
        properties[name] = JsonValue.JsonArray(value.toMutableList())
    }

    fun getPropertiesJson(): Map<String, JsonValue> {
        return properties.toMap()
    }

    fun hasProperty(name: String): Boolean {
        return properties.containsKey(name)
    }

    fun removeProperty(name: String) {
        properties.remove(name)
    }

    fun clearProperties() {
        properties.clear()
    }

    fun toJsonString(): String {
        val sb = StringBuilder()
        sb.append("{")
        properties.forEach { (name, value) ->
            sb.append("\"$name\":${toJsonString(value)},")
        }
        if (properties.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1) // remove the last comma
        }
        sb.append("}")
        return sb.toString()
    }

    private fun toJsonString(value: JsonValue): String {
        return when (value) {
            is JsonString -> "\"${value.value}\""
            is JsonNumber -> value.value.toString()
            is JsonBoolean -> value.value.toString()
            is JsonNull -> "null"
            is JsonArray -> {
                val sb = StringBuilder()
                sb.append("[")
                value.elements.forEach { element ->
                    sb.append("${toJsonString(element)},")
                }
                if (value.elements.isNotEmpty()) {
                    sb.deleteCharAt(sb.length - 1) // remove the last comma
                }
                sb.append("]")
                sb.toString()
            }

            else -> jsonObjectToString(value as JsonObject)
        }
    }

    private fun jsonObjectToString(obj: JsonObject): String {
        val sb = StringBuilder()
        sb.append("{")
        obj.properties.forEach { (name, value) ->
            sb.append("\"$name\":${toJsonString(value)},")
        }
        if (obj.properties.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1) // remove the last comma
        }
        sb.append("}")
        return sb.toString()
    }

    override fun accept(visitor: Visitor) {
        visitor.visitObjectStart()
        properties.forEach { (name, value) ->
            visitor.visitProperty(name)
            value.accept(visitor)
        }
        visitor.visitObjectEnd()
    }

    companion object {
        fun from(obj: Any?): JsonObject {
            val jsonObject = JsonObject()
            obj?.let {
                val kClass = it::class
                kClass.memberProperties.reversed().forEach { prop ->
                    val propName = prop.name
                    var propValue: Any?
                    if (prop.isAccessible) {
                        propValue = prop.getter.call(it)
                    } else {
                        prop.isAccessible = true
                        propValue = prop.getter.call(it)
                        prop.isAccessible = false
                    }
                    when (propValue) {
                        is String -> jsonObject.setString(propName, propValue)
                        is Number -> jsonObject.setNumber(propName, propValue)
                        is Boolean -> jsonObject.setBoolean(propName, propValue)
                        is Collection<*> -> {
                            val jsonArray = propValue.map { value ->
                                when (value) {
                                    is String -> JsonValue.JsonString(value)
                                    is Number -> JsonValue.JsonNumber(value)
                                    is Boolean -> JsonValue.JsonBoolean(value)
                                    else -> from(value)
                                }
                            }
                            jsonObject.setArray(propName, jsonArray)
                        }

                        is Map<*, *> -> {
                            val nestedObject = JsonObject()
                            propValue.forEach { (key, value) ->
                                nestedObject.addProperty(
                                    key.toString(), when (value) {
                                        is String -> JsonValue.JsonString(value)
                                        is Number -> JsonValue.JsonNumber(value)
                                        is Boolean -> JsonValue.JsonBoolean(value)
                                        else -> from(value)
                                    }
                                )
                            }
                            jsonObject.addProperty(propName, nestedObject)
                        }

                        is Enum<*> -> jsonObject.setString(propName, propValue.name)
                        null -> jsonObject.setNull(propName)
                        else -> {
                            val nestedObject = from(propValue)
                            jsonObject.addProperty(propName, nestedObject)
                        }
                    }
                }
            }
            return jsonObject
        }
    }


}

