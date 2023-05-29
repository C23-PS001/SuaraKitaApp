package academy.bangkit.capstone.suarakita.ui.welcome

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityWelcomeBinding
import academy.bangkit.capstone.suarakita.ui.login.LoginActivity
import academy.bangkit.capstone.suarakita.ui.signup.SyaratActivity
import academy.bangkit.capstone.suarakita.ui.signup.VerifyActivity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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
        binding.loginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }

        binding.registerButton.setOnClickListener {
            val registerIntent = Intent(this, SyaratActivity::class.java)
            startActivity(registerIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}