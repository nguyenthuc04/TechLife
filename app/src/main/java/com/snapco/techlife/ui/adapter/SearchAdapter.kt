import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapco.techlife.data.model.SearchUserResponse
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ItemSearchUserBinding

class SearchAdapter(
    private var items: List<SearchUserResponse>,
    private val onItemClick: (SearchUserResponse) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: SearchUserResponse) {
            binding.nameTextView.text = user.name
            binding.emailTextView.text = user.account
            Glide.with(binding.avatarImageView.context).load(user.avatar)
                .into(binding.avatarImageView)
            binding.root.setOnClickListener {
                onItemClick(user)
                Log.d("SearchAdapter", "User clicked: $user")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<SearchUserResponse>) {
        items = newItems
        notifyDataSetChanged()
    }
}