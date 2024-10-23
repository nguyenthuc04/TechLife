package com.snapco.snaplife.extensions

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber

object TwilioHelper {
    private const val ACCOUNT_SID = "AC97ab12b9dde4940fc4c4b8e15f4c261a"
    private const val AUTH_TOKEN = "d9ec131f4e085296b6d85530f59c7e73"
    private const val TWILIO_PHONE_NUMBER = "+12085497563"

    init {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
    }

    fun sendSms(
        to: String,
        body: String,
    ) {
        Message
            .creator(
                PhoneNumber(to),
                PhoneNumber(TWILIO_PHONE_NUMBER),
                body,
            ).create()
    }
}
