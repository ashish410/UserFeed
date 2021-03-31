package com.project.feed.data

import android.os.Parcel
import android.os.Parcelable

data class UserDetail(
    val id: Int,
    val name: String?,
    val username: String?,
    val email: String?,
    val address: Address?,
    val phone: String?,
    val website: String?,
    val company: Company?,
    var allPostByUser: MutableList<Post>,
    val noOfPost: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Company::class.java.classLoader),
        mutableListOf<Post>().apply {
            parcel.readArrayList(Post::class.java.classLoader)
        },
        parcel.readInt()
    )

    constructor(id: Int, allPostByUser: MutableList<Post>) : this(
        id, "", "", "", null, "", "",
        null, allPostByUser, allPostByUser.size
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeParcelable(address, flags)
        parcel.writeString(phone)
        parcel.writeString(website)
        parcel.writeParcelable(company, flags)
        parcel.writeInt(noOfPost)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetail> {
        override fun createFromParcel(parcel: Parcel): UserDetail {
            return UserDetail(parcel)
        }

        override fun newArray(size: Int): Array<UserDetail?> {
            return arrayOfNulls(size)
        }
    }
}