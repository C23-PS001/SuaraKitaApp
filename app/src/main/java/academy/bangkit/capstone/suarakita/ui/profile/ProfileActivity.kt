package academy.bangkit.capstone.suarakita.ui.profile

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityProfileBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.UserItem
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.home.HomeActivity
import academy.bangkit.capstone.suarakita.ui.main.MainActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[ProfileViewModel::class.java]

        profileViewModel.getUser().observe(this) { user ->
            profileViewModel.getProfile(user.token)
        }

        profileViewModel.response.observe(this) { user ->
            setUserData(user)
        }
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
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intentHome = Intent(this, HomeActivity::class.java)
                    startActivity(intentHome)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener{

        }

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        binding.bottomNavigationView.background = null
        supportActionBar?.hide()
    }
}