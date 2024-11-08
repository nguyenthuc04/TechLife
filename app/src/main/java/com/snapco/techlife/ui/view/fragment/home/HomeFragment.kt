import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.adapter.home.PostAdapter
import com.snapco.techlife.data.model.home.Post
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.ui.viewmodel.home.SharedViewModel

class HomeFragment : Fragment(), PostAdapter.OnPostActionListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var postRecyclerView: RecyclerView
    private val postModelList = mutableListOf<Post>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        postRecyclerView = binding.recyclerViewId

        // Observe changes from ViewModel
        sharedViewModel.newPost.observe(viewLifecycleOwner) { newPost ->
            newPost?.let {
                postModelList.add(0, it)
                postAdapter.notifyDataSetChanged()
                sharedViewModel.clearNewPost() // Clear new post data after adding
            }
        }
        setupPostList()
        return binding.root
    }

    private fun setupPostList() {
        // Populate the list with initial data if needed
        if (postModelList.isEmpty()) {
            postModelList.addAll(
                listOf(
                    Post(
                        postId = 1,
                        caption = "Hello, have a nice day",
                        imageUrl = "https://example.com/image1.jpg",
                        createdAt = "10/7/2023",
                        likesCount = 10,
                        commentsCount = 3,
                        userId = 101,
                        userName = "Jack",
                        userImageUrl = "https://example.com/profile1.jpg",
                        isLiked = false,
                        isOwnPost = true
                    ),
                    Post(
                        postId = 2,
                        caption = "Enjoying the day!",
                        imageUrl = "https://example.com/image2.jpg",
                        createdAt = "18/7/2023",
                        likesCount = 15,
                        commentsCount = 5,
                        userId = 102,
                        userName = "Alina",
                        userImageUrl = "https://example.com/profile2.jpg",
                        isLiked = true,
                        isOwnPost = false
                    )
                )
            )
        }

        postAdapter = PostAdapter(postModelList, this)
        postRecyclerView.adapter = postAdapter
        postAdapter.notifyDataSetChanged()
    }

    override fun onPostLongClicked(position: Int) {
        showPostOptionsDialog(position)
    }

    override fun onEditPost(position: Int) {
        showEditDialog(position)
    }

    override fun onDeletePost(position: Int) {
        if (position >= 0 && position < postModelList.size) {
            postModelList.removeAt(position)
            postAdapter.notifyItemRemoved(position)
            sharedViewModel.deletePost(position)
        } else {
            Log.e("HomeFragment", "Invalid position for deletion.")
        }
    }

    private fun showPostOptionsDialog(position: Int) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(requireContext())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditDialog(position)
                    1 -> deletePost(position)
                }
            }
            .show()
    }

    private fun showEditDialog(position: Int) {
        val currentPost = postModelList[position]

        val editText = EditText(context)
        editText.setText(currentPost.caption)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val updatedCaption = editText.text.toString()
                val updatedPost = currentPost.copy(caption = updatedCaption)

                postModelList[position] = updatedPost
                postAdapter.notifyItemChanged(position)

                sharedViewModel.updatePost(updatedPost, position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePost(position: Int) {
        postModelList.removeAt(position)
        postAdapter.notifyItemRemoved(position)
        sharedViewModel.deletePost(position)
    }
}
