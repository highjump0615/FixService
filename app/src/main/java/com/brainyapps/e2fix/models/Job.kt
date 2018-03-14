package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import com.google.firebase.database.*

/**
 * Created by Administrator on 2/21/18.
 */
class Job() : BaseModel(), Parcelable {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "jobs"
        const val FIELD_TITLE = "title"
        const val FIELD_DESC = "description"
        const val FIELD_USERID = "userId"
        const val FIELD_PHOTO_URL = "photoUrl"
        const val FIELD_CATEGORY = "category"
        const val FIELD_LOCATION = "location"
        const val FIELD_BIDID_TAKEN = "bidTakenId"

        fun readFromDatabase(withId: String, fetchListener: FetchDatabaseListener) {

            val database = FirebaseDatabase.getInstance().reference
            val query = database.child(Job.TABLE_NAME + "/" + withId)

            // Read from the database
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val j = dataSnapshot.getValue(Job::class.java)
                    j?.id = withId

                    fetchListener.onFetchedJob(j, true)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(User.TAG, "failed to read from database.", error.toException())
                    fetchListener.onFetchedJob(null, false)
                }
            })
        }


        @JvmField
        val CREATOR: Parcelable.Creator<Job> = object : Parcelable.Creator<Job> {
            override fun createFromParcel(parcel: Parcel): Job {
                return Job(parcel)
            }

            override fun newArray(size: Int): Array<Job?> {
                return arrayOfNulls(size)
            }
        }
    }

    // user posted
    @get:Exclude
    var userPosted: User? = null

    @get:Exclude
    var bidArray: ArrayList<Bid> = ArrayList()

    var userId = ""

    var title = ""
    var description = ""

    var photoUrl = ""
    var category = 0

    var location = ""

    var bidTakenId = ""

    constructor(parcel: Parcel) : this() {
        userPosted = parcel.readParcelable(User::class.java.classLoader)
        userId = parcel.readString()
        title = parcel.readString()
        description = parcel.readString()
        photoUrl = parcel.readString()
        category = parcel.readInt()
        location = parcel.readString()
        bidTakenId = parcel.readString()
        parcel.readList(bidArray, Bid::class.java.classLoader)

        readFromParcelBase(parcel)
    }

    fun saveToDatabase(withId: String, key: String? = null, value: Any? = null) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        val node = database.child(Job.TABLE_NAME).child(withId)

        if (key != null && value != null) {
            node.child(key).setValue(value)
        }
        else {
            node.child(FIELD_TITLE).setValue(this.title)
            node.child(FIELD_DESC).setValue(this.description)
            node.child(FIELD_USERID).setValue(this.userId)
            node.child(FIELD_PHOTO_URL).setValue(this.photoUrl)
            node.child(FIELD_CATEGORY).setValue(this.category)
            node.child(FIELD_LOCATION).setValue(this.location)
            node.child(FIELD_BIDID_TAKEN).setValue(this.bidTakenId)
        }

        saveToDatabaseBase(node)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(userPosted, flags)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(photoUrl)
        parcel.writeInt(category)
        parcel.writeString(location)
        parcel.writeString(bidTakenId)
        parcel.writeList(bidArray)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * get bid taken
     */
    fun jobBidTaken() : Bid? {
        var bid: Bid? = null
        for (bidItem in this.bidArray) {
            if (TextUtils.equals(bidItem.id, this.bidTakenId)) {
                bid = bidItem
            }
        }

        return bid
    }

    fun fetchBidList(fetchListener: FetchBidInfoListener) {
        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(Bid.TABLE_NAME).orderByChild(Bid.FIELD_JOBID).equalTo(this.id)

        // Read from the database
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                this@Job.bidArray.clear()

                for (bidItem in dataSnapshot.children) {
                    val bid = bidItem.getValue(Bid::class.java)
                    bid!!.id = bidItem.key
                    bid.job = this@Job

                    if (bid.isTaken) {
                        this@Job.bidArray.add(0, bid)
                    }
                    else {
                        this@Job.bidArray.add(bid)
                    }
                }

                // update the list
                fetchListener.onFetchedBid(true)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(User.TAG, "failed to read from database.", error.toException())

                fetchListener.onFetchedBid(false)
            }
        })
    }

    fun isBidAvailable() : Boolean {
        return TextUtils.isEmpty(bidTakenId)
    }


    /**
     * interface for reading from database
     */
    interface FetchDatabaseListener {
        fun onFetchedJob(job: Job?, success: Boolean)
    }
    interface FetchBidInfoListener {
        fun onFetchedBid(success: Boolean)
    }
}