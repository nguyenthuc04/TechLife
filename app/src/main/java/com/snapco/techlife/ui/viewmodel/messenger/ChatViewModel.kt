import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.ChatEvent
import io.getstream.chat.android.client.events.NotificationMarkReadEvent
import io.getstream.chat.android.client.events.NotificationMessageNewEvent
import io.getstream.chat.android.client.utils.observable.Disposable
import io.getstream.chat.android.models.User

class ChatViewModel : ViewModel() {

    private val client: ChatClient = ChatClient.instance()
    private val _unreadMessagesCount = MutableLiveData<Int>()
    val unreadMessagesCount: LiveData<Int> get() = _unreadMessagesCount

    private var eventSubscription: Disposable? = null

    init {
        subscribeToUnreadCountChanges()
        fetchInitialUnreadMessagesCount()
    }

    private fun subscribeToUnreadCountChanges() {
        eventSubscription = client.subscribeFor(
            NotificationMarkReadEvent::class.java,
            NotificationMessageNewEvent::class.java
        ) { event ->
            handleChatEvent(event)
        }
    }

    private fun handleChatEvent(event: ChatEvent) {
        when (event) {
            is NotificationMarkReadEvent -> updateUnreadMessagesCount()
            is NotificationMessageNewEvent -> updateUnreadMessagesCount()
            else -> {}
        }
    }

    private fun updateUnreadMessagesCount() {
        val currentUser: User? = client.clientState.user.value
        val unreadCount = currentUser?.totalUnreadCount ?: 0
        _unreadMessagesCount.postValue(unreadCount)
    }

    private fun fetchInitialUnreadMessagesCount() {
        val currentUser: User? = client.clientState.user.value
        _unreadMessagesCount.value = currentUser?.totalUnreadCount ?: 0
    }

    fun markChannelAsRead(channelId: String) {
        client.markRead(channelType = "messaging", channelId = channelId).enqueue { result ->
            if (!result.isSuccess) {
                updateUnreadMessagesCount()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventSubscription?.dispose() // Dọn dẹp subscription
    }



}