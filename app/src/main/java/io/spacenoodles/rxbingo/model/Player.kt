package io.spacenoodles.rxbingo.model

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

data class Player(val name: String, private var bingoBoard: BingoBoard) : Observer<Int> {

    override fun onError(e: Throwable) {
        //TODO: Handle error
    }

    override fun onNext(t: Int) {
        markSquaresIfMatch(t)
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    fun markSquaresIfMatch(number: Int) {
        bingoBoard.getMatchingSquares(number)
                .forEach {
                    bingoBoard.markSquare(it.row, it.column)
                }
    }

    fun getBingoBoard() = bingoBoard

    fun newGame(bingoBoard: BingoBoard) {
        this.bingoBoard = bingoBoard
    }
}
