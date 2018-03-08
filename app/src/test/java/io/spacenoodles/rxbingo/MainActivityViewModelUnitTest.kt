package io.spacenoodles.rxbingo

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.spacenoodles.rxbingo.model.BingoBoard
import io.spacenoodles.rxbingo.model.BingoGame
import io.spacenoodles.rxbingo.model.Player
import io.spacenoodles.rxbingo.view.BingoRecyclerViewAdapter
import io.spacenoodles.rxbingo.viewmodel.MainActivityViewModel
import io.spacenoodles.rxbingo.viewmodel.StateEvent
import io.spacenoodles.rxbingo.viewmodel.Status
import org.junit.Assert.assertEquals
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.mock

class MainActivityViewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Mock lateinit var observer: Observer<StateEvent>
    @Mock lateinit var bingoGame: BingoGame
    lateinit var mainActivityViewModel: MainActivityViewModel

    @Test
    fun checkForWin() {
        //Given
        bingoGame = BingoGame()
        bingoGame.maxRepetitions = 5
        bingoGame.timeInterval = 0
        bingoGame.numbers = arrayListOf(1,2,3,4,5)
        bingoGame.start()
        mainActivityViewModel = MainActivityViewModel(bingoGame)

        var players = ArrayList<Player>()

        mainActivityViewModel.bingoListAdapter = mock<BingoRecyclerViewAdapter> {
            on { items } doReturn players
        }

        observer = mock()
        mainActivityViewModel.state.observeForever(observer)

        val squares = BingoBoard.generateRandomBoard()
        squares[0][0].number = 1
        squares[0][1].number = 2
        squares[0][2].number = 3
        squares[0][3].number = 4
        squares[0][4].number = 5

        val board = BingoBoard(squares)

        //When
        players.add(Player("Fred", board))
        mainActivityViewModel.addPlayer("Fred", board)

        //Then
        assertEquals(Status.PLAYER_WINS, mainActivityViewModel.state.value?.status)
        assertEquals("Fred", mainActivityViewModel.state.value?.player?.name)
    }

    @Test
    fun checkForNewNumber() {
        //Given
        bingoGame = BingoGame()
        bingoGame.maxRepetitions = 2
        bingoGame.timeInterval = 0
        bingoGame.numbers = arrayListOf(1,1,1,1,1)
        bingoGame.start()
        mainActivityViewModel = MainActivityViewModel(bingoGame)

        var players = ArrayList<Player>()

        mainActivityViewModel.bingoListAdapter = mock<BingoRecyclerViewAdapter> {
            on { items } doReturn players
        }

        observer = mock()
        mainActivityViewModel.state.observeForever(observer)

        //When
        players.add(Player("Fred", BingoBoard.new()))
        mainActivityViewModel.addPlayer("Fred")

        //Then
        assertEquals(Status.NEW_NUMBER, mainActivityViewModel.state.value?.status)
        assertEquals(1, mainActivityViewModel.state.value?.number)
    }

    @Test
    fun checkForNewPlayer() {
        //Given
        bingoGame = BingoGame()
        bingoGame.maxRepetitions = 0
        bingoGame.timeInterval = 0
        bingoGame.numbers = arrayListOf(1,1,1,1,1)
        bingoGame.start()
        mainActivityViewModel = MainActivityViewModel(bingoGame)

        var players = ArrayList<Player>()

        mainActivityViewModel.bingoListAdapter = mock<BingoRecyclerViewAdapter> {
            on { items } doReturn players
        }

        observer = mock()
        mainActivityViewModel.state.observeForever(observer)

        //When
        players.add(Player("Fred", BingoBoard.new()))
        mainActivityViewModel.addPlayer("Fred")

        //Then
        assertEquals(Status.NEW_PLAYER, mainActivityViewModel.state.value?.status)
        assertEquals("Fred", mainActivityViewModel.state.value?.player?.name)
    }
}