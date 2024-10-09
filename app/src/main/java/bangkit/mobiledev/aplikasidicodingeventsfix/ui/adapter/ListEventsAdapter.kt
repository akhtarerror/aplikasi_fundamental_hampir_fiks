package bangkit.mobiledev.aplikasidicodingeventsfix.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import bangkit.mobiledev.aplikasidicodingeventsfix.data.response.ListEventsItem
import bangkit.mobiledev.aplikasidicodingeventsfix.databinding.ItemListEventsBinding
import bangkit.mobiledev.aplikasidicodingeventsfix.ui.detailevent.DetailEventActivity

class ListEventsAdapter: ListAdapter<ListEventsItem, ListEventsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemListEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.textViewName.text = event.name
            Glide.with(binding.imageView.context)
                .load(event.imageLogo)
                .into(binding.imageView)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java)
                Log.e("kirimdata","erro ${event.id}")
                intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
                itemView.context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}