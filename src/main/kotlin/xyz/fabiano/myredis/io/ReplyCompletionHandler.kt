package xyz.fabiano.myredis.io

import org.apache.logging.log4j.kotlin.logger
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

class ReplyCompletionHandler(
    private val socketChannel: AsynchronousSocketChannel
) : CompletionHandler<Int, Void?> {

    private val logger = logger()

    override fun completed(bytesWritten: Int, attachment: Void?) {
        logger.info("Echo server wrote: $bytesWritten byte(s)")

        val buffer = ByteBuffer.allocate(1024)
        val readCompletionHandler = RequestCommandHandler(socketChannel, buffer)

        socketChannel.read(buffer, null, readCompletionHandler)

        try {

        } catch (e: IOException) {
            logger.error("Exception during socket closing", e)
        }
    }

    override fun failed(t: Throwable, attachment: Void?) {
        logger.error("Exception during socket writing", t)
    }
}
