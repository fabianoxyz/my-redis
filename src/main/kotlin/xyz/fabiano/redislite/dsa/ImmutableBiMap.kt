package xyz.fabiano.redislite.dsa

class ImmutableBiMap<K, V>(vararg pairs: Pair<K, V>) {
    private val keyMap = mapOf(*pairs)
    private val valMap : Map<V, K> = pairs.associate { (k, v) -> Pair(v, k) }

//    fun get(key: K): V? {
//        return keyMap[key]
//    }
//
//    fun get(value : V): K? {
//        return valMap[value]
//    }
}