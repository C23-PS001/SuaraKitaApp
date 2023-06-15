package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.databinding.ActivityGuideBinding
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupActions()
    }

    private fun setupActions() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.voteButton.setOnClickListener {
            val voteIntent = Intent(this, VoteActivity::class.java)
            startActivity(voteIntent)
            finish()
        }
    }
}