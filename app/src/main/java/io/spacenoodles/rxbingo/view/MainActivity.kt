package io.spacenoodles.rxbingo.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import io.spacenoodles.rxbingo.App
import io.spacenoodles.rxbingo.R
import io.spacenoodles.rxbingo.util.toastLong
import io.spacenoodles.rxbingo.viewmodel.MainActivityViewModel
import io.spacenoodles.rxbingo.viewmodel.StateEvent
import io.spacenoodles.rxbingo.viewmodel.Status
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var names: ArrayList<String>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        initDagger()
        initViewModel()
        initData()
        initLayout()
        initSubscriptions()
    }

    private fun initDagger() {
        App.component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private fun initData() {
        names = arrayListOf("Gerald", "Bertha", "Picard", "Susan", "Sam", "Dave", "Pete", "Clarisa", "Brendan",
                "Bob", "Freeman", "Luke", "Mike", "Jason", "Julia", "Ethel", "Rosa", "Winnefred", "Hannah", "Mr. S", "Georgio", "Anna")
    }

    private fun initLayout() {
        val layoutManager = GridLayoutManager(this, 2)
        bingo_list.layoutManager = layoutManager
        bingo_list.adapter = viewModel.bingoListAdapter
    }

    private fun initSubscriptions() {
        val rand = Random()
        viewModel.state.observe(this, Observer<StateEvent> {
            it?.let {
                update(it)
            }
        })

        add_player_button.setOnClickListener {
            if (names.isNotEmpty())
                viewModel.addPlayer(generatePlayerName(rand))
            else
                add_player_button.isEnabled = false
        }

        new_game_button.setOnClickListener {
            number.text = ""
            viewModel.newGame()
        }
    }

    private fun update(state: StateEvent) {
        when (state.status) {
            Status.NEW_PLAYER -> {
                toastLong(state.player?.name + " has joined the game.")
            }

            Status.NEW_NUMBER -> {
                number.text = state.number.toString()
            }

            Status.PLAYER_WINS -> {
                number.text = state.number.toString()
                toastLong(state.player?.name + " Wins!!")
            }

            Status.ERROR -> {
                Log.e("MainActivity: ", state.error?.localizedMessage)
            }
        }
    }

    private fun generatePlayerName(rand: Random): String {
        val index = rand.nextInt(names.size)
        val name = names[index]
        names.removeAt(index)
        return name
    }
}
