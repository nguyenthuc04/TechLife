import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.data.model.UserCourse
import com.snapco.techlife.databinding.ItemRegisteredUserBinding
import com.snapco.techlife.extensions.loadImage

class RegisteredUsersAdapter(
    private var users: List<UserCourse>,
    private var ClickNT : ClickChat
) : RecyclerView.Adapter<RegisteredUsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(
        private val binding: ItemRegisteredUserBinding,

    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserCourse) {
            binding.userName.text = user.userName
            binding.userAvatar.loadImage(user.avatar)

            binding.imgChat.setOnClickListener {
                ClickNT.clickMess(user.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRegisteredUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<UserCourse>) {
        users = newUsers
        notifyDataSetChanged()
    }

    interface ClickChat {
        fun clickMess(id: String)
    }
}