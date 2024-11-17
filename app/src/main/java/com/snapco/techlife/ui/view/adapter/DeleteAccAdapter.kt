package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.UserAccount
import com.snapco.techlife.databinding.ItemDeleteAccBinding
import com.snapco.techlife.extensions.loadImage

class DeleteAccAdapter(
    private var accounts: List<UserAccount>,
    private val onDeleteClick: (UserAccount) -> Unit,
) : RecyclerView.Adapter<DeleteAccAdapter.AccountViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AccountViewHolder {
        val binding = ItemDeleteAccBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int,
    ) {
        holder.bind(accounts[position])
    }

    override fun getItemCount(): Int = accounts.size

    fun updateAccounts(newAccounts: List<UserAccount>) {
        accounts = newAccounts
        notifyDataSetChanged()
    }

    class AccountViewHolder(
        private val binding: ItemDeleteAccBinding,
        private val onDeleteClick: (UserAccount) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: UserAccount) {
            binding.imgAvatar.loadImage(account.avatar)
            binding.textView3.text = account.name
            binding.buttonDelete.setOnClickListener { onDeleteClick(account) }
        }
    }
}
