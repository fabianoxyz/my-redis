package xyz.fabiano.redislite.io

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.loggerOf

inline fun <reified T : Any> T.logger(): KotlinLogger {
    return loggerOf(T::class.java)
}