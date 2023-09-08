package com.anlmk.base.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anlmk.base.R
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.InstalledApplicationInfo
import com.anlmk.base.databinding.AdapterHeaderTypeBinding
import com.anlmk.base.databinding.AdapterInformationAppBinding
import com.anlmk.base.databinding.AdapterMainMenuServiceBinding
import com.anlmk.base.extensions.setSafeOnClickListener

class CommonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val MENU_SERVICE = 101
        const val HEADER = 100
        const val INFO_APP = 102

    }

    var onClick: (Any) -> Unit = {}
    private val mDataSet = mutableListOf<MainMenuVHData>()

    class MainMenuVHData(val data: Any)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MENU_SERVICE -> {
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
            INFO_APP -> {
                InfoAppViewHolder(
                    AdapterInformationAppBinding.inflate(
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
                holder.onBind(data.data as CommonEntity)
            }
            is InfoAppViewHolder -> {
                holder.onBind(data.data as InstalledApplicationInfo)
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
        if (data is InstalledApplicationInfo){
            return INFO_APP
        }
        return super.getItemViewType(position)
    }


    inner class ServiceViewHolder(private val binding: AdapterMainMenuServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? CommonEntity
                if (data != null) {
                    onClick(data)
                }
            }
        }

        fun onBind(data: CommonEntity) {
            binding.imgFunctionLogo.setImageResource(data.getIcon())
            binding.txtFunctionName.text = data.getTitle()
            binding.txtFunctionDescription.text = data.getDescript()
        }
    }

    inner class HeaderViewHolder(private val binding: AdapterHeaderTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CommonEntity) {
            binding.title.text = data.getTitle()
        }
    }

    inner class InfoAppViewHolder(private val binding: AdapterInformationAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: InstalledApplicationInfo) {
            binding.imgAppLogo.setImageDrawable(data.iconApp)
            binding.txtAppName.text = data.appName
            binding.txtPackageName.text=data.packageName
            binding.txtStatus.text= data.statusApp
            binding.txtStatus.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    getColorStatusText(data.isSafeApp)
                )
            )
            binding.txtPercentageOfMalware.text = data.percentageOfMalware.toString()
        }

        init {
            binding.root.setSafeOnClickListener {
                val data = mDataSet[absoluteAdapterPosition].data as? InstalledApplicationInfo
                if (data != null) {
                    onClick(data)
                }
            }
        }
    }

    fun updateData(list: List<Any>?) {
        mDataSet.clear()
        list?.forEach {
            mDataSet.add(MainMenuVHData(it))
        }
        notifyDataSetChanged()
    }

    fun getColorStatusText(isSafe: Boolean): Int {
        return if (isSafe)
            R.color.color_4caf50
        else
            R.color.color_ed3432
    }
}