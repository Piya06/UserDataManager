package com.userdata.manager.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.userdata.manager.R
import com.userdata.manager.data.local.UserEntity
import com.userdata.manager.databinding.BottomSheetUserBinding
import com.userdata.manager.utils.UserDataValidation
import com.userdata.manager.viewmodel.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditUserBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewmodel by activityViewModels()

    private var user: UserEntity? = null

    companion object {
        private const val ARG_USER = "arg_user"

        fun newInstance(user: UserEntity? = null): AddEditUserBottomSheet {
            return AddEditUserBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_USER, UserEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetUserBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            user?.let { existingUser ->
                addUser.text = getString(R.string.edit_user)
                btnSave.text = getString(R.string.update_user)

                etName.setText(existingUser.name)
                etAddress.setText(existingUser.address)
                etPhone.setText(existingUser.phone)

            } ?: run {
                addUser.text = getString(R.string.add_user)
                btnSave.text = getString(R.string.save_user)
            }

            btnSave.setOnClickListener {
                saveUser()
            }
        }
    }


    private fun saveUser() {

        val name = binding.etName.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        val error = UserDataValidation.dataValidation(name, address, phone)

        if (error != null) {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            return
        }

        if (user == null) {
            addUser(name, address, phone)
        } else {
            updateUser(name, address, phone)
        }

        dismiss()
    }

    private fun addUser(name: String, address: String, phone: String) {

        val newUser = UserEntity(
            name = name,
            address = address,
            phone = phone
        )

        viewModel.insert(newUser)

        Toast.makeText(
            requireContext(),
            getString(R.string.user_added_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateUser(name: String, address: String, phone: String) {

        val existingUser = user ?: return

        val updatedUser = existingUser.copy(
            name = name,
            address = address,
            phone = phone
        )

        viewModel.update(updatedUser)

        Toast.makeText(
            requireContext(),
            getString(R.string.user_updated_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}