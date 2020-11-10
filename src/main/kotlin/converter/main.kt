package converter

fun main() {
    run()
}

fun run() {
    val srcBase = readNum()
    val num = readStr()
    val trgBase = readNum()
    print(addPrefix(convert(srcBase, num, trgBase, fractionLen = 5), trgBase))
}

fun readNum() = readLine()!!.toInt()
fun readStr() = readLine()!!
