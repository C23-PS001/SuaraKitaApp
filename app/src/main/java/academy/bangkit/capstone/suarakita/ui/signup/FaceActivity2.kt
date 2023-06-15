package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityFace2Binding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.camera.SelfieActivity
import academy.bangkit.capstone.suarakita.ui.camera.rotateFile
import academy.bangkit.capstone.suarakita.ui.login.LoginActivity
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FaceActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityFace2Binding
    private lateinit var signupViewModel: SignupViewModel

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
        binding = ActivityFace2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")

        val fileString = intent.getStringExtra("image1")
        val fileUri: Uri = Uri.parse(fileString)

        val file1 = File(fileUri.path)
        val requestImageFile1 = file1.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart1: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image1",
            "foto1$userId.jpg",
            requestImageFile1
        )

        setupViewModel()
        setupView()
        showLoading(false)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.uploadButton.setOnClickListener { startUploadPhoto(imageMultipart1) }
    }

    private fun setupView() {
        supportActionBar?.hide()

        Glide.with(this)
            .asGif()
            .load(R.drawable.selfie_guide2)
            .into(binding.previewImageView)
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
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

    private fun startUploadPhoto(imageMultipart1: MultipartBody.Part) {
        val userId = intent.getStringExtra("idUser")?.toRequestBody("text/plain".toMediaTypeOrNull())
        if (getFile != null) {

            val file2 = reduceFileImage(getFile as File)
            val requestImageFile2 = file2.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart2: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image2",
                "fotodua_${intent.getStringExtra("idUser")}.jpg",
                requestImageFile2
            )

            Toast.makeText(this, "Sedang mengenali wajah anda", Toast.LENGTH_SHORT).show()

            if (userId != null) {
                signupViewModel.uploadPhoto(imageMultipart1, imageMultipart2, userId)
                signupViewModel.faceResponse.observe(this) {
                    if (it != null) {
                        if (it.error == "false") {
                            val intent = Intent(this, LoginActivity::class.java)
                            Toast.makeText(this,"Data wajah berhasil dipelajari!",Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this,"Gagal mengirimkan data wajah!",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Log.d("FaceActivity2", "startUploadPhoto: userId is null")
            }

        } else {
            Toast.makeText(
                this,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
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