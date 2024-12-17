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
        it("for BulkStrings/Blobs") {
            Resp2ValuesMapper.deserialize("$-1\r\n") shouldBe null

            Resp2ValuesMapper.deserialize("$0\r\n\r\n") shouldBe ""

            Resp2ValuesMapper.deserialize("$10\r\nbla\t\nbla\rI\r\n") shouldBe "bla\t\nbla\nI"
        }

        it("for SimpleStrings") {
            Resp2ValuesMapper.deserialize("+OK\r\n") shouldBe "OK"

            Resp2ValuesMapper.deserialize("+hello world\r\n") shouldBe "hello world"
        }

        it("for Errors") {
            Resp2ValuesMapper.deserialize("-Error message\r\n") shouldBe "Error message"

            Resp2ValuesMapper.deserialize("-WRONGTYPE my-message\r\n") shouldBe "WRONGTYPE my-message"
        }

        it("for Arrays with 'get' and 'key' BulkStrings") {
            Resp2ValuesMapper.deserialize("*2\r\n$3\r\nget\r\n$3\r\nkey\r\n") shouldBe arrayOf("get", "key")

            Resp2ValuesMapper.deserialize("*1\r\n$4\r\nping\r\n") shouldBe arrayOf("ping")

            Resp2ValuesMapper.deserialize("*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n") shouldBe arrayOf("echo", "hello world")
        }
    }



})