package com.brainyapps.ezfix.activities.admin

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseEditProfileActivity
import com.brainyapps.ezfix.utils.Utils
import kotlinx.android.synthetic.main.activity_admin_edit_profile.*

class AdminEditProfileActivity : BaseEditProfileActivity(), View.OnClickListener {

    private val TAG = AdminEditProfileActivity::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_profile)

        setNavbar("Edit Owner Profile", true)

        initUserInfo()

        this.but_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // save
            R.id.but_save -> {

                if (!checkPasswordValidate()) {
                    return
                }

                if (TextUtils.isEmpty(this.editPassword!!.text.toString())) {
                    finish()
                    return
                }

                Utils.createProgressDialog(this, "Updating Profile", "Saving your user information")

                savePassword(object: PasswordSaveListener {
                    override fun onSavedPassword(success: Boolean) {
                        Utils.closeProgressDialog()

                        if (success) {
                            finish()
                        }
                    }
                })
            }
        }
    }
}
