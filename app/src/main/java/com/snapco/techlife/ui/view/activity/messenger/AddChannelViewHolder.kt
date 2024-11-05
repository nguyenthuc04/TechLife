package com.snapco.techlife.ui.view.activity.messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import com.snapco.techlife.databinding.ItemAddChannelBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.ui.ChatUI
import io.getstream.chat.android.ui.feature.channels.list.ChannelListView
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListItem
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListPayloadDiff
import io.getstream.chat.android.ui.feature.channels.list.adapter.viewholder.BaseChannelListItemViewHolder
import io.getstream.chat.android.ui.feature.channels.list.adapter.viewholder.ChannelListItemViewHolderFactory

class AddChannelViewHolder : ChannelListItemViewHolderFactory() {
    override fun createChannelViewHolder(parentView: ViewGroup): BaseChannelListItemViewHolder {
        return CustomAddChannelViewHolder(parentView, listenerContainer.channelClickListener)
    }
}

class CustomAddChannelViewHolder(
    parent: ViewGroup,
    private val channelClickListener: ChannelListView.ChannelClickListener,
    private val binding: ItemAddChannelBinding = ItemAddChannelBinding.inflate(
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
    fun bind(channel: Channel, diff: ChannelListPayloadDiff) {
        this.channel = channel

        // Gán giá trị cho các trường trong giao diện người dùng
        binding.imgAvatar.setChannel(channel)
        binding.txtName.text = ChatUI.channelNameFormatter.formatChannelName(
            channel = channel,
            currentUser = ChatClient.instance().getCurrentUser()
        )

    }

    override fun bind(channelItem: ChannelListItem.ChannelItem, diff: ChannelListPayloadDiff) {
        bind(channelItem.channel, diff)
    }


}