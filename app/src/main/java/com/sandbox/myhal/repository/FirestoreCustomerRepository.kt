package com.sandbox.myhal.repository

import android.app.Activity

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.content.pm.PackageManager
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.sandbox.myhal.activities.*
import com.sandbox.myhal.models.Board
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
                val mFireStore = FirebaseFirestore.getInstance()

                mFireStore.collection(Constants.USERS)
                    .document(getCurrentUserId())
                    .set(user, SetOptions.merge())
                    .addOnSuccessListener {
                        FirebaseAuth.getInstance().signOut()
                        activity.userRegisteredSuccess()
                    }.addOnFailureListener{
                            e ->
                        Log.e(activity.javaClass.simpleName, "Error in FirestoreClass")
                        activity.userRegisteredUnSuccessful("Error in FirestoreClass")
                    }
                //saveUser(user)
            } else {
                activity.userRegisteredUnSuccessful(task.exception!!.message.toString())
            }

        }

    }

    override fun getAssignedMembersListDetails(
        activity: Activity,
        assignedTo: ArrayList<String>
    ) {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val usersList : ArrayList<User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }
                if(activity is MembersActivity){
                    activity.setupMembersList(usersList)
                } else if (activity is TaskListActivity){
                    activity.boardMembersDetailsList(usersList)
                }

            }.addOnFailureListener {
                    e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a board.")
            }
    }

    override fun loadUserData(activity: BaseActivity) {
        val mFireStore = FirebaseFirestore.getInstance()
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                activity.receiveUserData(loggedInUser)

            }.addOnFailureListener {
                    e ->
                Log.e(activity.javaClass.simpleName, "Error writing document")
            }
    }

    override fun updateUserProfileData(activity: BaseActivity, userHashMap: HashMap<String, Any>) {


        mFireStore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserId()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                when(activity){
                    is BoardActivity -> {
                       activity.tokenUpdateSuccess()
                    }
                    is MyProfileActivity -> {
                        activity.profileUpdateSuccess()
                    }
                }
                // Notify the success result.

            }
            .addOnFailureListener { e ->
                when(activity){
                    is BoardActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MyProfileActivity -> {
                        activity.profileUpdateUnSuccessful(e)
                    }
                }

            }
    }

    override fun uploadUserImage(activity: MyProfileActivity, fileUri: Uri?) {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(activity,
                fileUri
            )
        )

        //adding the file to reference
        sRef.putFile(fileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.i("Downloadable Image URL", uri.toString())

                        // assign the image url to the variable.
                        //mProfileImageURL = uri.toString()

                        // Call a function to update user details in the database.
                        activity.updateUserProfileData(uri.toString())
                    }
            }
            .addOnFailureListener { e ->
                activity.profileUpdateUnSuccessful(e)
            }
    }

    override fun isLoggedIn(): Boolean {
        return getCurrentUserId() != ""
    }

    override fun getMemberDetails(activity: MembersActivity, email: String) {
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener {
                document ->
                if(document.documents.size > 0){
                    val user = document.documents[0].toObject(User::class.java)
                    if (user != null) {
                        activity.memberDetails(user)
                    }
                } else {
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("No such member found")
                }
            }.addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details"
                )
            }
    }


    override fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    override fun signInUser(activity: SignInActivity, userInfo: User, password: String){
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(userInfo.email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sign In", "signInWithEmail:success")

                    mFireStore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .get()
                        .addOnSuccessListener { document ->
                            val loggedInUser = document.toObject(User::class.java)!!
                            activity.receiveUserData(loggedInUser)
                        }.addOnFailureListener {
                                e ->
                            Log.e(activity.javaClass.simpleName, "Error writing document")
                            activity.signInUnSuccessful("Internal Error please check datastore.")

                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign In", "signInWithEmail:failure", task.exception)
                    activity.signInUnSuccessful("Authentication failed.")
                }
            }

    }

    override fun getCurrentUserId(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if(currentUser != null){
            currentUserId = currentUser.uid
        }

        return  currentUserId
    }

    private fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
        //return ".jpg"
    }
}