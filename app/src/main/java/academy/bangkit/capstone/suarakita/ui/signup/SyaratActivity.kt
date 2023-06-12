package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.databinding.ActivitySyaratBinding
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SyaratActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySyaratBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyaratBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupAction()
    }


    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            val loginIntent = Intent(this, KtpActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}