package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

fun main() {
    run()
}

fun run() {
    val prefixes = mapOf(Pair(2, "0b"), Pair(8, "0"), Pair(16, "0x"))
    val srcBase = readNum()
    val num = readStr()
    val trgBase = readNum()
    print(addPrefix(decimalTo(toDecimal(num, srcBase), trgBase), trgBase, prefixes))
}

fun decimalTo(decimal: BigInteger, base: Int): String {
    if (base == 1) {
        val sb = StringBuilder()
        var decimalTemp = decimal
        while (decimalTemp != BigInteger.ZERO) {
            sb.append("1")
            decimalTemp--
        }
        return sb.toString()
    }
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

fun toDecimal(number: String, base: Int): BigInteger {
    val baseDouble = base.toDouble()
    var decimal = BigDecimal.ZERO

    for ((i, num) in number.withIndex()) {
        decimal += (baseDouble.pow(number.length - 1 - i)
                * (num.toString().toIntOrNull() ?: num - 'a' + 10)).toBigDecimal()
    }
    return decimal.toBigInteger()
}

fun addPrefix(num: String, base: Int, prefixes: Map<Int, String>, defaultPrefix: String = ""): String {
    return prefixes.getOrDefault(base, defaultPrefix) + num
}

fun readNum() = readLine()!!.toInt()
fun readStr() = readLine()!!