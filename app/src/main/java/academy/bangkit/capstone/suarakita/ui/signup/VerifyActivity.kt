package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.databinding.ActivityVerifyBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        setupView()
        setupAction()
        showLoading(false)

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
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
        binding.namaEditText.setText("Menyiram Bunga")
        binding.nikEditText.setText("1234567890123456")
        binding.tanggalEditText.setText("2000/01/01")

        binding.tanggalEditText.isFocusable = false
        binding.tanggalEditText.isClickable = true

        binding.tanggalEditText.setOnClickListener{
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(supportFragmentManager, "date_picker_tag")

            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = Date(it)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)
                binding.tanggalEditText.setText(formattedDate)
            }
        }

        binding.verifyButton.setOnClickListener {
            val nik = binding.nikEditText.text.toString()
            val nama = binding.namaEditText.text.toString()
            val tanggal = binding.tanggalEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            when {
                nik.isEmpty() -> {
                    binding.nikField.error = "Masukkan NIK"
                }
                nama.isEmpty() -> {
                    binding.namaField.error = "Masukkan nama"
                }
                tanggal.isEmpty() -> {
                    binding.tanggalField.error = "Masukkan tanggal lahir"
                }
                email.isEmpty() -> {
                    binding.emailField.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passField.error = "Masukkan password"
                }
                else -> {
                    Log.d("VerifyActivity", "verify: $tanggal")
                    signupViewModel.registerUser(nama, signupViewModel.hashNik(nik), tanggal, email, password)
                    signupViewModel.response.observe(this) {
                        if (it != null) {
                            if(!it.error) {
                                val intent = Intent(this, FaceCompareActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}