package com.snapco.techlife.ui.view.fragment.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snapco.techlife.databinding.BottomSheetAvatarBinding
import com.snapco.techlife.databinding.BottomSheetItemBinding

class BottomSheetProfielAvatar : BottomSheetDialogFragment() {
    private lateinit var items: List<BottomSheetItem>

    fun setItems(items: List<BottomSheetItem>) {
        this.items = items
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = BottomSheetAvatarBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = BottomSheetAdapter(items)
        return binding.root
    }

    private inner class BottomSheetAdapter(
        private val items: List<BottomSheetItem>,
    ) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {
        inner class ViewHolder(
            private val binding: BottomSheetItemBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: BottomSheetItem) {
                binding.imageView.setImageResource(item.iconResId)
                binding.textView.text = item.text
                binding.root.setOnClickListener {
                    item.onClick()
                    dismiss() // Đóng BottomSheet khi item được chọn
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val binding =
                BottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }
}

data class BottomSheetItem(
    val iconResId: Int,
    val text: String,
    val onClick: () -> Unit,
)
