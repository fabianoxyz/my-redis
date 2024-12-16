package xyz.fabiano.redislite

import xyz.fabiano.redislite.io.RedisLiteServer

fun main(args: Array<String>) {
    val argsMap = args.toList().chunked(2).filter { it.size == 2 }.associate { it[0] to it[1] }

    val port = argsMap["-p"]?.toInt() ?: 5554
    val threads = argsMap["-threads"]?.toInt() ?: 1

    RedisLiteServer.start(port, threads)
}