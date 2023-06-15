package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.databinding.ActivityVerifyIdBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
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

class VerifyIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyIdBinding
    private lateinit var voteViewModel: VoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        voteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[VoteViewModel::class.java]

        supportActionBar?.hide()
        setupAction()
        showLoading(false)

        voteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            val nik = binding.emailEditText.text.toString()
            if (nik.isEmpty()) {
                binding.emailEditText.error = "NIK tidak boleh kosong"
            } else {
                val hashNik = voteViewModel.hashNik(nik)
                voteViewModel.getVoteStatus(hashNik)
                voteViewModel.voteStatus.observe(this) {
                    if (it == null) {
                        Toast.makeText(this, "Mohon maaf sedang ada kendala", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    if (it.isVoted == false && it.dataExist == true) {
                        Toast.makeText(this, "Anda memiliki hak suara", Toast.LENGTH_SHORT).show()
                        val intentVote = Intent(this, VerivoteActivity::class.java)
                        startActivity(intentVote)
                        finish()
                    } else if (it.isVoted == true && it.dataExist == true) {
                        Toast.makeText(this, "Anda sudah memilih", Toast.LENGTH_SHORT).show()

                    } else if (it.isVoted == true && it.dataExist == false) {
                        Toast.makeText(this, "Data NIK anda tidak sesuai", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}