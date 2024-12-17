package xyz.fabiano.redislite.io

import xyz.fabiano.redislite.resp2.RespValue
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.nio.charset.StandardCharsets

internal class RequestCommandHandler(
    private val clientSocketChannel: AsynchronousSocketChannel,
    private val buffer: ByteBuffer
) : CompletionHandler<Int, Void?> {

    private val charset = StandardCharsets.US_ASCII

    private val logger = logger()

    override fun completed(result: Int, attachment: Void?) {
        if(result == -1) {
            logger.info { "Client ${clientSocketChannel.remoteAddress} disconnected." }

            RedisLiteServer.clientDisconnected(clientSocketChannel)
            return
        }

        buffer.flip()
        val bytes = ByteArray(buffer.limit())
        buffer[bytes]
        val command = String(bytes, charset)

        logger.info { "Command received: $command" }

        val replyCompletionHandler = ReplyCompletionHandler(clientSocketChannel)
        buffer.clear()

        val writeByteBuffer = ByteBuffer.wrap("-UNKCMDERR unknown command${RespValue.CRLF}".toByteArray(charset))

        clientSocketChannel.write(writeByteBuffer, null, replyCompletionHandler)
    }

    override fun failed(t: Throwable, attachment: Void?) {
        logger.error("Exception while reading from socket ${clientSocketChannel.remoteAddress}. Closing connection.", t)
    }
}
