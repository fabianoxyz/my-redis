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
            Resp2Serializer.serializeBlob(null) shouldBe NULL_STRING
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
            Resp2Serializer.serialize(arrayOf("get", "key")) shouldBe "*2\r\n$3\r\nget\r\n$3\r\nkey\r\n"
            Resp2Serializer.serialize(listOf("get", "key")) shouldBe "*2\r\n$3\r\nget\r\n$3\r\nkey\r\n"
        }
        it("with 'ping' BulkString") {
            Resp2Serializer.serialize(listOf("ping")) shouldBe "*1\r\n$4\r\nping\r\n"
        }
        it("with 'echo' and 'hello world' BulkStrings") {
            Resp2Serializer.serialize(arrayOf("echo", "hello world")) shouldBe
                    "*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n"
        }
        it("with an empty string and a null value") {
            Resp2Serializer.serialize(arrayOf<Any?>("", null)) shouldBe "*2\r\n$0\r\n\r\n$NULL_STRING"
            Resp2Serializer.serialize(listOf("", null)) shouldBe "*2\r\n$0\r\n\r\n$NULL_STRING"
        }
        it("empty") {
            Resp2Serializer.serialize(arrayOf<Any?>()) shouldBe "*0\r\n"
            Resp2Serializer.serialize(setOf<String>()) shouldBe "*0\r\n"
        }
        it("null") {
            Resp2Serializer.serialize(null) shouldBe NULL_ARRAY

            val nullList : List<Any?>? = null
            Resp2Serializer.serialize(nullList) shouldBe NULL_ARRAY
        }
    }
})
