package xyz.fabiano.redislite.resp2

object Resp2Serializer {
    fun serialize(value: String) = serializeBlob(value)

    fun serialize(value: Int) = ":$value$CRLF"

    fun serialize(e: Throwable) = "-${e.message}$CRLF"

    fun serialize(value: Iterable<*>): String {
        return value.joinToString(separator = CRLF, prefix = "*", postfix = CRLF) { serialize(it) }
    }

    fun serialize(value: Array<*>): String {
        return serialize(value.asList())
    }

    fun serializeSimpleString(value: String) = "+$value$CRLF"

    fun serializeBlob(value: String?): String {
        return if (value == null) NULL_STRING
        else "$${value.length}$CRLF${value}$CRLF"
    }

    fun serialize(value: Any?): String {
        return when (value) {
            null -> NULL_ARRAY
            is String -> serialize(value)
            is Int -> serialize(value)
            is Throwable -> serialize(value)
            is Iterable<*> -> serialize(value)
            is Array<*> -> serialize(value)
            else -> serializeBlob(value.toString())
        }
    }
}