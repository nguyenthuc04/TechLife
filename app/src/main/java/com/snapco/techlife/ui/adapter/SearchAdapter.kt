import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapco.techlife.data.model.User
import com.snapco.techlife.databinding.ItemSearchUserBinding

class SearchAdapter(private var items: List<User>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSearchUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.binding.nameTextView.text = user.name
        holder.binding.emailTextView.text = user.email
        Glide.with(holder.itemView.context).load(user.avatar).into(holder.binding.avatarImageView)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<User>) {
        items = newItems
        notifyDataSetChanged()
    }
}