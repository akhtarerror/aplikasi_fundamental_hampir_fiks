package bangkit.mobiledev.aplikasidicodingeventsfix.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.aplikasidicodingeventsfix.data.response.ListEventsItem
import bangkit.mobiledev.aplikasidicodingeventsfix.data.response.ListEventsResponse
import bangkit.mobiledev.aplikasidicodingeventsfix.data.retrofit.ApiConfig
import bangkit.mobiledev.aplikasidicodingeventsfix.databinding.FragmentHomeBinding
import bangkit.mobiledev.aplikasidicodingeventsfix.ui.adapter.ListEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        setupRecyclerView()

        // Call API to fetch events
        fetchEvents()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ListEventsAdapter()
        binding.rvEvents.layoutManager = LinearLayoutManager(activity)
        binding.rvEvents.adapter = adapter
    }

    private fun fetchEvents() {
        val client = ApiConfig.getApiService().getEvents(active = 1)
        client.enqueue(object : Callback<ListEventsResponse> {
            override fun onResponse(
                call: Call<ListEventsResponse>,
                response: Response<ListEventsResponse>
            ) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    if (events != null) {
                        adapter.submitList(events.filterNotNull())
                    } else {
                        Toast.makeText(context, "No events available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ListEventsResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
