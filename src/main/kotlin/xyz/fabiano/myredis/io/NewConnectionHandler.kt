package xyz.fabiano.myredis.io

import org.apache.logging.log4j.kotlin.logger
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

class NewConnectionHandler(private val serverSocketChannel: AsynchronousServerSocketChannel) :
    CompletionHandler<AsynchronousSocketChannel, Void?> {

    private val logger = logger()

    override fun completed(socketChannel: AsynchronousSocketChannel, attachment: Void?) {
        logger.info { "Connection accepted: $socketChannel" }

        serverSocketChannel.accept<Void?>(null, this)

        val buffer = ByteBuffer.allocate(1024)
        val readCompletionHandler = RequestCommandHandler(socketChannel, buffer)
        socketChannel.read<Void?>(buffer, null, readCompletionHandler)
    }

    override fun failed(t: Throwable, attachment: Void?) {
        logger.error("Exception during connection accepting", t)
    }
}
