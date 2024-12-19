package xyz.fabiano.redislite.resp2

object Resp2Deserializer {

    fun deserialize(respValue: String): RespValue {
        val strippedValue = if (respValue.contains(CRLF)) {
            respValue.substring(1, respValue.lastIndexOf(CRLF))
        } else {
            respValue.substring(1)
        }
        return when (respValue[0]) {
            '*' -> deserializeArray(strippedValue)
            '$' -> deserializeBulkString(strippedValue)
            ':' -> RespValue(strippedValue.toInt())
            '+' -> RespValue(strippedValue)
            '-' -> deserializeError(strippedValue)
            else -> throw InvalidRespValueException(respValue[0])
        }
    }

    private fun deserializeArray(strippedValue: String): RespValue {
        if (strippedValue.contains('$') || strippedValue.contains('*')) return deserializeNestedAggregates(strippedValue)

        return if (strippedValue.startsWith("-1")) RespValue(null)
        else RespValue { strippedValue.split(CRLF).drop(1).map { deserialize(it).value } }
    }

    private fun deserializeNestedAggregates(strippedValue: String): RespValue {
        val list = mutableListOf<Any?>()
        val splitted = strippedValue.split(CRLF).drop(1)
        var skipIter = 0

        for (i in 0..splitted.lastIndex) {
            if (skipIter > 0) {
                --skipIter
                continue
            }
            val current = splitted[i]
            if (current[0] == '*') {
                val nested = mutableListOf<Any?>()
                skipIter = current.substring(1).toInt()
                for (j in 1..skipIter) {
                    nested.add(deserialize(splitted[i + j]).value)
                }
                list.add(nested)
                continue
            }
            val parsed: Any? = when (current) {
                "$-1", "*-1" -> null
                "$0" -> ""
                "*0" -> listOf<Any>()
                else -> if (current[0] == '$') {
                    skipIter = 1
                    splitted[i + 1]
                } else {
                    deserialize(current).value
                }
            }
            list.add(parsed)
        }
        return RespValue { list }
    }

    private fun deserializeBulkString(strippedValue: String): RespValue {
        return if (strippedValue.startsWith("-1")) RespValue(null)
        else RespValue { strippedValue.substring(strippedValue.indexOf(CRLF) + 2) }
    }

    private fun deserializeError(strippedValue: String) = RespError(strippedValue)
}