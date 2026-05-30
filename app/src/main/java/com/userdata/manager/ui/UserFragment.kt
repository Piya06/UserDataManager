package com.userdata.manager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.userdata.manager.R
import com.userdata.manager.data.local.UserEntity
import com.userdata.manager.databinding.FragmentUserBinding
import com.userdata.manager.viewmodel.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: UserViewmodel by viewModels()

    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        observeUsers()
        setupToolbarMenu()

        binding.floatingAddbtn.setOnClickListener {
            AddEditUserBottomSheet
                .newInstance()
                .show(
                    parentFragmentManager,
                    "AddUser"
                )
        }


        handlingBackpress()

    }

    private fun handlingBackpress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (adapter.getSelectedIds().isNotEmpty()) {
                        adapter.clearSelection()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun setupRecyclerView() {

        adapter = UserAdapter(
            onClick = { user -> openEditBottomSheet(user) },

            onDelete = { user -> viewModel.delete(user) },

            onSelectionChanged = { count ->
                if (count > 0) {
                    binding.toolbar.title = "$count Selected"
                } else {
                    binding.toolbar.title = "UserData Manager"
                }
                requireActivity().invalidateOptionsMenu()
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UserFragment.adapter
        }
    }

    private fun observeUsers() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }
    }

    private fun openEditBottomSheet(user: UserEntity) {
        AddEditUserBottomSheet
            .newInstance(user)
            .show(
                parentFragmentManager,
                "EditUser"
            )
    }

    private fun setupToolbarMenu() {

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater
                ) {
                    menuInflater.inflate(
                        R.menu.search_menu,
                        menu
                    )

                    menu.findItem(R.id.action_delete)?.isVisible =
                        adapter.getSelectedIds().isNotEmpty()

                    val searchItem = menu.findItem(R.id.action_search)

                    val searchView = searchItem.actionView as SearchView

                    searchView.queryHint = getString(R.string.search_by_name_or_phone)

                    searchView.maxWidth = Int.MAX_VALUE

                    searchView.setOnQueryTextListener(

                        object : SearchView.OnQueryTextListener {

                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                viewModel.search(
                                    newText.orEmpty()
                                )
                                return true
                            }
                        }
                    )
                }

                override fun onMenuItemSelected(
                    menuItem: MenuItem
                ): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_delete -> {
                            val ids = adapter.getSelectedIds()
                            if (ids.isNotEmpty()) {
                                viewModel.deleteMultiple(ids)
                                adapter.clearSelection()
                            }
                            true
                        }
                        else -> false
                    }
                }
            },

            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}