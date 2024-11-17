package com.snapco.techlife.ui.view.activity.messenger

import android.app.Application
import com.cloudinary.android.MediaManager

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config =
            mapOf(
                "cloud_name" to "dy9scmo1m",
                "api_key" to "454234546361358",
                "api_secret" to "wQTZeUdsTS89sqSHPqqo9PkWqqY",
            )
        MediaManager.init(this, config)

        val statePluginFactory =
            StreamStatePluginFactory(
                appContext = applicationContext,
                config =
                    StatePluginConfig(
                        backgroundSyncEnabled = true,
                        userPresence = true,
                    ),
            )

        val offlinePluginFactory = StreamOfflinePluginFactory(applicationContext)

        ChatClient
            .Builder("zjttkfv87qhy", applicationContext)
            .withPlugins(statePluginFactory, offlinePluginFactory)
            .uploadAttachmentsNetworkType(
                UploadAttachmentsNetworkType.NOT_ROAMING,
            ).build()
    }
}
