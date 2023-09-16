package com.anlmk.base.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.MealsEntity
import com.anlmk.base.data.`object`.Session
import com.anlmk.base.databinding.AdapterHeaderTypeBinding
import com.anlmk.base.databinding.AdapterMainMenuServiceBinding
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.utils.Utils

class CommonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val MEALS_HOME = 101
        const val HEADER = 100
    }

    var onClick: (Any) -> Unit = {}
    private val mDataSet = mutableListOf<MainMenuVHData>()

    class MainMenuVHData(val data: Any)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MEALS_HOME -> {
                ServiceViewHolder(
                    AdapterMainMenuServiceBinding.inflate(
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
                ServiceViewHolder(
                    AdapterMainMenuServiceBinding.inflate(
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
            is ServiceViewHolder -> {
                holder.onBind(data.data as MealsEntity)
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


    inner class ServiceViewHolder(private val binding: AdapterMainMenuServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? MealsEntity
                if (data != null) {
                    onClick(data)
                }
            }
        }

        fun onBind(data: MealsEntity) {
            binding.txtDateOfMeal.text = data.dateOfMeal
            if (data.mealBreakfast != null) {
                binding.llBreakfast.txtSession.text = data.mealBreakfast?.sessionName ?: ""
                binding.llBreakfast.txtTime.text =
                    Utils.getTimeFormat().format(data.mealBreakfast?.timeOfMeal)
                binding.llBreakfast.txtFood.text = data.mealBreakfast?.foodOfMeal
                binding.llBreakfast.txtValueMod.text = data.mealBreakfast?.molOfFood
            }
            if (data.mealLunch != null) {
                binding.llLunch.txtSession.text = data.mealLunch?.sessionName ?: ""
                binding.llLunch.txtTime.text =
                    Utils.getTimeFormat().format(data.mealLunch?.timeOfMeal)
                binding.llLunch.txtFood.text = data.mealLunch?.foodOfMeal
                binding.llLunch.txtValueMod.text = data.mealLunch?.molOfFood
            }
            if (data.mealDinner != null) {
                binding.llDinner.txtSession.text = data.mealDinner?.sessionName ?: ""
                binding.llDinner.txtTime.text =
                    Utils.getTimeFormat().format(data.mealDinner?.timeOfMeal)
                binding.llDinner.txtFood.text = data.mealDinner?.foodOfMeal
                binding.llDinner.txtValueMod.text = data.mealDinner?.molOfFood
            }
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