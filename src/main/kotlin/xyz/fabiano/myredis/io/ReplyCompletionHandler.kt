package xyz.fabiano.myredis.io

import org.apache.logging.log4j.kotlin.logger
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

class ReplyCompletionHandler(
    private val clientSocketChannel: AsynchronousSocketChannel
) : CompletionHandler<Int, Void?> {

    private val logger = logger()

    override fun completed(result: Int, attachment: Void?) {
        if(result == -1) {
            logger.info { "Client ${clientSocketChannel.remoteAddress} disconnected." }

            RedisLiteServer.clientDisconnected(clientSocketChannel)
            return
        }

        val buffer = ByteBuffer.allocate(1024)
        val requestCommandHandler = RequestCommandHandler(clientSocketChannel, buffer)

        clientSocketChannel.read(buffer, null, requestCommandHandler)
    }

    override fun failed(t: Throwable, attachment: Void?) {
        logger.error("Error while attempting to write to socket ${clientSocketChannel.remoteAddress}. Closing connection", t)

        RedisLiteServer.clientDisconnected(clientSocketChannel)
    }
}
