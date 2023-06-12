package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.R
import academy.bangkit.capstone.suarakita.databinding.FragmentHomeBinding
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.ui.ViewModelFactory
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.models.SlideModel
import com.intrusoft.scatter.ChartData

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding ?= null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]

        homeViewModel.getUser().observe(viewLifecycleOwner) {
            binding.haloName.text = it.name
        }

        setupCarousel()
        setupPieChart()

        return binding.root
    }

    private fun setupCarousel(){
        val carouselList = ArrayList<SlideModel>()
        carouselList.add(SlideModel(R.drawable.trivia))
        carouselList.add(SlideModel(R.drawable.trivia3))
        carouselList.add(SlideModel(R.drawable.trivia2))
        binding.imageSlider.setImageList(carouselList)
    }

    private fun setupPieChart(){
        val pieChartList = ArrayList<ChartData>()
        pieChartList.add(ChartData("Paslon 1", 53.2f, Color.BLACK, Color.LTGRAY))
        pieChartList.add(ChartData("Paslon 2", 46.8f, Color.WHITE, Color.DKGRAY))
        Log.d("PieChart", "Pie Chart Data: $pieChartList")
        binding.pieChart.setChartData(pieChartList)
    }

}