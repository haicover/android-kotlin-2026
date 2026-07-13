package com.haicover.smoketest

class CounterState(initialCount: Int = 0) {
    var count: Int = initialCount
        private set

    fun increment() {
        count += 1
    }
}
