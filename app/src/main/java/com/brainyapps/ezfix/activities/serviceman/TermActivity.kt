package com.brainyapps.ezfix.activities.serviceman

import android.os.Bundle
import android.text.Html
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_term.*

class TermActivity : BaseActivity() {

    companion object {
        val KEY_TERM_TYPE = "term_type"

        val TERM_SERVICE = 0
        val TERM_POLICY = 1
    }

    private var type = TERM_SERVICE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term)

        setNavbar(null, true)

        val bundle = intent.extras
        // get type from intent
        this.type = bundle?.getInt(KEY_TERM_TYPE)!!

        if (this.type == TERM_SERVICE) {
            this.text_title.text = getString(R.string.terms_title)
            this.text_content.text = Html.fromHtml(getString(R.string.terms_content))
        }
        else if (this.type == TERM_POLICY) {
            this.text_title.text = getString(R.string.privacy_title)
            this.text_content.text = Html.fromHtml(getString(R.string.privacy_content))
        }
    }
}
