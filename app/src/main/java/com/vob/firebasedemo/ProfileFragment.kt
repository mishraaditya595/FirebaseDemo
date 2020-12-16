package com.vob.firebasedemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.vob.firebasedemo.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        val user  = auth.currentUser
        firestore = FirebaseFirestore.getInstance()

        binding.name.text = user?.displayName
        binding.email.text = user?.email
        val photoURL = user?.photoUrl

        Glide.with(requireContext())
            .load(photoURL)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(binding.profilePic)

        binding.updateDatabase.setOnClickListener {
            user?.let {
                val userMap = hashMapOf<String, String>("name" to it.displayName!!,
                "email" to it.email!!, "profile pic" to it.photoUrl.toString(), "uid" to it.uid)

                firestore.collection("users").document(it.uid).set(userMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context,"Task Successful", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }
}