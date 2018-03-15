package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import java.util.*


/**
 * Created by Administrator on 2/19/18.
 */

class User() : BaseModel(), Parcelable {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2
        val TAG = BaseModel::class.java.getSimpleName()

        var currentUser: User? = null

        fun readFromDatabase(withId: String, fetchListener: FetchDatabaseListener) {

            val database = FirebaseDatabase.getInstance().reference
            val query = database.child(User.TABLE_NAME + "/" + withId)

            // Read from the database
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val u = dataSnapshot.getValue(User::class.java)
                    u?.id = withId

                    fetchListener.onFetchedUser(u, true)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "failed to read from database.", error.toException())
                    fetchListener.onFetchedUser(null, false)
                }
            })
        }

        //
        // table info
        //
        val TABLE_NAME = "users"
        val FIELD_EMAIL = "email"
        val FIELD_TYPE = "type"
        val FIELD_BANNED = "banned"


        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }

    var type: Int = USER_TYPE_ADMIN
    var banned: Boolean = false

    @get:Exclude
    var password = ""

    @get:Exclude
    var photoByteArray: ByteArray? = null

    var email = ""

    var facebookId = ""

    var firstName = ""
    var lastName = ""
    var photoUrl = ""

    var contact = ""
    var location = ""

    var skill = ""
    var rating = 0.0

    @get:Exclude
    var posts = ArrayList<Job>()

    @get:Exclude
    var reviews = ArrayList<Review>()

    // stripe
    var stripeAccountId = ""
    var stripeCustomerId = ""
    var stripeSource: StripeSource? = null

    override fun tableName() = TABLE_NAME

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        banned = parcel.readByte().toInt() != 0
        password = parcel.readString()
        photoByteArray = parcel.createByteArray()
        email = parcel.readString()
        facebookId = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        photoUrl = parcel.readString()
        contact = parcel.readString()
        location = parcel.readString()
        skill = parcel.readString()
        rating = parcel.readDouble()
        stripeAccountId = parcel.readString()
        stripeCustomerId = parcel.readString()
        stripeSource = parcel.readParcelable(StripeSource::class.java.classLoader)

        readFromParcelBase(parcel)
    }

    constructor(id: String) : this() {
        this.id = id
    }

    /**
     * returns type name
     */
    fun userTypeString(): String {
        return if (type == User.USER_TYPE_CUSTOMER) {
            "Customer"
        }
        else {
            "Serviceman"
        }
    }

    /**
     * returns full name
     */
    fun userFullName(): String {
        return "${this.firstName} ${this.lastName}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeByte((if (banned) 1 else 0).toByte())
        parcel.writeString(password)
        parcel.writeByteArray(photoByteArray)
        parcel.writeString(email)
        parcel.writeString(facebookId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photoUrl)
        parcel.writeString(contact)
        parcel.writeString(location)
        parcel.writeString(skill)
        parcel.writeDouble(rating)
        parcel.writeString(stripeAccountId)
        parcel.writeString(stripeCustomerId)
        parcel.writeParcelable(stripeSource, flags)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * fetch reviews of the user
     */
    fun fetchReviews(fetchListener: FetchDatabaseListener) {
        mFetchReviewListener = fetchListener

        val database = FirebaseDatabase.getInstance().reference.child(Review.TABLE_NAME)
        val query = database.orderByChild(Review.FIELD_USERID_RATED).equalTo(this.id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                this@User.reviews.clear()

                for (reviewItem in dataSnapshot.children) {
                    val review = reviewItem.getValue(Review::class.java)
                    review!!.id = reviewItem.key

                    this@User.reviews.add(review)

                    // fetch user
                    User.readFromDatabase(review.userId, mFetchListener)
                }

                // sort
                Collections.sort(this@User.reviews, Collections.reverseOrder())
            }

            override fun onCancelled(error: DatabaseError) {
                mFetchReviewListener?.onFetchedReviews()
            }
        })
    }

    private var mFetchReviewListener: FetchDatabaseListener? = null
    private val mFetchListener = object : FetchDatabaseListener {
        override fun onFetchedUser(user: User?, success: Boolean) {
            var bFetchedAll = true

            for (reviewItem in this@User.reviews) {
                if (TextUtils.equals(reviewItem.userId, user!!.id)) {
                    reviewItem.user = user
                }

                if (reviewItem.user == null) {
                    bFetchedAll = false
                }
            }

            if (bFetchedAll) {
                mFetchReviewListener?.onFetchedReviews()
            }
        }

        override fun onFetchedReviews() {
        }
    }

    /**
     * interface for reading from database
     */
    interface FetchDatabaseListener {
        fun onFetchedUser(user: User?, success: Boolean)
        fun onFetchedReviews()
    }

}