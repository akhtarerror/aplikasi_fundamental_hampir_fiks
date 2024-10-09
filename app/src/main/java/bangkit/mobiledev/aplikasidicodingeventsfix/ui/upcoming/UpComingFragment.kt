package bangkit.mobiledev.aplikasidicodingeventsfix.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.aplikasidicodingeventsfix.data.response.ListEventsResponse
import bangkit.mobiledev.aplikasidicodingeventsfix.data.retrofit.ApiConfig
import bangkit.mobiledev.aplikasidicodingeventsfix.databinding.FragmentUpComingBinding
import bangkit.mobiledev.aplikasidicodingeventsfix.ui.adapter.ListEventsAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpComingFragment : Fragment() {

    private var _binding: FragmentUpComingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpComingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListEventsAdapter()
        binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext()) // Menetapkan LayoutManager
        binding.rvHeroes.adapter = adapter
        fetchEvents()
    }


    private fun fetchEvents() {
        if (_binding == null) {
            Log.e("UpComingFragment", "Binding is null")
        } else {
            showLoading(true)
            val apiService = ApiConfig.getApiService()
            val call = apiService.getEvents(1)
            call.enqueue(object : Callback<ListEventsResponse> {
                override fun onResponse(
                    call: Call<ListEventsResponse>,
                    response: Response<ListEventsResponse>
                ) {
                    if (_binding != null) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            Log.d("UpComingFragment", "Data received: ${response.body()}")
                            val events = response.body()?.listEvents
                            events?.let {
                                adapter.submitList(it)
                            } ?: run {
                                showError("Data kosong")
                            }
                        } else {
                            showError("Gagal mengambil data: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<ListEventsResponse>, t: Throwable) {
                    if (_binding != null) {
                        showLoading(false)
                        showError("Gagal terhubung ke server: ${t.message}")
                    }
                    Log.e("UpComingFragment", "Error fetching events", t)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let {
            it.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}