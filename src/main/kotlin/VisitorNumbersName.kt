// SearchVisitor for finding all values stored in properties with the identifier
class SearchVisitorWithNumbers(private val identifier: String) : Visitor {
    private val result = mutableListOf<JsonValue>()

    fun visit(json: JsonObject): MutableList<JsonValue> {
        json.properties.forEach { (name, value) ->
            if (name == identifier) {
                result.add(value)
            }
            if (value is JsonValue.JsonArray)
                value.elements.forEach {
                    visit(it.asJsonObject())
                }
        }
        return result
    }

    override fun visitString(value: String) {
    }

    override fun visitObjectStart() {}

    override fun visitProperty(name: String) {
    }

    override fun visitObjectEnd() {}

    override fun visitArrayStart() {}

    override fun visitArrayEnd() {}

    override fun visitNumber(value: Number) {}

    override fun visitBoolean(value: Boolean) {}

    override fun visitNull() {}

}

// SearchVisitor for finding all objects that have the properties number and name
class SearchObjectVisitorNumberName(private val identifier: Set<String>) : Visitor {
    private val results = mutableListOf<Map<String, JsonValue>>()

    private val result = mutableListOf<Map<String, JsonValue>>()

    fun visit(json: JsonObject): MutableList<Map<String, JsonValue>> {
        json.properties.forEach { (name, value) ->
            if (identifier.contains(name)) {
                result.add(mapOf(Pair(name, value)))
            }
            if (value is JsonValue.JsonArray)
                value.elements.forEach {
                    visit(it.asJsonObject())
                }
        }
        return result
    }

    override fun visitString(value: String) {
    }

    override fun visitObjectStart() {
    }

    override fun visitProperty(name: String) {
    }

    override fun visitObjectEnd() {}

    override fun visitArrayStart() {}

    override fun visitArrayEnd() {}

    override fun visitNumber(value: Number) {
    }

    override fun visitBoolean(value: Boolean) {}

    override fun visitNull() {}

}

// ValidationVisitor for verifying that the property number only has integer values
class IntegerValidationVisitor(private val identifier: String) : Visitor {

    fun visit(json: JsonObject): Boolean {
        json.properties.forEach { (name, value) ->
            if (name == identifier) {
                if (value.valueAsType() is Number && value.valueAsType() !is Int) return false
            }
            if (value is JsonValue.JsonArray)
                value.elements.forEach {
                    visit(it.asJsonObject())
                }
        }
        return true
    }

    override fun visitString(value: String) {}

    override fun visitObjectStart() {}

    override fun visitProperty(name: String) {}

    override fun visitObjectEnd() {}

    override fun visitArrayStart() {}

    override fun visitArrayEnd() {}

    override fun visitNumber(value: Number) {
    }

    override fun visitBoolean(value: Boolean) {}

    override fun visitNull() {}

}

//// ValidationVisitor for verifying that the property subscribers consists of an array where all objects have the same structure
//class ArrayValidationVisitor : Visitor {
//    private var isValid = true
//    private var structure: List<String>? = null
//
//    override fun visitString(value: String) {}
//
//    override fun visitObjectEnd() {}
//
//    override fun visitArrayStart() {
//        structure = null
//    }
//
//    override fun visitArrayEnd() {
//        if (structure != null) {
//            for (i in 1 until structure!!.size) {
//                if (structure!![i] != structure!![0]) {
//                    isValid = false
//                    break
//                }
//            }
//        }
//    }
//
//    override fun visitNumber(value: Number) {}
//
//    override fun visitBoolean(value: Boolean) {}
//
//    override fun visitNull() {}
//
//    override fun visitProperty(name: String) {
//        if (structure != null) {
//            structure!!.plus(name)
//        }
//    }
//
//    override fun visitObjectStart() {
//        if (structure == null) {
//            structure = mutableListOf()
//        }
//    }
//
//    fun isValid(): Boolean {
//        return isValid
//    }
//}