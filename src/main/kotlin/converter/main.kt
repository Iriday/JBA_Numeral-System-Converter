package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun main() {
    val prefixes = mapOf(Pair(2, "0b"), Pair(8, "0"), Pair(16, "0x"))
    val num = readNum()
    val base = readNum().toInt()
    print(addPrefix(decimalTo(num, base), base, prefixes))
}

fun decimalTo(decimal: BigInteger, base: Int): String {
    val b = base.toBigDecimal()
    val sb = StringBuilder()
    var left = decimal.toBigDecimal()

    while (true) {
        val numStr = (left.divide(b, 10, RoundingMode.UP)).toPlainString()
        left = numStr.substringBefore('.').toBigDecimal()
        val right = (numStr.substring(numStr.indexOf('.')).toBigDecimal() * b).toInt()
        sb.append(if (right < 10) right else 'a' - 10 + right)

        if (left == BigDecimal.ZERO) return sb.reverse().toString()
    }
}

fun addPrefix(num: String, base: Int, prefixes: Map<Int, String>, defaultPrefix: String = ""): String {
    return prefixes.getOrDefault(base, defaultPrefix) + num
}

fun readNum() = readLine()!!.toBigInteger()