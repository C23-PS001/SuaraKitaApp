package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityHomeBinding
import academy.bangkit.capstone.suarakita.ui.vote.VerivoteActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.bottomNavigationView.setOnItemSelectedListener () { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> setFragment(HomeFragment())
                R.id.profile -> setFragment(ProfileFragment())
            }
            true
        }

        binding.fab.setOnClickListener{
            val intentVote = Intent(this, VerivoteActivity::class.java)
            startActivity(intentVote)
        }
    }

    private fun setFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment,fragment)
            commit()
        }

    private fun setupView() {
        binding.bottomNavigationView.background = null
        supportActionBar?.hide()

        setFragment(HomeFragment())
    }

}