package xyz.fabiano.redislite.io

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.CountDownLatch

object RedisLiteServer {
    private val logger = logger()
    private val latch = CountDownLatch(1)

    private val serverSocketChannel = AsynchronousServerSocketChannel.open()
    private val clientSockets = mutableSetOf<AsynchronousSocketChannel>()

    fun registerClient(clientSocket: AsynchronousSocketChannel) {
        clientSockets.add(clientSocket)
    }

    fun clientDisconnected(clientSocket: AsynchronousSocketChannel) {
        logger.info { "Closing client socket on ${clientSocket.localAddress}." }
        clientSocket.close()
        clientSockets.remove(clientSocket)
    }

    fun start(port: Int, workerPoolSize: Int) {
        logger.info("Starting Redis Lite server")

        val address = InetSocketAddress(port)

        serverSocketChannel.bind(address)

        val newConnectionHandler = NewConnectionHandler(serverSocketChannel)
        serverSocketChannel.accept<Void?>(null, newConnectionHandler)

        logger.info("Redis Lite server listening on ${address.hostName}:${address.port}")

        Runtime.getRuntime().addShutdownHook(shutdownHook())

        try {
            latch.await()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        // TODO: Add extra clean up code for the server if needed
    }

    private fun shutdownHook() : Thread {
        return Thread {
            logger.info("Redis Lite server shutting down.")

            clientSockets.forEach { socket ->
                socket.close()
            }
            clientSockets.clear()
            serverSocketChannel.close()
            latch.countDown()
        }
    }
}

