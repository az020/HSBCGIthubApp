package com.tencent.wx.livestream.hsbcgithubapp.ui

import android.accounts.Account
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tencent.wx.livestream.hsbcgithubapp.R
import com.tencent.wx.livestream.hsbcgithubapp.account.AccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tokenInput = view.findViewById<EditText>(R.id.token)
        val loginButton = view.findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            val token = tokenInput.text.toString()
            if (token == "") {
                Toast.makeText(activity, "Token cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = AccountManager.login(token)
                    withContext(Dispatchers.Main) {
                        response.success?.let {
                           findNavController().navigate( R.id.action_LoginFragment_to_ProfileFragment)
                        }
                        response.fail?.let {
                            Toast.makeText(activity, "Login Failed: $it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val tokenInstructLink = view.findViewById<TextView>(R.id.token_direction)
        tokenInstructLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token"))
            startActivity(browserIntent)
        }
    }
}
