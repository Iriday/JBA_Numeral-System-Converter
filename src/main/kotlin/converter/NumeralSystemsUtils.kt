package converter

import java.math.BigDecimal
import java.math.RoundingMode

// Model
fun convert(srcBase: Int, number: String, trgBase: Int, scale: Int = 16, fractionLen: Int = 16): String {
    return decimalTo(toDecimal(number, srcBase, scale), trgBase, scale, fractionLen)
}

fun decimalTo(decimal: String, base: Int, scale: Int = 16, fractionLen: Int = 16): String {
    throwIfIncorrectBase(base)
    throwIfIncorrectNumber(decimal, 10)
    throwIfIncorrectScale(scale)
    throwIfIncorrectFractionLen(scale, fractionLen)

    fun decimalToBaseOne(decimal: String): String {
        val sb = StringBuilder()
        var counter = decimal.toBigDecimal()
        while (counter >= BigDecimal.ONE) {
            sb.append("1")
            counter--
        }
        return sb.toString()
    }

    fun convertIntegerPart(integer: String, base: Int, scale: Int): String {
        val baseBigDec = base.toBigDecimal()
        val sb = StringBuilder()
        var left = integer.toBigDecimal()

        while (true) {
            val numStr = (left.divide(baseBigDec, scale, RoundingMode.UP)).toPlainString()
            left = numStr.substringBefore('.').toBigDecimal()
            val right = numStr.substring(numStr.indexOf('.')).toBigDecimal() * baseBigDec
            sb.append(numToChar(right.toInt()))

            if (left == BigDecimal.ZERO) return sb.reverse().toString()
        }
    }

    fun convertFractionalPart(fraction: String, base: Int, fractionLen: Int): String {
        val baseBigDec = base.toBigDecimal()
        val sb = StringBuilder(fractionLen)
        var r = fraction.toBigDecimal()

        repeat(fractionLen) {
            val parts = splitNum((r * baseBigDec).toPlainString())
            sb.append(numToChar(parts.first.toInt()))
            r = parts.second.toBigDecimal()
        }
        return sb.toString()
    }
    if (base == 1) {
        return decimalToBaseOne(decimal)
    }
    if (!decimal.contains('.')) {
        return convertIntegerPart(decimal, base, scale)
    }
    val (integer, fraction) = splitNum(decimal)
    // to get better precision try to pass "scale" instead of "fractionLen" to "convertFractionalPart" function,
    // then the result that the function returned round (not just cut) to "fractionLen" digits
    return convertIntegerPart(integer, base, scale) + "." + convertFractionalPart(fraction, base, fractionLen)
}

fun toDecimal(number: String, base: Int, scale: Int = 16): String {
    throwIfIncorrectBase(base)
    throwIfIncorrectNumber(number, base)
    throwIfIncorrectScale(scale)

    fun convertIntegerPart(integer: String, base: Int): BigDecimal {
        val baseBigDec = base.toBigDecimal()
        var decimal = BigDecimal.ZERO

        for ((i, num) in integer.withIndex()) {
            decimal += baseBigDec.pow(integer.length - 1 - i) * charToNum(num).toBigDecimal()
        }
        return decimal
    }

    fun convertFractionalPart(fraction: String, base: Int, scale: Int): BigDecimal {
        val baseBigDec = base.toBigDecimal()
        var result = BigDecimal.ZERO

        for ((i, num) in fraction.withIndex()) {
            result += charToNum(num).toBigDecimal().divide(baseBigDec.pow(i + 1), scale, RoundingMode.HALF_EVEN)
        }
        return result
    }

    if (!number.contains('.')) {
        return convertIntegerPart(number, base).stripTrailingZeros().toPlainString()
    }
    return (convertIntegerPart(number.substringBefore('.'), base)
            + convertFractionalPart(number.substringAfter('.'), base, scale)).toPlainString()
}

private val prefixes = mapOf(Pair(2, "0b"), Pair(8, "0"), Pair(16, "0x"))
fun addPrefix(num: String, base: Int, defaultPrefix: String = "") = prefixes.getOrDefault(base, defaultPrefix) + num

fun isNumberCorrect(number: String, base: Int): Boolean {
    val regexPart = when (base) {
        1 -> "[1]*"
        in 2..10 -> "[0-${base - 1}]+"
        in 11..36 -> "[0-9a-${'a' - 11 + base}]+"
        else -> throw IllegalArgumentException("Error: incorrect base \"$base\".")
    }
    return number.matches(Regex("($regexPart)?[.]?$regexPart"))
}

private fun splitNum(number: String) = Pair(number.substringBefore('.'), number.substring(number.indexOf('.')))

private fun charToNum(c: Char) = c.toString().toIntOrNull() ?: c - 'a' + 10

private fun numToChar(n: Int) = if (n < 10) n else 'a' - 10 + n

// exceptions
private fun throwIfIncorrectBase(base: Int) {
    if (base !in 1..36) throw IllegalArgumentException("Error: incorrect base \"$base\".")
}

private fun throwIfIncorrectNumber(number: String, base: Int) {
    if (!isNumberCorrect(number, base)) throw IllegalArgumentException("Error: incorrect number \"$number\".")
}

private fun throwIfIncorrectScale(scale: Int) {
    if (scale < 1) throw IllegalArgumentException("Error: scale must be >= 1")
}

private fun throwIfIncorrectFractionLen(scale: Int, fractionLen: Int) {
    if (fractionLen < 0) throw IllegalArgumentException("Error: fractionLen should be >= 0.")
    if (fractionLen > scale) throw IllegalArgumentException("Error: fractionLen should be <= scale.")
}
