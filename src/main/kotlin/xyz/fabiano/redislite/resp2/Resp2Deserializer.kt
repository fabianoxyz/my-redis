package xyz.fabiano.redislite.resp2

object Resp2Deserializer {

    fun deserialize(respValue: String): RespValue {
        val strippedValue = if (respValue.contains(CRLF)) {
            respValue.substring(1,respValue.lastIndexOf(CRLF))
        } else {
            respValue.substring(1)
        }
        println("\n\n$strippedValue\n\n")
        return when (respValue[0]) {
            '*' -> deserializeArray(strippedValue)
            '$' -> deserializeBulkString(strippedValue)
            ':' -> RespValue(strippedValue.toInt())
            '+' -> RespValue(strippedValue)
            '-' -> deserializeError(strippedValue)
            else -> throw InvalidRespValueException(respValue[0])
        }
    }

    private fun deserializeArray(strippedValue: String) : RespValue {
        return if(strippedValue.startsWith("-1")) RespValue(null)
        else RespValue { strippedValue.split(CRLF).drop(1).map { deserialize(it).value } }
    }

    private fun deserializeBulkString(strippedValue: String): RespValue {
        return if(strippedValue.startsWith("-1")) RespValue(null)
        else RespValue { strippedValue.substring(strippedValue.indexOf(CRLF) + 2) }
    }

    private fun deserializeError(strippedValue: String) = RespError(strippedValue)
}