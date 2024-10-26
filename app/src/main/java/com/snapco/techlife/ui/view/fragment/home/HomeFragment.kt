import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.adapter.home.FeedAdapter
import com.snapco.techlife.data.model.home.Feed
import com.snapco.techlife.databinding.FragmentHomeBinding
import com.snapco.techlife.ui.view.fragment.home.CreatePostFragment
import com.snapco.techlife.ui.viewmodel.home.SharedViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var feedRecyclerView: RecyclerView
    private val feedModelList = mutableListOf<Feed>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // Initialize RecyclerViews
        feedRecyclerView = binding.recyclerViewId

        // Set up feed list
        sharedViewModel.newPost.observe(viewLifecycleOwner) { newPost ->
            newPost?.let {
                feedModelList.add(0, it)
                feedAdapter.notifyDataSetChanged()
                sharedViewModel.clearNewPost() // Xóa dữ liệu sau khi cập nhật
            }
        }
        setupFeedList()
        return binding.root
    }

    private fun setupFeedList() {
        // Initial feed data
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
                    Feed(R.drawable.profile4,
                        "Alina",
                        "USA",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMdl3HTGdSrPPtE1intiEqAGncJF0-HAyL6VpjWlBNG_wsroaBdglQkhczbEJ6rt5MeCg&usqp=CAU",
                        "Hello, have a nice day",
                        "8",
                        "18/7/2023"),
                    Feed(R.drawable.profile3,
                        "Mariya",
                        "USA",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6bQFqMhQmg9hJ-FA5xUUrQidHgQqZC5Nktw&usqp=CAU",
                        "Hello, have a nice day",
                        "13",
                        "1/7/2023")
                    // Additional feed data here...
                )
            )
        }

        feedAdapter = FeedAdapter(feedModelList)
        feedRecyclerView.adapter = feedAdapter
        feedAdapter.notifyDataSetChanged()
    }
}
