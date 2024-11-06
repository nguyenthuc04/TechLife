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
import com.snapco.techlife.adapter.home.FeedAdapter
import com.snapco.techlife.data.model.home.Feed
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.ui.viewmodel.home.SharedViewModel

class HomeFragment : Fragment(), FeedAdapter.OnPostActionListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var feedRecyclerView: RecyclerView
    private val feedModelList = mutableListOf<Feed>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        feedRecyclerView = binding.recyclerViewId

        sharedViewModel.newPost.observe(viewLifecycleOwner) { newPost ->
            newPost?.let {
                feedModelList.add(0, it)
                feedAdapter.notifyDataSetChanged()
                sharedViewModel.clearNewPost() // Clear new post data after adding it
            }
        }
        setupFeedList()
        return binding.root
    }

    private fun setupFeedList() {
        if (feedModelList.isEmpty()) {
            feedModelList.addAll(
                listOf(
                    Feed(
                        R.drawable.profile2,
                        "Jack",
                        "USA",
                        "https://marketplace.canva.com/EAFH_oMBen8/1/0/900w/canva-gray-and-white-asthetic-friend-instagram-story-C5KpyJG5MHA.jpg",
                        "Hello, have a nice day",
                        "3",
                        "10/7/2023"
                    ),
                    Feed(
                        R.drawable.profile4,
                        "Alina",
                        "USA",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMdl3HTGdSrPPtE1intiEqAGncJF0-HAyL6VpjWlBNG_wsroaBdglQkhczbEJ6rt5MeCg&usqp=CAU",
                        "Hello, have a nice day",
                        "8",
                        "18/7/2023"
                    ),
                    Feed(
                        R.drawable.profile3,
                        "Mariya",
                        "USA",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6bQFqMhQmg9hJ-FA5xUUrQidHgQqZC5Nktw&usqp=CAU",
                        "Hello, have a nice day",
                        "13",
                        "1/7/2023"
                    )
                )
            )
        }

        feedAdapter = FeedAdapter(feedModelList, this)
        feedRecyclerView.adapter = feedAdapter
        feedAdapter.notifyDataSetChanged()
    }

    override fun onPostLongClicked(position: Int) {
        showPostOptionsDialog(position)
    }

    override fun onEditPost(position: Int) {
        showEditDialog(position)  // Pass the position here
    }

    override fun onDeletePost(position: Int) {
        if (position >= 0 && position < feedModelList.size) {
            feedModelList.removeAt(position)  // Remove the post at the specified position
            feedAdapter.notifyItemRemoved(position)  // Notify the adapter about the removed item

            // Delete the post from the ViewModel as well
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
                    0 -> showEditDialog(position) // Pass position to showEditDialog
                    1 -> deletePost(position)
                }
            }
            .show()
    }

    private fun showEditDialog(position: Int) {
        val currentPost = feedModelList[position]

        // Assuming you're showing an EditText to edit the status
        val editText = EditText(context)
        editText.setText(currentPost.status) // Set current status text as default

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(editText)
            .setPositiveButton("Update") { dialog: DialogInterface?, which: Int ->
                val updatedStatus = editText.text.toString()
                val updatedPost =
                    currentPost.withUpdatedStatus(updatedStatus) // Use the custom method here

                feedModelList[position] = updatedPost
                feedAdapter.notifyItemChanged(position)

                sharedViewModel.updatePost(updatedPost, position) // Update the ViewModel
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun deletePost(position: Int) {
        feedModelList.removeAt(position)
        feedAdapter.notifyItemRemoved(position)

        sharedViewModel.deletePost(position)
    }
}
