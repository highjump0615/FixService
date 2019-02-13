package com.brainyapps.ezfix.activities.signin

import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.app.ProgressDialog
import android.content.Intent
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.utils.FirebaseManager
import com.brainyapps.ezfix.utils.Utils

import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.AuthResult
import android.util.Log
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.models.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), LoaderCallbacks<Cursor>, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = LoginActivity::class.java.getSimpleName()

    private var googleApiClient: GoogleApiClient? = null
    private var RC_SIGN_IN = 2000

    private var loginType:Int = 0

    private var mFbCallbackManager: CallbackManager? = null

    var progressDlg: ProgressDialog? = null

    override fun onClick(view: View?) {
        when (view?.id) {
            // Facebook login
            R.id.but_facebook -> {
                this.loginType = SignupLandingActivity.LOGIN_TYPE_FACEBOOK
                this.but_fb_login.performClick()
            }
            // Google login
            R.id.but_gplus -> {
                this.loginType = SignupLandingActivity.LOGIN_TYPE_GOOGLE
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.googleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            // Log in button
            R.id.but_login -> {
                this.loginType = SignupLandingActivity.LOGIN_TYPE_EMAIL
                attemptLogin()
            }
            // Forget password button
            R.id.but_forget -> {
                val forgetIntent = Intent(this@LoginActivity, ForgetActivity::class.java)
                startActivity(forgetIntent)
            }
            // Sign up button
            R.id.but_signup -> {
                val signupIntent = Intent(this@LoginActivity, SignupLandingActivity::class.java)
                startActivity(signupIntent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Set up the login form.
        populateAutoComplete()
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        this.but_login.setOnClickListener(this)
        this.but_forget.setOnClickListener(this)
        this.but_signup.setOnClickListener(this)
        this.but_facebook.setOnClickListener(this)
        this.but_gplus.setOnClickListener(this)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        this.googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        // Initialize Facebook Login button
        mFbCallbackManager = CallbackManager.Factory.create();
        this.but_fb_login.setReadPermissions("email", "public_profile");
        this.but_fb_login.registerCallback(mFbCallbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(TAG, "facebook:onSuccess: $result")
                handleFacebookAccessToken(result?.accessToken!!);
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, "facebook:onError", error)
            }

        })
    }

    override fun onResume() {
        super.onResume()

        // logout facebook completely
        LoginManager.getInstance().logOut()
    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        }
        else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
            return
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        if (progressDlg != null) {
            // already processing, exit
            return
        }

        progressDlg = Utils.createProgressDialog(this, "Loggin in...", "Submitting user credentials")
        this.but_login.isEnabled = false

        FirebaseManager.mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Utils.createErrorAlertDialog(this, "Authentication failed.", task.exception?.localizedMessage!!).show()
                        closeProgressDialog()
                        progressDlg = null
                        this.but_login.isEnabled = true

                        return@OnCompleteListener
                    }

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")

                    fetchUserInfo()
                })
    }

    private fun fetchUserInfo(userInfo: FirebaseUser? = null) {
        val userId = FirebaseManager.mAuth.currentUser!!.uid

        User.readFromDatabase(userId, object: User.FetchDatabaseListener {
            override fun onFetchedReviews() {
            }

            override fun onFetchedUser(user: User?, success: Boolean) {
                User.currentUser = user

                if (!success) {
                    signOutClear()
                    this@LoginActivity.but_login.isEnabled = true
                }
                else {
                    if (User.currentUser == null) {
                        // get user info, from facebook account info
                        if (userInfo != null) {
                            val newUser = User(userId)
                            if (!TextUtils.isEmpty(userInfo.displayName)) {
                                val names = userInfo.displayName!!.split(" ")
                                newUser.firstName = names[0]
                                newUser.lastName = names[1]
                            }

                            newUser.email = userInfo.email!!
                            newUser.photoUrl = userInfo.photoUrl.toString()

                            User.currentUser = newUser
                        }

                        // social login, go to user type page
                        val intent = Intent(this@LoginActivity, SignupLandingActivity::class.java)
                        intent.putExtra(SignupLandingActivity.KEY_LOGIN_TYPE, this@LoginActivity.loginType)
                        startActivity(intent)
                    }
                    else {
                        goToMain()
                    }
                }

                closeProgressDialog()
            }
        })
    }

    private fun closeProgressDialog() {
        Utils.closeProgressDialog()
        progressDlg = null
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@LoginActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        email.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account);
            }
            catch (e: ApiException ) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK
        mFbCallbackManager!!.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * google log in
     */
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id);

        progressDlg = Utils.createProgressDialog(this, "Loggin in...", "Submitting user credentials")

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null);
        FirebaseManager.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Utils.createErrorAlertDialog(this, "Authentication failed.", task.exception?.localizedMessage!!).show()
                        closeProgressDialog()

                        return@OnCompleteListener
                    }

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    fetchUserInfo(task.result.user)
                })
    }

    /**
     * facebook log in
     */
    fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        progressDlg = Utils.createProgressDialog(this, "Loggin in...", "Submitting user credentials")

        val credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseManager.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (!task.isSuccessful) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Utils.createErrorAlertDialog(this, "Authentication failed.", task.exception?.localizedMessage!!).show()
                        closeProgressDialog()

                        return@OnCompleteListener
                    }

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    fetchUserInfo(task.result.user)
                })
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0
    }
}
