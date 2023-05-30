package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.ActivityHomeBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import academy.bangkit.capstone.suarakita.ui.profile.ProfileActivity
import academy.bangkit.capstone.suarakita.ui.vote.VerivoteActivity
import academy.bangkit.capstone.suarakita.ui.vote.VoteActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.models.SlideModel
import com.intrusoft.scatter.ChartData

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        setupName()
    }

    private fun setupAction() {
        binding.bottomNavigationView.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this, ProfileActivity::class.java)
                    startActivity(intentProfile)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener{
            val intentVote = Intent(this, VerivoteActivity::class.java)
            startActivity(intentVote)
        }
    }

    private fun setupView() {
        binding.bottomNavigationView.background = null
        supportActionBar?.hide()

        setupCarousel()
        setupPieChart()
    }

    private fun setupName() {
        homeViewModel.getUser().observe(this) {
            binding.haloName.text = it.name
        }
    }

    private fun setupCarousel(){
        val carouselList = ArrayList<SlideModel>()
        carouselList.add(SlideModel(R.drawable.sample1))
        carouselList.add(SlideModel(R.drawable.sample2))
        carouselList.add(SlideModel(R.drawable.sample3))
        binding.imageSlider.setImageList(carouselList)
    }

    private fun setupPieChart(){
        val pieChartList = ArrayList<ChartData>()
        pieChartList.add(ChartData("Paslon 1", 43f, Color.WHITE, Color.BLUE))
        pieChartList.add(ChartData("Paslon 2", 57f, Color.WHITE, Color.RED))
        binding.pieChart.setChartData(pieChartList)
    }
}