import kotlin.system.measureTimeMillis

// Inspired by: https://youtu.be/oifN-YVlrq8?si=XHLXPNWBG4okBQ0y
// function which calculates the number of ways we can throw N with M dice

fun bruteForce(n: Int, m: Int): Int {
    var solution = 0
    val diceRoll = (1..n).map { 1 }.toMutableList()
    while (true) {
        if (diceRoll.sum() == m) solution++
        if (++diceRoll[n - 1] == 7) {
            diceRoll[n - 1] = 1
            for (i in (n - 2) downTo 0) {
                if (diceRoll[i] == 6) {
                    if (i == 0) return solution
                    diceRoll[i] = 1
                } else {
                    diceRoll[i]++
                    break
                }
            }
        }
    }
}


fun recursive(n: Int, m: Int): Int {
    if (n == 1) return if (m in 1..6) 1 else 0
    return (1..6).sumOf { recursive(n - 1, m - it) }
}


fun recursiveMemo(n: Int, m: Int): Int {
    val cache: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    fun rec(n: Int, m: Int): Int {
        if (n == 1) return if (m in 1..6) 1 else 0
        if (n to m in cache) return cache[n to m]!!
        val result =  (1..6).sumOf { rec(n - 1, m - it) }
        cache[n to m] = result
        return result
    }

    return rec(n, m)
}

fun dynamic(n: Int, m: Int): Int {
    val cache: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    for (i in 1..n) {
        for (j in 1..m) {
            cache[i to j] = if (i == 1) {
                if (j <= 6) 1 else 0
            } else {
                (1..6).filter { it < j }.sumOf { cache[i - 1 to j - it]!! }
            }
        }
    }
    return cache[n to m]!!
}


bruteForce(2, 7)
bruteForce(2, 12)
bruteForce(2, 8)
bruteForce(10, 28)
measureTimeMillis { bruteForce(10, 28) }

recursive(2, 7)
recursive(2, 12)
recursive(2, 8)
recursive(10, 28)
measureTimeMillis { recursive(10, 28) }

recursiveMemo(2, 7)
recursiveMemo(2, 12)
recursiveMemo(2, 8)
recursiveMemo(10, 28)
measureTimeMillis { recursiveMemo(10, 28) }

dynamic(2, 7)
dynamic(2, 12)
dynamic(2, 8)
dynamic(10, 28)
measureTimeMillis { dynamic(10, 28) }
