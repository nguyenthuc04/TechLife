package com.snapco.techlife.ui.view.activity.messenger

import android.app.Application
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val statePluginFactory = StreamStatePluginFactory(
            appContext = applicationContext,
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true,
            ),
        )

        val offlinePluginFactory = StreamOfflinePluginFactory(applicationContext)

        ChatClient.Builder("zjttkfv87qhy", applicationContext)
            .withPlugins(statePluginFactory, offlinePluginFactory)
            .uploadAttachmentsNetworkType(
                UploadAttachmentsNetworkType.NOT_ROAMING
            )
            .build()

    }
}