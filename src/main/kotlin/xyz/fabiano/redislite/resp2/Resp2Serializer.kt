package xyz.fabiano.redislite.resp2

object Resp2Serializer {

    fun serialize(value: String) = serializeBlob(value)

    fun serialize(value: Int) = ":$value$CRLF"

    fun serialize(e: Throwable) = "-${e.message}$CRLF"

    fun serialize(value: Collection<*>?): String {
        if (value == null) return NULL_ARRAY
        return value.joinToString(separator = "", prefix = "*${value.size}$CRLF", postfix = "") { serialize(it) }
    }

    fun serialize(value: Array<*>) = serialize(value.asList())

    fun serializeSimpleString(value: String) = "+$value$CRLF"

    fun serializeBlob(value: String?): String {
        return if (value == null) NULL_STRING
        else "$${value.length}$CRLF${value}$CRLF"
    }

    private fun serialize(value: Any?): String {
        return when (value) {
            null -> NULL_STRING
            is String -> serialize(value)
            is Int -> serialize(value)
            is Throwable -> serialize(value)
            is Iterable<*> -> serialize(value)
            is Array<*> -> serialize(value)
            else -> serializeBlob(value.toString())
        }
    }
}