package academy.bangkit.capstone.suarakita.ui.welcome

import academy.bangkit.capstone.suarakita.databinding.ActivityWelcomeBinding
import academy.bangkit.capstone.suarakita.ui.login.LoginActivity
import academy.bangkit.capstone.suarakita.ui.signup.SyaratActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()

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