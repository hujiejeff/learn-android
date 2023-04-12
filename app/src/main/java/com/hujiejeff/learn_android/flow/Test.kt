package com.hujiejeff.learn_android.flow

import com.google.gson.Gson

class Test {
    fun test() {
        val list = listOf<String>("1", "2", "3")
        list.forEach {
            val after = "$it."
            println(after)
        }

        var after2: String
        list.forEach {
            after2 = "$it.2"
            println(after2)
        }
    }
}

fun main() {
    val jsonStr = """{"name": "hu", "age": null}"""
    val ob = Gson().fromJson<Person>(jsonStr, Person::class.java)
    println(ob.toString())
    println(ob.age)
}

data class Person(val name: String = "", val age: String = "", val sex: Int = 0)