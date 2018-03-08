package io.spacenoodles.rxbingo

import io.spacenoodles.rxbingo.model.BingoBoard
import org.junit.Test

import org.junit.Assert.*

class BingoBoardUnitTest {

    @Test
    fun hasRowBingo() {
        val board = BingoBoard.generateRandomBoard()
        board[0][0].marked = true
        board[0][1].marked = true
        board[0][2].marked = true
        board[0][3].marked = true
        board[0][4].marked = true

        val boardWithRowMarkedTrue = BingoBoard(board)

        assertTrue(boardWithRowMarkedTrue.hasBingo())
    }

    @Test
    fun hasColumnBingo() {
        val board = BingoBoard.generateRandomBoard()
        board[0][0].marked = true
        board[1][0].marked = true
        board[2][0].marked = true
        board[3][0].marked = true
        board[4][0].marked = true

        val boardWithColumnMarkedTrue = BingoBoard(board)

        assertTrue(boardWithColumnMarkedTrue.hasBingo())
    }

    @Test
    fun hasDiagonalBingo() {
        val board = BingoBoard.generateRandomBoard()
        board[0][0].marked = true
        board[1][1].marked = true
        board[2][2].marked = true
        board[3][3].marked = true
        board[4][4].marked = true

        val boardWithDiagonalMarkedTrue = BingoBoard(board)

        assertTrue(boardWithDiagonalMarkedTrue.hasBingo())

        val board2 = BingoBoard.generateRandomBoard()
        board2[0][4].marked = true
        board2[1][3].marked = true
        board2[2][2].marked = true
        board2[3][1].marked = true
        board2[4][0].marked = true

        val boardWithDiagonalMarkedTrue2 = BingoBoard(board2)

        assertTrue(boardWithDiagonalMarkedTrue2.hasBingo())
    }
}
