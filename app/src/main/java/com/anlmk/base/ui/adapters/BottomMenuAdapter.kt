package com.anlmk.base.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.MealsEntity
import com.anlmk.base.databinding.AdapterHeaderTypeBinding
import com.anlmk.base.databinding.AdapterItemMenuBinding
import com.anlmk.base.databinding.AdapterMainMenuServiceBinding
import com.anlmk.base.extensions.setSafeOnClickListener

class BottomMenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM = 101
        const val HEADER = 100
    }

    var onClick: (Any) -> Unit = {}
    private val mDataSet = mutableListOf<MainMenuVHData>()

    class MainMenuVHData(val data: Any)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> {
                ItemMenuViewHolder(
                    AdapterItemMenuBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            HEADER -> {
                HeaderViewHolder(
                    AdapterHeaderTypeBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> {
                ItemMenuViewHolder(
                    AdapterItemMenuBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mDataSet[position]
        when (holder) {
            is HeaderViewHolder -> {
                holder.onBind(data.data as CommonEntity)
            }
            is ItemMenuViewHolder -> {
                holder.onBind(data.data as CommonEntity)
            }
        }

    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        val data = mDataSet[position].data
        if (data is CommonEntity) {
            return data.getTypeLayout()
        }
        return super.getItemViewType(position)
    }


    inner class ItemMenuViewHolder(private val binding: AdapterItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? CommonEntity
                if (data != null) {
                    onClick(data)
                }
            }
        }
        fun onBind(data: CommonEntity) {
            binding.txtItemMenu.text = data.getTitle()
            binding.viewBottom.visibility = if (absoluteAdapterPosition == mDataSet.size - 1) View.GONE else View.VISIBLE
        }
    }

    inner class HeaderViewHolder(private val binding: AdapterHeaderTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CommonEntity) {
            binding.title.text = data.getTitle()
        }
    }

    fun updateData(list: List<Any>?) {
        mDataSet.clear()
        list?.forEach {
            mDataSet.add(MainMenuVHData(it))
        }
        notifyDataSetChanged()
    }

}