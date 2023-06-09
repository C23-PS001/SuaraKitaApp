package academy.bangkit.capstone.suarakita.ui.login

import academy.bangkit.capstone.suarakita.databinding.ActivityLoginBinding
import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.home.HomeActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()

        showLoading(false)

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.response.observe(this) {
            if (it != null) {
                if (!it.error) {
                    loginViewModel.saveUser(UserModel(it.nama, it.id, true))
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            loginViewModel.login(email, password)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}