interface Visitor {
    fun visitNull()
    fun visitBoolean(value: Boolean)
    fun visitNumber(value: Number)
    fun visitString(value: String)
    fun visitArrayStart()
    fun visitArrayEnd()
    fun visitObjectStart()
    fun visitObjectEnd()
    fun visitProperty(property: String)
}