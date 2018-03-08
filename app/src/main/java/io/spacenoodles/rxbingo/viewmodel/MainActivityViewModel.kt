package io.spacenoodles.rxbingo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.spacenoodles.rxbingo.model.BingoBoard
import io.spacenoodles.rxbingo.model.BingoGame
import io.spacenoodles.rxbingo.model.BingoGame.Companion.NO_VALUE
import io.spacenoodles.rxbingo.model.Player
import io.spacenoodles.rxbingo.view.BingoRecyclerViewAdapter
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val bingoGame: BingoGame) : ViewModel() {

    val state: MutableLiveData<StateEvent> = MutableLiveData()

    var bingoListAdapter = BingoRecyclerViewAdapter(ArrayList())

    private lateinit var disposables: CompositeDisposable

    init {
        initRx()
        if (bingoListAdapter.items.isNotEmpty())
            initSubscriptions()
    }

    private fun initRx() {
        disposables = CompositeDisposable()
    }

    private fun initSubscriptions() {
        addSub(
            bingoGame
                    .gameObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ number ->
                        if (number != NO_VALUE) {
                            state.postValue(StateEvent.newNumber(number))
                            bingoListAdapter.notifyDataSetChanged()
                            var hasWinner = false
                            bingoListAdapter.items.forEach {
                                if (it.getBingoBoard().hasBingo()) {
                                    hasWinner = true
                                    state.postValue(StateEvent.playerWins(it, number))
                                }
                            }
                            if (hasWinner)
                                bingoGame.stop()
                        }
                    },
                    {
                        state.postValue(StateEvent.error(it))
                    })
        )
    }

    fun addPlayer(name: String, board: BingoBoard? = null) {

        val player = if (board != null)
                        Player(name, board)
                    else
                        Player(name, BingoBoard.new())
        bingoListAdapter.items = (bingoListAdapter.items.plus(player))
        bingoListAdapter.notifyDataSetChanged()
        state.value = StateEvent.newPlayer(player)
        bingoGame.addSubscriber(player)

        if (bingoListAdapter.items.size == 1) {
            initSubscriptions()
            bingoGame.start()
        }
    }

    fun newGame() {
        bingoGame.newGame()
        bingoListAdapter.items.forEach {
            it.newGame(BingoBoard.new())
        }
        bingoListAdapter.notifyDataSetChanged()
    }

    @Synchronized
    private fun addSub(disposable: Disposable?) {
        if (disposable == null) return
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }
}