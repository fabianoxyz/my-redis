package xyz.fabiano.myredis

import org.apache.logging.log4j.kotlin.logger
import xyz.fabiano.myredis.io.NewConnectionHandler
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel

private val logger = logger("Main NIO Server")

fun main(args: Array<String>) {
    val serverSocketChannel = AsynchronousServerSocketChannel.open()
    serverSocketChannel.bind(InetSocketAddress(5555))
    logger.info("Redis Lite server started")

    val acceptCompletionHandler = NewConnectionHandler(serverSocketChannel)
    serverSocketChannel.accept<Void?>(null, acceptCompletionHandler)

    System.`in`.read()
}