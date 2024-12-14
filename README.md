# Redis Lite Server

This simple and powerful implementation of a basic/lite functional Redis-compliant database.

It's inspired by the challenge proposed by [John Crickett](https://github.com/johncrickett) on
[Write your own Redis Server](https://codingchallenges.fyi/challenges/challenge-redis)

This solution is implemented in Kotlin 2.1.x using JVM 21.

## Description
This is Redis-like server with support for RESP v2 and v3 protocols.
It seamlessly works with the Redis CLI, as well as the Jedis (Java) Client for Redis 
or any other standard client.

---
### Important Disclaimer

At your own discretion, you are free to use and modify it as you wish.

Nevertheless, although this code and application follow the best standards available for
Kotlin and JVM async socket programming, this is not a production-ready solution,
nor is it intended to be maintained as such.
***Its academic and fun nature is of the utmost importance here.***

If you're either looking for a drop-in replacement for Redis, an open-source alternative,
or a flatly better solution while maintaining all the latest features, 
**Check out [Valkey](https://github.com/valkey-io/valkey)**, a full open-source solution backed by 
[Linux Foundation](https://www.linuxfoundation.org/press/linux-foundation-launches-open-source-valkey-community).

---

## Technologies

- JVM 21 Non-blocking TCP Sockets (via Java NIO2 API)
- Kotlin 2.1.x and Kotlin Coroutines 1.9.x

## The Features

The supported Redis commands are: PING, ECHO, GET, SET (with NX, XX, EX and PX options), DEL, EXISTS, INCR, DECR, LPUSH, RPUSH, LRANGE, FLUSHALL and SAVE.


## The implementation

