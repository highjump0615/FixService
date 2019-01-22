package com.brainyapps.ezfix.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.models.StripeSource
import com.brainyapps.ezfix.utils.Utils
import com.stripe.android.SourceCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Source
import com.stripe.android.model.SourceCardData
import com.stripe.android.model.SourceParams
import kotlinx.android.synthetic.main.activity_stripe_card_input.*

class StripeCardInputActivity : BaseActivity(), View.OnClickListener {

    companion object {
        val KEY_STRIPE_SOURCE = "source"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stripe_card_input)

        setNavbar("Payment Information", true)

        this.but_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // save
            R.id.but_save -> {
                onButSave()
            }
        }
    }

    private fun onButSave() {
        val card = this.card_multiline_widget.card

        if (card == null) {
            Toast.makeText(this, "Card data is not invalid", Toast.LENGTH_SHORT).show()
            return
        }

        enableButton(this.but_save, false)
        Utils.createProgressDialog(this, "Validating..", "Creating source for card")

        val stripe = Stripe(this, getString(R.string.stripe_public_key))
        val cardSourceParams = SourceParams.createCardParams(card)

        stripe.createSource(cardSourceParams, object: SourceCallback {
            override fun onSuccess(source: Source?) {
                val selectedSourceCardData = source!!.sourceTypeModel as SourceCardData

                val newSource = StripeSource()
                newSource.sourceId = source.id;
                newSource.secretId = source.clientSecret;
                newSource.last4 = selectedSourceCardData.last4!!
                newSource.brand = selectedSourceCardData.brand!!

                val intent = Intent()
                intent.putExtra(KEY_STRIPE_SOURCE, newSource)
                setResult(1, intent)

                finish()
            }

            override fun onError(error: java.lang.Exception?) {
                Toast.makeText(this@StripeCardInputActivity, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                enableButton(this@StripeCardInputActivity.but_save, true)
                Utils.closeProgressDialog()
            }
        })
    }
}
