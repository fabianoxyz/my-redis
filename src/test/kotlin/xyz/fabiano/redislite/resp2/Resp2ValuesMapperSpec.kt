package xyz.fabiano.redislite.resp2

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Resp2ValuesMapperSpec : DescribeSpec({
    describe("Serializing") {
        it("BulkStrings/Blobs") {
            // TODO: Implement specs
        }
    }

    describe("Deserializing") {
        it("for Integer with negative value") {
            Resp2ValuesMapper.deserialize(":-123456\r\n").value shouldBe -123456
        }

        it("for Integer with positive value") {
            Resp2ValuesMapper.deserialize(":123456\r\n").value shouldBe 123456
            Resp2ValuesMapper.deserialize(":+123459\r\n").value shouldBe 123459
            Resp2ValuesMapper.deserialize(":0\r\n").value shouldBe 0
        }

        it("for 'OK' SimpleString") {
            Resp2ValuesMapper.deserialize("+OK\r\n").value shouldBe "OK"
        }
        it("for 'hello world' SimpleString") {
            Resp2ValuesMapper.deserialize("+hello world\r\n").value shouldBe "hello world"
        }
        it("for empty SimpleString") {
            Resp2ValuesMapper.deserialize("+\r\n").value shouldBe ""
        }

        it("for null BulkStrings/Blobs") {
            Resp2ValuesMapper.deserialize("$-1\r\n").value shouldBe null
        }
        it("for empty BulkStrings/Blobs") {
            Resp2ValuesMapper.deserialize("$0\r\n\r\n").value shouldBe ""
        }
        it("for valid BulkStrings/Blobs") {
            Resp2ValuesMapper.deserialize("$10\r\nbla\t\nbla\rI\r\n").value shouldBe "bla\t\nbla\rI"
        }

        it("for Errors") {
            Resp2ValuesMapper.deserialize("-Error message\r\n").value shouldBe "Error message"

            Resp2ValuesMapper.deserialize("-WRONGTYPE my-message\r\n").value shouldBe "WRONGTYPE my-message"
        }

        it("for Array with 'get' and 'key' BulkStrings") {
            Resp2ValuesMapper.deserialize("*2\r\n$3\r\nget\r\n$3\r\nkey\r\n").value shouldBe arrayOf("get", "key")
        }
        it("for Array with 'ping' BulkString") {
            Resp2ValuesMapper.deserialize("*1\r\n$4\r\nping\r\n").value shouldBe arrayOf("ping")
        }
        it("for Array with 'echo' and 'hello world' BulkStrings") {
            Resp2ValuesMapper.deserialize("*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n").value shouldBe arrayOf("echo", "hello world")
        }
        it("for Array with an empty string and a null value") {
            Resp2ValuesMapper.deserialize("*2\r\n+\r\n\$-1\r\n").value shouldBe arrayOf<Any?>("", null)
        }
        it("for an empty Array") {
            Resp2ValuesMapper.deserialize("*0\r\n").value shouldBe arrayOf<Any?>()
        }
        it("for a null Array") {
            Resp2ValuesMapper.deserialize("*-1\r\n").value shouldBe null
        }
    }
})