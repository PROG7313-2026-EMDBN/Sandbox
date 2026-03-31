package com.prog7313.sandbox.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prog7313.sandbox.model.UserProfile
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProfileRepository(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    private fun userRef(uid: String) = db.reference.child("users").child(uid).child("profile")

    suspend fun ensureProfileExists(uid: String, email: String) {
        val snapshot = userRef(uid).get().await()

        if (!snapshot.exists()) {
            val starter = UserProfile(
                uid = uid,
                email = email,
                displayName = "",
                badgeTitle = "Bug Hunter",
                avatarUrl = ""
            )
            saveProfile(starter)
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        suspendCancellableCoroutine<Unit> { cont ->
            userRef(profile.uid).setValue(profile)
                .addOnSuccessListener {
                    Log.d("ProfileRepository", "Profile saved successfully for uid=${profile.uid}")
                    if (cont.isActive) cont.resume(Unit)
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileRepository", "Failed to save profile", e)
                    if (cont.isActive) cont.resumeWithException(e)
                }
        }
    }

    fun observeProfile(
        uid: String,
        onChange: (UserProfile?) -> Unit
    ): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue(UserProfile::class.java)
                Log.d("ProfileRepository", "Profile loaded: $profile")
                onChange(profile)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileRepository", "observeProfile cancelled: ${error.message}")
                onChange(null)
            }
        }

        userRef(uid).addValueEventListener(listener)
        return listener
    }

    fun removeListener(uid: String, listener: ValueEventListener) {
        userRef(uid).removeEventListener(listener)
    }
}