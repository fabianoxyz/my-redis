package xyz.fabiano.myredis.io

import org.apache.logging.log4j.kotlin.logger
import xyz.fabiano.myredis.protocol.datatype.CRLF
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.nio.charset.StandardCharsets

internal class RequestCommandHandler(
    private val socketChannel: AsynchronousSocketChannel,
    private val buffer: ByteBuffer
) : CompletionHandler<Int, Void?> {

    private val charset = StandardCharsets.US_ASCII

    private val logger = logger()

    override fun completed(bytesRead: Int?, attachment: Void?) {
        logger.info("Echo server read: $bytesRead byte(s)")

        buffer.flip()
        val bytes = ByteArray(buffer.limit())
        buffer[bytes]

        val message = String(bytes, charset)
        logger.info("Echo server received: $message", )

        val writeCompletionHandler = ReplyCompletionHandler(socketChannel)
        buffer.clear()

        val writeByteBuffer = ByteBuffer.wrap("-UNKCMDERR unknown command${CRLF}".toByteArray(charset))

        socketChannel.write(writeByteBuffer, null, writeCompletionHandler)
    }

    override fun failed(t: Throwable, attachment: Void?) {
        logger.error("Exception during socket reading", t)
    }
}
