package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.databinding.ActivityVoteBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoteBinding
    private lateinit var voteViewModel: VoteViewModel

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        voteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[VoteViewModel::class.java]

        supportActionBar?.hide()
        setupAction()
    }

    private fun setupAction() {
        countDownTimer = object : CountDownTimer(900000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString() + " detik"
            }

            override fun onFinish() {
                openDialog("habis")
            }
        }

        binding.card1.setOnClickListener {
            openDialog("1")
        }

        binding.card2.setOnClickListener {
            openDialog("2")
        }
    }

    override fun onStart() {
        super.onStart()
        countDownTimer.start()
    }

    override fun onBackPressed() {
        openDialog("keluar")
    }

    private fun openDialog(message: String) {
        val builder = MaterialAlertDialogBuilder(this)
        when{
            message == "1" -> {
                builder.setMessage("Anda memilih pasangan nomor urut 1")
                builder.setPositiveButton("Pilih") { dialog, _ ->
                    goVote(1)
                    dialog.dismiss()
                    finish()
                }
                builder.setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            message == "2" -> {
                builder.setMessage("Anda memilih pasangan calon nomor urut 2")
                builder.setPositiveButton("Pilih") { dialog, _ ->
                    goVote(2)
                    dialog.dismiss()
                    finish()
                }
                builder.setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            message == "habis" -> {
                builder.setMessage("Waktu anda telah habis")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
            }
            message == "keluar" -> {
                builder.setMessage("Apakah anda yakin ingin keluar?")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                builder.setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
            }
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun goVote(candNum: Int) {
        voteViewModel.getUser().observe(this) {
            voteViewModel.setVote(it.token, candNum)
        }
    }
}