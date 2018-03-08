package io.spacenoodles.rxbingo.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.spacenoodles.rxbingo.R
import io.spacenoodles.rxbingo.model.Player
import io.spacenoodles.rxbingo.util.inflate
import kotlinx.android.synthetic.main.item_bingo.view.*

class BingoRecyclerViewAdapter(var items: List<Player>) : RecyclerView.Adapter<BingoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_bingo))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Player) {
            itemView.bingo_view.setSquares(item.getBingoBoard().getBingoSquares())
            itemView.bingo_view.setPlayerName(item.name)
        }
    }
}