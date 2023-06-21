package com.hujiejeff.learn_android.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
//    zipDemo()
//    combineDemo()
    transformDemo()
}

fun foo() = flow {
    for (i in 1..3) {
        delay(100) // 假装我们异步等待了 100 毫秒
        emit(i) // 发射下一个值
    }
}

fun sampleDemo() = runBlocking<Unit> {
//sampleStart
    val time = measureTimeMillis {
        foo()
            .collect { value -> // 取消并重新发射最后一个值
                println("Collecting $value")
                delay(300) // 假装我们花费 300 毫秒来处理它
                println("Done $value")

            }
    }
    println("Collected in $time ms")
//sampleEnd
}


fun collectLatestDemo() = runBlocking<Unit> {
//sampleStart
    val time = measureTimeMillis {
        foo()
            .collectLatest { value -> // 取消并重新发射最后一个值
                println("Collecting $value")
                delay(300) // 假装我们花费 300 毫秒来处理它
                println("Done $value")
            }
    }
    println("Collected in $time ms")
//sampleEnd
}


fun zipDemo() = runBlocking<Unit> {
//sampleStart
    val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis() // 记录开始的时间
    nums.zip(strs) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
        .collect { value -> // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
//sampleEnd
}

fun combineDemo() = runBlocking {
//sampleStart
    val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
    val startTime = System.currentTimeMillis() // 记录开始的时间
    nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
        .collect { value -> // 收集并打印
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
//sampleEnd
}


fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}


fun transformDemo() = runBlocking {
    flow {
        emit(1)
        emit(2)
    } .transform { value ->
        if (value == 1) {
            emit("value :$value*2")
        }
        emit("transform :$value")
    }.collect { value->
        println(value)
    }

}