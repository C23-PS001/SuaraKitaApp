package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.databinding.ActivityVerifyBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
        supportActionBar?.hide()

        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        setupAction()
        showLoading(false)

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun setupAction() {
        binding.namaEditText.setText(intent.getStringExtra("name"))
        binding.nikEditText.setText(intent.getStringExtra("nik"))
        binding.tanggalEditText.setText(intent.getStringExtra("dob"))

        binding.tanggalEditText.isFocusable = false
        binding.tanggalEditText.isClickable = true

        binding.tanggalEditText.setOnClickListener{
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(supportFragmentManager, "date_picker")

            datePicker.addOnPositiveButtonClickListener {
                val selectedDate = Date(it)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)
                binding.tanggalEditText.setText(formattedDate.toString())
            }
        }

        binding.verifyButton.setOnClickListener {
            var nik = binding.nikEditText.text.toString()
            var nama = binding.namaEditText.text.toString()
            var tanggal = binding.tanggalEditText.text.toString()
            var email = binding.emailEditText.text.toString()
            var password = binding.passEditText.text.toString()
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
                                val intent = Intent(this, FaceActivity::class.java)
                                intent.putExtra("userId", it.idUser)
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