package converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class TestTopLvlFunctions {
    val one = 1
    val two = 2
    val four = 4
    val six = 6
    val eight = 8
    val ten = 10
    val sixteen = 16
    val twentyOne = 21
    val twentySeven = 27
    val thirtyThree = 33
    val thirtySix = 36

    @Test
    fun testDecimalTo() {
        fun testRange(range: LongRange, step: Long, base: Int) {
            for (i in range step step) {
                assertEquals(decimalTo(bi(i), base), i.toString(base))
            }
        }

        fun testBaseOne(range: IntRange, step: Int) {
            for (i in range step step)
                assertEquals(decimalTo(bi(i.toString()), one), "1".repeat(i))
        }

        // base 1
        testBaseOne(0..9999, 1)

        // base 2
        assertEquals(decimalTo(bi(0), two), "0")
        assertEquals(decimalTo(bi(7), two), "111")
        assertEquals(decimalTo(bi(17), two), "10001")
        assertEquals(decimalTo(bi(9999999), two), 9999999L.toString(two))
        assertEquals(decimalTo(bi(999999119), two), 999999119L.toString(two))
        assertEquals(decimalTo(bi(12349999999), two), 12349999999L.toString(two))
        assertEquals(decimalTo(bi("1234999999912349999999"), two), bi("1234999999912349999999").toString(two))
        testRange(0..10000L, 9, two)

        // base 6
        assertEquals(decimalTo(bi(0), six), "0")
        assertEquals(decimalTo(bi(5), six), "5")
        assertEquals(decimalTo(bi(6), six), "10")
        assertEquals(decimalTo(bi(17), six), 17L.toString(six))
        assertEquals(decimalTo(bi(99929999), six), 99929999L.toString(six))
        assertEquals(decimalTo(bi(19999119), six), 19999119L.toString(six))
        assertEquals(decimalTo(bi(123499999299), six), 123499999299L.toString(six))
        assertEquals(decimalTo(bi("3123499999991234999919"), six), bi("3123499999991234999919").toString(six))
        testRange(0..10000L, 6, six)

        // base 8
        assertEquals(decimalTo(bi(0), eight), "0")
        assertEquals(decimalTo(bi(7), eight), "7")
        assertEquals(decimalTo(bi(8), eight), "10")
        assertEquals(decimalTo(bi(63), eight), "77")
        assertEquals(decimalTo(bi(64), eight), "100")
        assertEquals(decimalTo(bi(65), eight), "101")
        assertEquals(decimalTo(bi(1234999992996), eight), 1234999992996L.toString(eight))
        assertEquals(decimalTo(bi("312349999999123499991933"), eight), bi("312349999999123499991933").toString(eight))
        testRange(0..99999L, 1, eight)

        // base 10
        testRange(0..9999L, 1, ten)
        testRange(99..99923478923947899L, 1149782343298, ten)

        // base 16
        testRange(0..10999L, 1, sixteen)

        // base 21
        testRange(0..99991L, 1, twentyOne)

        // base 27
        testRange(0..123749L, 1, twentySeven)

        // base 36
        testRange(0..999999L, 3, thirtySix)
    }


    @Test
    fun testToDecimal() {
        fun testRange(range: LongRange, step: Long, base: Int) {
            for (i in range step step) {
                assertEquals(toDecimal(i.toString(base), base), bi(i))
            }
        }

        fun testBaseOne(range: IntRange, step: Int) {
            for (i in range step step) {
                assertEquals(toDecimal("1".repeat(i), one), bi(i.toString()))
            }
        }

        // base 1
        testBaseOne(0..9999, 99)

        // base 2
        assertEquals(toDecimal("101", two), BigInteger.valueOf(5))
        testRange(0..999L, 1, two)

        // base 4
        testRange(0..999999L, 9, four)

        // base 8
        testRange(0..2798943L, 17, eight)

        // base 10
        testRange(0..10000L, 8, ten)

        // base 16
        testRange(0..923479234234L, 999999, sixteen)

        // base 33
        testRange(0..234978923L, 189382, thirtyThree)

        // base 36
        testRange(0..87342L, 1, thirtySix)
    }


    fun bi(num: Long): BigInteger {
        return num.toBigInteger()
    }

    fun bi(num: String): BigInteger {
        return num.toBigInteger()
    }
}