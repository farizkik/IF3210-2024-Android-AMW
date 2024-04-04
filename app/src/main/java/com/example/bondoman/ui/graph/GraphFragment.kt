package com.example.bondoman.ui.graph

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.bondoman.databinding.FragmentGraphBinding
import kotlinx.coroutines.launch

class GraphFragment: Fragment() {

    private lateinit var anyChartView: AnyChartView;

    private var _binding: FragmentGraphBinding? = null

    private lateinit var graphViewModel: GraphViewModel

    private var pie: Pie? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        graphViewModel = ViewModelProvider(this).get(GraphViewModel::class.java)

        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        anyChartView = binding.graphChartFragment
        pie= AnyChart.pie()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val (pemasukan, pembelian) = graphViewModel.getCountTypeData()
            Log.d("Graph Fragment", "$pemasukan, $pembelian")
            updateChart(pemasukan, pembelian)
        }

    }

    private fun updateChart(pemasukanCount: Int, pembelianCount: Int){
        Log.d("Graph Fragment", "$pemasukanCount and $pembelianCount")
        val dataEntries: List<DataEntry> = listOf(
            ValueDataEntry("Pemasukan", pemasukanCount),
            ValueDataEntry("Pembelian", pembelianCount)
        )


        pie!!.data(dataEntries)
        anyChartView.setChart(pie)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}