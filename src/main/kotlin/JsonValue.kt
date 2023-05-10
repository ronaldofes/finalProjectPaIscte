sealed class JsonValue {

    abstract fun accept(visitor: Visitor)

    fun valueAsType(): Any? {
        return when (this) {
            is JsonString -> this.value
            is JsonNumber -> this.value
            is JsonBoolean -> this.value
            is JsonNull -> null
            is JsonArray -> this.elements
            else -> {}
        }
    }

    fun asJsonObject(): JsonObject {
        return if (this is JsonObject) {
            this
        } else if (this is JsonArray) {
            if (elements.size == 1 && elements[0] is JsonObject) {
                elements[0] as JsonObject
            } else {
                throw UnsupportedOperationException("Cannot convert ${this.javaClass.simpleName} to JsonObject")
            }
        } else {
            throw UnsupportedOperationException("Cannot convert ${this.javaClass.simpleName} to JsonObject")
        }
    }


    data class JsonString(val value: String) : JsonValue() {
        override fun accept(visitor: Visitor) {
            visitor.visitString(value)
        }
    }

    data class JsonNumber(val value: Number) : JsonValue() {
        override fun accept(visitor: Visitor) {
            return if (value is Int) {
                visitor.visitNumber(value)
            } else visitor.visitNumber(value)
        }

        fun getValueJson() = value
    }


    data class JsonBoolean(val value: Boolean) : JsonValue() {
        override fun accept(visitor: Visitor) {
            visitor.visitBoolean(this.value)
        }
    }

    data class JsonNull(val value: Any? = null) : JsonValue() {
        override fun accept(visitor: Visitor) {
            visitor.visitNull()
        }
    }

    data class JsonArray(val elements: MutableList<JsonValue> = mutableListOf()) : JsonValue() {
        override fun accept(visitor: Visitor) {
            visitor.visitArrayStart()
            val type = elements[0]
            elements.forEach {
                it.accept(visitor)
            }
            visitor.visitArrayEnd()
        }
    }
}



