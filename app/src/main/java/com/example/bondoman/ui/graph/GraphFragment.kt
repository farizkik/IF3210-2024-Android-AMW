package com.example.bondoman.ui.graph

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Pie
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentGraphBinding

class GraphFragment: Fragment() {

    private lateinit var anyChartView: AnyChartView;

    private var _binding: FragmentGraphBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val graphViewModel = ViewModelProvider(this).get(GraphViewModel::class.java)
        graphViewModel.getCountTypeData()

        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        anyChartView = binding.graphChartFragment
        graphViewModel.countData.observe(viewLifecycleOwner) { dataEntries ->
            setupChartView(dataEntries)
        }

        return root
    }

    private fun setupChartView(dataEntries: List<DataEntry>) {
        val pie: Pie = AnyChart.pie()
        pie.data(dataEntries)

        anyChartView.setChart(pie)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}