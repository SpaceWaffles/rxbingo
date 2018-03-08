package io.spacenoodles.rxbingo.viewmodel

import io.spacenoodles.rxbingo.model.Player

data class StateEvent private constructor(val status: Status,
                                          val player: Player? = null,
                                          val number: Int? = null,
                                          val error: Throwable? = null) {
    companion object {

        fun newPlayer(player: Player): StateEvent {
            return StateEvent(Status.NEW_PLAYER, player = player)
        }

        fun newNumber(number: Int): StateEvent {
            return StateEvent(Status.NEW_NUMBER, number = number)
        }

        fun playerWins(player: Player, number: Int): StateEvent {
            return StateEvent(Status.PLAYER_WINS, player = player, number = number)
        }

        fun error(error: Throwable): StateEvent {
            return StateEvent(Status.ERROR, error = error)
        }
    }
}