package com.sandbox.myhal.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sandbox.myhal.activities.SignInActivity
import com.sandbox.myhal.activities.SignUpActivity
import com.sandbox.myhal.models.User
import com.sandbox.myhal.utils.Constants

class FirestoreCustomerRepository : CustomerRepository {

    private val mFireStore = FirebaseFirestore.getInstance()

    override fun registerUser(activity: SignUpActivity, userInfo: User, password: String){
        Log.i("FireStore", "Registering a user")
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(userInfo.email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val firebaseUser: FirebaseUser = task.result!!.user!!
                val registeredEmail = firebaseUser.email!!

                val user = User(firebaseUser.uid, userInfo.name, registeredEmail)
                mFireStore.collection(Constants.USERS)
                    .document(getCurrentUserId())
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener {
                        activity.userRegisteredSuccess()
                    }.addOnFailureListener{
                            e ->
                        Log.e(activity.javaClass.simpleName, "Error in FirestoreClass")
                    }
                //saveUser(user)
            } else {
                Toast.makeText(
                    activity,
                    task.exception!!.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    override fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                activity.signInSuccess(loggedInUser)
            }.addOnFailureListener {
                    e ->
                Log.e(activity.javaClass.simpleName, "Error writing document")
            }
    }

    fun saveUser(userInfo: User) {

    }

    fun getCurrentUserId(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if(currentUser != null){
            currentUserId = currentUser.uid
        }

        return  currentUserId
    }
}