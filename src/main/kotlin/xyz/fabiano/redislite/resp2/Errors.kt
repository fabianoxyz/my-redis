package xyz.fabiano.redislite.resp2

abstract class RedisServerException(message: String) : RuntimeException(message)
abstract class RespProtocolException(message: String) : RedisServerException(message)

class IllegalRespCommandException : RedisServerException("The command provided is not a valid RESP Command.")
class UnknownCommandException(command : String) : RedisServerException("ERR unknown command '$command'")
class WrongTypeException() : RedisServerException("WRONGTYPE Operation against a key holding the wrong kind of value")

class InvalidRespValueException : RespProtocolException {
    constructor(char: Char) : super("The string starting with '$char' is not a valid RESP2 data.")
    constructor() : super("The string is not a valid RESP data.")
}
