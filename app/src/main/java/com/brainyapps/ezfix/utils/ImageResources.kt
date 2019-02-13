package com.brainyapps.ezfix.utils

import com.stripe.android.model.Card
import java.util.HashMap

/**
 * Created by Administrator on 3/14/18.
 */

object ImageResources {
    val TEMPLATE_RESOURCE_MAP: MutableMap<String, Int> = HashMap()

    init {
        TEMPLATE_RESOURCE_MAP[Card.AMERICAN_EXPRESS] = com.stripe.android.R.drawable.ic_amex
        TEMPLATE_RESOURCE_MAP[Card.DINERS_CLUB] = com.stripe.android.R.drawable.ic_diners
        TEMPLATE_RESOURCE_MAP[Card.DISCOVER] = com.stripe.android.R.drawable.ic_discover
        TEMPLATE_RESOURCE_MAP[Card.JCB] = com.stripe.android.R.drawable.ic_jcb
        TEMPLATE_RESOURCE_MAP[Card.MASTERCARD] = com.stripe.android.R.drawable.ic_mastercard
        TEMPLATE_RESOURCE_MAP[Card.VISA] = com.stripe.android.R.drawable.ic_visa
        TEMPLATE_RESOURCE_MAP[Card.UNKNOWN] = com.stripe.android.R.drawable.ic_unknown
    }
}
