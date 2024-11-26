package com.snapco.techlife.ui.view.activity.messenger

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ItemChannelBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.extensions.internal.lastMessage
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.ui.ChatUI
import io.getstream.chat.android.ui.feature.channels.list.ChannelListView
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListItem
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListPayloadDiff
import io.getstream.chat.android.ui.feature.channels.list.adapter.viewholder.BaseChannelListItemViewHolder
import io.getstream.chat.android.ui.feature.channels.list.adapter.viewholder.ChannelListItemViewHolderFactory

class ChannelViewHolder : ChannelListItemViewHolderFactory() {
    override fun createChannelViewHolder(parentView: ViewGroup): BaseChannelListItemViewHolder {
        return CustomChannelViewHolder(parentView, listenerContainer.channelClickListener)
    }
}

class CustomChannelViewHolder(
    parent: ViewGroup,
    private val channelClickListener: ChannelListView.ChannelClickListener,
    private val binding: ItemChannelBinding = ItemChannelBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ),
) : BaseChannelListItemViewHolder(binding.root) {

    private lateinit var channel: Channel

    init {
        binding.root.setOnClickListener { channelClickListener.onClick(channel) }
    }

    @OptIn(InternalStreamChatApi::class)
    override fun bind(channelItem: ChannelListItem.ChannelItem, diff: ChannelListPayloadDiff) {
        // Sử dụng channelItem.channel để gán giá trị cho channel
        this.channel = channelItem.channel

        binding.channelAvatarView.setChannel(channel)
        binding.txtName.text = ChatUI.channelNameFormatter.formatChannelName(
            channel = channel,
            currentUser = ChatClient.instance().getCurrentUser()
        )
        // Kiểm tra và lấy tin nhắn mới nhất
        val lastMessage = channel.lastMessage
        if (lastMessage != null) {
            // Lấy nội dung tin nhắn mới nhất
            val lastMess = ChatUI.messagePreviewFormatter.formatMessagePreview(channel, lastMessage, ChatClient.instance().getCurrentUser())
            binding.txtLastMessenger.text = lastMess
            if (channel.unreadCount > 0 ) {
                // Nếu có tin nhắn chưa đọc, set màu đen
                binding.txtLastMessenger.setTextColor(itemView.context.getColor(R.color.black))
                binding.txtLastTimeMessenger.setTextColor(itemView.context.getColor(R.color.black))
            } else {
                // Nếu không có tin nhắn chưa đọc, set màu xám
                binding.txtLastMessenger.setTextColor(itemView.context.getColor(R.color.stream_gray_dark))
                binding.txtLastTimeMessenger.setTextColor(itemView.context.getColor(R.color.stream_gray_dark))
            }

            // Lấy thời gian gửi tin nhắn
            val timestamp = lastMessage.createdAt
            binding.txtLastTimeMessenger.text = DateUtils.getRelativeTimeSpanString(timestamp!!.time) // Sử dụng DateUtils để hiển thị thời gian
        } else {
            binding.txtLastMessenger.text = itemView.context.getString(R.string.no_messenger)
            binding.txtLastTimeMessenger.text = null // Hoặc một chuỗi mặc định nếu không có tin nhắn
        }
    }

}