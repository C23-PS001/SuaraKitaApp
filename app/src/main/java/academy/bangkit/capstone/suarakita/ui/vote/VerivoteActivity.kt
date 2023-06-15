package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.databinding.ActivityVerivoteBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.camera.SelfieActivity
import academy.bangkit.capstone.suarakita.ui.camera.rotateFile
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VerivoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerivoteBinding
    private lateinit var voteViewModel: VoteViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerivoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        showLoading(false)
        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        voteViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.uploadButton.setOnClickListener { startUploadPhoto() }
    }

    private fun setupViewModel() {
        voteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[VoteViewModel::class.java]
    }

    private fun startTakePhoto() {
        val intent = Intent(this, SelfieActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private var getFile: File? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startUploadPhoto() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "Gambar",
                file.name,
                requestImageFile
            )

            voteViewModel.getUser().observe(this) {
                val idUser = it.token.toRequestBody("text/plain".toMediaTypeOrNull())
                voteViewModel.verifikasiWajah(imageMultipart, idUser)
            }

            voteViewModel.faceResponse.observe(this) {
                if (it != null) {
                    if(it.error == "false") {
                        Toast.makeText(this, "Verifikasi Wajah Berhasil", Toast.LENGTH_SHORT).show()
                        val intentVote = Intent(this, GuideActivity::class.java)
                        startActivity(intentVote)
                        finish()
                    } else {
                        Toast.makeText(this, "Verifikasi wajah gagal, mohon coba lagi", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Mohon pastikan wajah anda terlihat dan terunggah", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}