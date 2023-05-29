package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.databinding.ActivityVerifyBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.login.LoginActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        signupViewModel.response.observe(this) {
            if (it.equals("User anda telah berhasil terdaftar")) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.namaEditText.setText("John Doe")
        binding.nikEditText.setText("1233423424523")
        binding.verifyButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailField.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passField.error = "Masukkan password"
                }
                else -> {
                    signupViewModel.registerUser(email, password)

                }
            }

        }
    }
}