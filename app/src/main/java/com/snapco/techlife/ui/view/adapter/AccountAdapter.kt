package com.snapco.techlife.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.UserAccount
import com.snapco.techlife.databinding.ItemListAccBinding
import com.snapco.techlife.extensions.loadImage

class AccountAdapter(
    private var accounts: List<UserAccount>,
    private val onItemClick: (UserAccount) -> Unit,
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AccountViewHolder {
        val binding = ItemListAccBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding, onItemClick)
    }

    fun updateAccounts(newAccounts: List<UserAccount>) {
        accounts = newAccounts
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int,
    ) {
        holder.bind(accounts[position])
    }

    override fun getItemCount(): Int = accounts.size

    class AccountViewHolder(
        private val binding: ItemListAccBinding,
        private val onItemClick: (UserAccount) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: UserAccount) {
            binding.textView3.text = account.name
            binding.imgAvatar.loadImage(account.avatar)
            binding.root.setOnClickListener { onItemClick(account) }
        }
    }
}
