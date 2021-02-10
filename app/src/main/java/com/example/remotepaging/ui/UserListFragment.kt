package com.example.remotepaging.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remotepaging.R
import com.example.remotepaging.adapter.UserAdapter
import com.example.remotepaging.databinding.ListUsersBinding
import com.example.remotepaging.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment : Fragment(){

    private val userListViewmodel: UserListViewModel by viewModels()

    @Inject
    lateinit var userAdapter : UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val binding = ListUsersBinding.inflate(layoutInflater)
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvUsers.layoutManager = linearLayoutManager
        binding.rvUsers.adapter = userAdapter
       with(userListViewmodel){
           launchOnLifecycleScope {
               usersFlow.collectLatest {
                   Log.d("UserList",""+it)
                   userAdapter.submitData(it)
               }
           }

       }

        binding.addFab.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.secondFragment)
        }
        return binding.root;
    }


    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }




}