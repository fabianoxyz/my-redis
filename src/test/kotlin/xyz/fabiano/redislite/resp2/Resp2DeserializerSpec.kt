package xyz.fabiano.redislite.resp2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Resp2DeserializerSpec : DescribeSpec({
    describe("Deserializing Integers") {
        it("with '-123456' value") {
            Resp2Deserializer.deserialize(":-123456\r\n").value shouldBe -123456
        }
        it("with '123456' value") {
            Resp2Deserializer.deserialize(":123456\r\n").value shouldBe 123456
        }
        it("with '123459' value") {
            Resp2Deserializer.deserialize(":+123459\r\n").value shouldBe 123459
        }
        it("with '0' value") {
            Resp2Deserializer.deserialize(":0\r\n").value shouldBe 0
        }
    }

    describe("Deserializing SimpleString") {
        it("with value 'OK'") {
            Resp2Deserializer.deserialize("+OK\r\n").value shouldBe "OK"
        }
        it("with value 'hello world'") {
            Resp2Deserializer.deserialize("+hello world\r\n").value shouldBe "hello world"
        }
        it("with empty value") {
            Resp2Deserializer.deserialize("+\r\n").value shouldBe ""
        }
    }

    describe("Deserializing BulkString") {
        it("with null value") {
            Resp2Deserializer.deserialize("$-1\r\n").value shouldBe null
        }
        it("with empty value") {
            Resp2Deserializer.deserialize("$0\r\n\r\n").value shouldBe ""
        }
        it("with 'bla\\t\\nbla\\rI' value") {
            Resp2Deserializer.deserialize("$10\r\nbla\t\nbla\rI\r\n").value shouldBe "bla\t\nbla\rI"
        }
    }
    describe("Deserializing Errors") {
        it("with message 'Error message'") {
            Resp2Deserializer.deserialize("-Error message\r\n").value shouldBe "Error message"
        }
        it("with message 'WRONGTYPE my-message'") {
            Resp2Deserializer.deserialize("-WRONGTYPE my-message\r\n").value shouldBe "WRONGTYPE my-message"
        }
    }
    describe("Deserializing Arrays") {
        it("with number 42 and 'key' Simple String") {
            Resp2Deserializer.deserialize("*2\r\n:+42\r\n+key\r\n").value shouldBe arrayOf(42, "key")
        }
        it("with 'get' and 'key' BulkStrings") {
            Resp2Deserializer.deserialize("*2\r\n$3\r\nget\r\n$3\r\nkey\r\n").value shouldBe arrayOf("get", "key")
        }
        it("with 'ping' BulkString") {
            Resp2Deserializer.deserialize("*1\r\n$4\r\nping\r\n").value shouldBe arrayOf("ping")
        }
        it("with 'echo' and 'hello world' BulkStrings") {
            Resp2Deserializer.deserialize("*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n").value shouldBe arrayOf(
                "echo", "hello world"
            )
        }
        it("with an empty string and a null value") {
            Resp2Deserializer.deserialize("*2\r\n+\r\n\$-1\r\n").value shouldBe arrayOf<Any?>("", null)
        }
        it("empty") {
            Resp2Deserializer.deserialize("*0\r\n").value shouldBe arrayOf<Any?>()
        }
        it("null") {
            Resp2Deserializer.deserialize("*-1\r\n").value shouldBe null
        }
        it("with nested arrays") {
            val nestedArray = "*3\r\n$3\r\nkey\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+Hello\r\n-World\r\n"

            Resp2Deserializer.deserialize(nestedArray).value shouldBe
                    arrayOf("key", arrayOf(1, 2, 3), arrayOf("Hello","World"))
        }
    }
})