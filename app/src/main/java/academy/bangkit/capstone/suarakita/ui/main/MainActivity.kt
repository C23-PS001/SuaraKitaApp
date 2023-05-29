package academy.bangkit.capstone.suarakita.ui.main

import academy.bangkit.capstone.suarakita.databinding.ActivityMainBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.home.HomeActivity
import academy.bangkit.capstone.suarakita.ui.welcome.WelcomeActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onResume(){
        super.onResume()
        setupViewModel()
    }

    private fun setupViewModel(){
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
            }else{
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        })
    }
}