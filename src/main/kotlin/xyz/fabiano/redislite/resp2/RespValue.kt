package xyz.fabiano.redislite.resp2

const val CRLF = "\\r\\n"
const val NULL_STRING = "\$-1\\r\\n"
const val NULL_ARRAY = "*-1\\r\\n"

open class RespValue(val value: Any?) {
    init {
        if (value != null && value !is String && value !is Array<*> && value !is Int) {
            throw InvalidRespValueException()
        }
    }

    open fun isError() = false
}

class RespError(message: String) : RespValue(message) {
    override fun isError() = true

    fun message() = value.toString()
}