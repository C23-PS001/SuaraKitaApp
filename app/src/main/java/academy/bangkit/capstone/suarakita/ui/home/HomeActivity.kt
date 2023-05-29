package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityHomeBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.profile.ProfileActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        setupName()
    }

    private fun setupAction() {
        binding.bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this, ProfileActivity::class.java)
                    startActivity(intentProfile)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener{
            Toast.makeText(this, "Fab", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupView() {
        binding.bottomNavigationView.background = null
        supportActionBar?.hide()
    }

    private fun setupName() {
        homeViewModel.getUser().observe(this, {
                binding.haloName.text =it.name
        })
    }
}