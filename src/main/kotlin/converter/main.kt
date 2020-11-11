package converter

fun main() {
    run()
}

// Controller
fun run() {
    val (srcBase, num, trgBase) = getInput()
    showResult(
        try {
            addPrefix(convert(srcBase, num, trgBase, fractionLen = 5), trgBase)
        } catch (e: IllegalArgumentException) {
            e.message ?: e.stackTraceToString()
        }
    )
}

// View
fun getInput(): Triple<Int, String, Int> = getBase().let { Triple(it, getNumber(it), getBase()) }

fun showResult(result: String) = println(result)

private fun getBase(): Int {
    while (true) {
        when (val num = readLine()!!.toIntOrNull()) {
            null -> println("Error: incorrect number, please try again.")
            !in 1..36 -> println("Error: incorrect base, please try again.")
            else -> return num
        }
    }
}

private fun getNumber(base: Int): String {
    var num = readLine()!!
    while (!isNumberCorrect(num, base)) {
        println("Error: incorrect number, please try again.")
        num = readLine()!!
    }
    return num
}