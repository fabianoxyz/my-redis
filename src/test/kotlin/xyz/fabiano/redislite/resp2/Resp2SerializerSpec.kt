package xyz.fabiano.redislite.resp2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Resp2SerializerSpec : DescribeSpec({

    describe("Serializing Integers") {
        it("with '-123456' value") {
            Resp2Serializer.serialize(-123456) shouldBe ":-123456\r\n"
        }
        it("with '123456' value") {
            Resp2Serializer.serialize(123456) shouldBe ":123456\r\n"
        }
        it("with '123459' value") {
            Resp2Serializer.serialize(123459) shouldBe ":123459\r\n"
        }
        it("with '0' value") {
            Resp2Serializer.serialize(0) shouldBe ":0\r\n"
        }
    }

    describe("Serializing SimpleString") {
        it("with value 'OK'") {
            Resp2Serializer.serializeSimpleString("OK") shouldBe "+OK\r\n"
        }
        it("with value 'hello world'") {
            Resp2Serializer.serializeSimpleString("hello world") shouldBe "+hello world\r\n"
        }
        it("with empty value") {
            Resp2Serializer.serializeSimpleString("") shouldBe "+\r\n"
        }
    }

    describe("Serializing BulkString") {
        it("with null value") {
            Resp2Serializer.serializeBlob(null) shouldBe "$-1\r\n"
        }
        it("with empty value") {
            Resp2Serializer.serializeBlob("") shouldBe "$0\r\n\r\n"
        }
        it("with 'bla\\t\\nbla\\rI' value") {
            Resp2Serializer.serialize("bla\t\nbla\rI") shouldBe "$10\r\nbla\t\nbla\rI\r\n"
        }

    }
    describe("Serializing Errors") {

        it("with message 'Error message'") {
            Resp2Serializer.serialize(RuntimeException("Error message")) shouldBe "-Error message\r\n"
        }
        it("with message 'WRONGTYPE my-message'") {
            Resp2Serializer.serialize(Throwable("WRONGTYPE my-message")) shouldBe "-WRONGTYPE my-message\r\n"
        }
    }
    describe("Serializing Arrays") {
        it("with 'get' and 'key' BulkStrings") {
            Resp2Serializer.serialize("*2\r\n$3\r\nget\r\n$3\r\nkey\r\n") shouldBe arrayOf("get", "key")
        }
        it("with 'ping' BulkString") {
            Resp2Serializer.serialize("*1\r\n$4\r\nping\r\n") shouldBe arrayOf("ping")
        }
        it("with 'echo' and 'hello world' BulkStrings") {
            Resp2Serializer.serialize("*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n") shouldBe arrayOf(
                "echo", "hello world"
            )
        }
        it("with an empty string and a null value") {
            Resp2Serializer.serialize(arrayOf<Any?>("", null)) shouldBe "*2\r\n+\r\n\$-1\r\n"
            Resp2Serializer.serialize(listOf("", null)) shouldBe "*2\r\n+\r\n\$-1\r\n"
        }
        it("empty") {
            Resp2Serializer.serialize(arrayOf<Any?>()) shouldBe "*0\r\n"
            Resp2Serializer.serialize(setOf<String>()) shouldBe "*0\r\n"
        }
        it("null") {
            Resp2Serializer.serialize(null) shouldBe "*-1\r\n"

            val nullArray : Array<String>? = null
            Resp2Serializer.serialize(nullArray) shouldBe "*-1\r\n"

            val nullList : List<Any?>? = null
            Resp2Serializer.serialize(nullList) shouldBe "*-1\r\n"
        }
    }
})
