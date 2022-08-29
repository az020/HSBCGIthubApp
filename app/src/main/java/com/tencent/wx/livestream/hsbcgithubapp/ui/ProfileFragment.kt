package com.tencent.wx.livestream.hsbcgithubapp.ui

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.tencent.wx.livestream.hsbcgithubapp.R
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import com.tencent.wx.livestream.hsbcgithubapp.databinding.FragmentProfileBinding
import com.tencent.wx.livestream.hsbcgithubapp.util.CircleTransformation
import com.tencent.wx.livestream.hsbcgithubapp.viewmodel.ProfileViewModel

/**
 * Displays simple user info
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(AccountManager)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userInfo.observe(
            viewLifecycleOwner,
            Observer {
                binding.userInfo = it
                Log.i("HSBC xxxxx", "user info public repos = ${it.publicRepos}")
                setUserAvatar()
            }
        )
        profileViewModel.errMsg.observe(
            viewLifecycleOwner,
            Observer { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        )
        if (AccountManager.isLoggedIn()) {
            profileViewModel.getUserInfo()
            binding.loginGroup.visibility = View.GONE
            binding.profileGroup.visibility = View.VISIBLE
        } else {
            binding.profileGroup.visibility = View.GONE
            binding.loginGroup.visibility = View.VISIBLE
        }

        binding.logout.setOnClickListener {
            AccountManager.logout()
            findNavController().navigate(R.id.action_ProfileFragment_to_SearchFragment)
        }
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileFragment_to_LoginFragment)
        }
    }

    private fun setUserAvatar() {
        val avatarUrl = profileViewModel.userInfo.value?.avatar
        if (avatarUrl != null && avatarUrl != "" ) {
            Picasso.get().load(avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .transform(CircleTransformation())
                .into(binding.avatar)
        }
    }
}

