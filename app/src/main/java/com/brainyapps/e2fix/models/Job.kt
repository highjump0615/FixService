package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
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
        const val FIELD_USERID = "userId"

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

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        database.child(Job.TABLE_NAME).child(withId).setValue(this)
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

                    this@Job.bidArray.add(bid)
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