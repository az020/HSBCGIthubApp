package com.tencent.wx.livestream.hsbcgithubapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tencent.wx.livestream.hsbcgithubapp.R
import com.tencent.wx.livestream.hsbcgithubapp.databinding.FragmentSearchBinding
import com.tencent.wx.livestream.hsbcgithubapp.networking.GitHubServiceManager
import com.tencent.wx.livestream.hsbcgithubapp.viewmodel.RepoViewModel


/**
 * A screen for user to search repositories and see the top 30 results
 */
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val repoViewModel: RepoViewModel by viewModels {
        RepoViewModel.Factory(GitHubServiceManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RepoListAdapter()
        binding.repoList.adapter = adapter
        repoViewModel.repoList.observe(
            viewLifecycleOwner,
            Observer{
                Log.i("SearchFragment", "repoList observe update: ${it.size}")
                adapter.setList(it)
            }
        )
        repoViewModel.total.observe(
            viewLifecycleOwner,
            Observer {
                binding.total = it
                binding.searchResult.visibility = View.VISIBLE
            }
        )
        repoViewModel.errMsg.observe(
            viewLifecycleOwner,
            Observer { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        )

        binding.searchBar.editText?.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH-> {
                    val text = v.text.toString()
                    Log.i("EDIT TEXT", text)
                    repoViewModel.search(text)
                    closeKeyboard(v)
                    true
                }
                else -> false
            }
        }
    }

    private fun closeKeyboard(view: View) {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
