package io.spacenoodles.rxbingo.model

import io.spacenoodles.rxbingo.view.BingoView
import java.util.*
import kotlin.collections.ArrayList

class BingoBoard(board: Array<Array<BingoView.BingoSquare>>? = null) {

    private val numbers = board ?: generateRandomBoard()
    companion object {
        private val FREE = 0 //Free space for middle square of board

        fun new(): BingoBoard {
            return BingoBoard(generateRandomBoard())
        }

        fun generateRandomBoard(): Array<Array<BingoView.BingoSquare>> {
            val rand = Random()
            val randomBoard = Array(5) { Array(5){ BingoView.BingoSquare(0, 0, 0) }}
            var col1 = (1..15).toList() as ArrayList
            var col2 = (16..30).toList() as ArrayList
            var col3 = (31..45).toList() as ArrayList
            var col4 = (46..60).toList() as ArrayList
            var col5 = (61..75).toList() as ArrayList
            var num = 0
            Array(5) { row ->
                Array(5) { col ->
                    when (col) {
                        0 -> num = pickNumber(rand, col1)
                        1 -> num = pickNumber(rand, col2)
                        2 -> num = pickNumber(rand, col3)
                        3 -> num = pickNumber(rand, col4)
                        4 -> num = pickNumber(rand, col5)
                    }
                    randomBoard[row][col] = BingoView.BingoSquare(num, row, col)
                }
            }
            randomBoard[2][2] = BingoView.BingoSquare(FREE, 2, 2, true) //Set middle square to free
            return randomBoard
        }

        private fun pickNumber(rand: Random, list: ArrayList<Int>): Int {
            val index = rand.nextInt(list.size)
            val num = list[index]
            list.removeAt(index)
            return num
        }
    }

    fun markSquare(row: Int, col: Int) {
        numbers[row][col].marked = true
    }

    fun getMatchingSquares(number: Int): ArrayList<BingoView.BingoSquare> {
        val matches = ArrayList<BingoView.BingoSquare>()
        numbers.map {
            matches.addAll(
                it.filter {
                    it.number == number
                }
            )
        }
        return matches
    }

    fun hasBingo(): Boolean {
        val rowBingo = numbers.any { row ->
            row.all { it.marked }
        }

        val colBingo = (0..4).any {
            numbers[0][it].marked &&
            numbers[1][it].marked &&
            numbers[2][it].marked &&
            numbers[3][it].marked &&
            numbers[4][it].marked
        }

        val diagonalBingo = (0..4).all { numbers[it][it].marked } ||
        (0..4).all { numbers[it][4-it].marked }

        return rowBingo || colBingo || diagonalBingo
    }

    fun getBingoSquares() = numbers
}