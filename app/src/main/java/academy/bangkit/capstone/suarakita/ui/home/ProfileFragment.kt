package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.databinding.FragmentProfileBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.UserItem
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.main.MainActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding: FragmentProfileBinding ?= null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(viewLifecycleOwner) { user ->
            profileViewModel.getProfile(user.token)
        }

        profileViewModel.response.observe(viewLifecycleOwner) { user ->
            setUserData(user)
        }

        setupAction()

        profileViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun setUserData(user: List<UserItem>?) {
        if (user != null) {
            binding.namaLengkap.text = user[0].nama
            binding.tanggalLahir.text = user[0].tanggalLahir
            binding.email.text = user[0].email
            binding.nik.text = user[0].nik.toString()
        }
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}