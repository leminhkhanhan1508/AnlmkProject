package com.anlmk.base.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.anlmk.base.R
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.MealsEntity
import com.anlmk.base.data.`object`.Session
import com.anlmk.base.databinding.AdapterHeaderTypeBinding
import com.anlmk.base.databinding.AdapterMainMenuServiceBinding
import com.anlmk.base.databinding.AdapterMealItemDetailTypeBinding
import com.anlmk.base.di.Common
import com.anlmk.base.extensions.loadBitmapFromInternalStorage
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.utils.Utils
import kotlinx.coroutines.*

class CommonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),SwipeToDeleteCallback {
    companion object {
        const val MEALS_HOME = 101
        const val HEADER = 100
        const val MEALS_ITEM_DETAIL = 102
    }

    var onClick: (Any?) -> Unit = {}
    var onSwipeDelete: (Any?) -> Unit = {}
    private var listener: ((Int?, Any?) -> Unit)? = null
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
            MEALS_ITEM_DETAIL -> {
                MealItemDetailViewHolder(
                    AdapterMealItemDetailTypeBinding.inflate(
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
            is MealItemDetailViewHolder -> {
                holder.onBind(data.data as Mealtime)
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
        if (data is Mealtime) {
            return MEALS_ITEM_DETAIL
        }
        return super.getItemViewType(position)
    }


    inner class ServiceViewHolder(private val binding: AdapterMainMenuServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.txtDateOfMeal.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? MealsEntity
                if (data != null) {
                    onClick(data)
                }
            }
            binding.llBreakfast.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? MealsEntity
                listener?.invoke(Session.getValueSession(Session.Morning), data)
            }
            binding.llLunch.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? MealsEntity
                listener?.invoke(Session.getValueSession(Session.Afternoon), data)

            }
            binding.llDinner.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? MealsEntity
                listener?.invoke(Session.getValueSession(Session.Night), data)
            }
        }

        fun onBind(data: MealsEntity) {
            binding.txtDateOfMeal.text = data.dateOfMeal
            binding.llLunch.txtSession.text =
                binding.llLunch.txtSession.context.getString(R.string.afternoon)
            binding.llBreakfast.txtSession.text =
                binding.llBreakfast.txtSession.context.getString(R.string.morning)
            binding.llDinner.txtSession.text =
                binding.llDinner.txtSession.context.getString(R.string.night)
            if (data.mealBreakfast != null) {
                binding.llBreakfast.txtTime.text = Utils.getTimeFormat().format(data.mealBreakfast?.timeOfMeal)
                binding.llBreakfast.txtFood.text = data.mealBreakfast?.foodOfMeal
                binding.llBreakfast.txtValueMod.text = Common.currentActivity.getString(
                    R.string.value_mmol,
                    data.mealBreakfast?.molOfFood
                )
            }
            if (data.mealLunch != null) {
                binding.llLunch.txtTime.text = Utils.getTimeFormat().format(data.mealLunch?.timeOfMeal)
                binding.llLunch.txtFood.text = data.mealLunch?.foodOfMeal
                binding.llLunch.txtValueMod.text = Common.currentActivity.getString(R.string.value_mmol, data.mealLunch?.molOfFood)
            }
            if (data.mealDinner != null) {
                binding.llDinner.txtTime.text = Utils.getTimeFormat().format(data.mealDinner?.timeOfMeal)
                binding.llDinner.txtFood.text = data.mealDinner?.foodOfMeal
                binding.llDinner.txtValueMod.text = Common.currentActivity.getString(
                    R.string.value_mmol,
                    data.mealDinner?.molOfFood
                )
            }
        }
    }

    inner class HeaderViewHolder(private val binding: AdapterHeaderTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CommonEntity) {
            binding.title.text = data.getTitle()
        }
    }

    inner class MealItemDetailViewHolder(private val binding: AdapterMealItemDetailTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? Mealtime
                if (data != null) {
                    onClick(data)
                }
            }
        }

        fun onBind(data: Mealtime) {
            if (!data.imageOfFood.isNullOrEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val imageOfMeal =
                        Common.currentActivity.loadBitmapFromInternalStorage(data.imageOfFood ?: "")
                    // Update the UI with the loaded image on the main thread
                    withContext(Dispatchers.Main) {
                        binding.imgImageOfMeal.setImageBitmap(imageOfMeal)
                    }
                }
            }
            binding.txtSession.text = data.sessionName ?: ""
            binding.txtTime.text = Utils.getTimeFormat().format(data.timeOfMeal)
            binding.txtFood.text = data.foodOfMeal
            binding.txtValueMod.text = Common.currentActivity.getString(R.string.value_mmol, data.molOfFood)


        }
    }

    fun updateData(list: List<Any>?) {
        mDataSet.clear()
        list?.forEach {
            mDataSet.add(MainMenuVHData(it))
        }
        notifyDataSetChanged()
    }

    fun setItemClick(listener: (Int?, Any?) -> Unit) {
        this.listener = listener
    }

    fun deleteItem(position: Int) {
        val data = mDataSet[position].data
        onSwiped(position)
        onSwipeDelete(data)
    }

    override fun onSwiped(position: Int) {
        mDataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    class SwipeToDeleteCallback(private val adapter: CommonAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Not needed for swipe-to-delete
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.deleteItem(position)
        }
    }


}
interface SwipeToDeleteCallback {
    fun onSwiped(position: Int)
}
