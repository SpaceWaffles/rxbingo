package io.spacenoodles.rxbingo.model

import io.reactivex.Observable
import io.reactivex.Observer
import java.util.*
import java.util.concurrent.TimeUnit

class BingoGame {

    lateinit var gameObservable: Observable<Int>
    var numbers = (1..75).toList() as ArrayList
    private var gameLive = false
    var timeInterval = 2000L // Time between each number in milliseconds
    var maxRepetitions = -1
    private var reps = 0

    companion object {
        val NO_VALUE = 0
    }

    init {
        initGameObservable()
    }

    private fun initGameObservable() {
        val rand = Random()
        gameObservable = Observable.fromCallable {
                        pickNumber(rand)
                }
                .map {
                    if (!gameLive || maxRepetitions == 0)
                        NO_VALUE
                    else
                        it
                }
                .delay(timeInterval, TimeUnit.MILLISECONDS)
                .map { it ->
                    reps++
                    it
                }
                .repeat()
                .takeUntil {
                    maxRepetitions != -1 && reps >= maxRepetitions
                }
                .share()
                .replay()
                .autoConnect()
    }

    fun addSubscriber(subscriber: Observer<Int>) {
        gameObservable.subscribe(subscriber)
    }

    fun start() {
        gameLive = true
    }

    fun stop() {
        gameLive = false
    }

    fun newGame() {
        reps = 0
        numbers = (1..75).toList() as ArrayList
        gameLive = true
    }

    private fun pickNumber(rand: Random): Int {
        if (numbers.isEmpty()) return NO_VALUE
        val index = rand.nextInt(numbers.size)
        val returnVal = numbers[index]
        numbers.removeAt(index)
        return returnVal
    }
}