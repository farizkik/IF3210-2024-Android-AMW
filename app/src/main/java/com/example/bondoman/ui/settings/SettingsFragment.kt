package com.example.bondoman.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.service.auth.TokenExpService
import com.example.bondoman.share_preference.PreferenceManager
import com.example.bondoman.ui.login.LoginActivity
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    private lateinit var preferenceManager: PreferenceManager

    private fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
        val row = sheet.createRow(0)

        val headerList = listOf("Tanggal", "Kategori", "Nominal", "Nama", "Lokasi")

        for ((index, value) in headerList.withIndex()) {
            val columnWidth = 15 * 500

            sheet.setColumnWidth(index, columnWidth)

            val cell = row.createCell(index)

            cell?.setCellValue(value)

            cell.cellStyle = cellStyle
        }
    }

    private fun addData(
        rowIndex: Int,
        transaction: Transaction,
        cellStyle: CellStyle,
        sheet: Sheet
    ) {
        val row = sheet.createRow(rowIndex)

        // Tanggal
        val dateCell = row.createCell(0)
        val dateFormat = SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.getDefault())
        dateCell?.setCellValue(dateFormat.format(Date(transaction.creationTime)))
        dateCell.cellStyle = cellStyle

        // Kategori
        val categoryCell = row.createCell(1)
        categoryCell?.setCellValue(transaction.type)
        categoryCell.cellStyle = cellStyle

        // Nominal
        val nominalCell = row.createCell(2)
        nominalCell?.setCellValue(transaction.nominal.toString())
        nominalCell.cellStyle = cellStyle

        // Nama
        val nameCell = row.createCell(3)
        nameCell?.setCellValue(transaction.title)
        nameCell.cellStyle = cellStyle

        // Lokasi
        val locationCell = row.createCell(4)
        locationCell.setCellValue(transaction.location)
        locationCell.cellStyle = cellStyle
    }

    private fun createWorkbook(): Workbook {
        val workbook = XSSFWorkbook()

        val dateFormat = SimpleDateFormat("HH-mm-ss-dd-MM-yyyy", Locale.getDefault())

        val sheet: Sheet = workbook.createSheet("Transaksi-" + dateFormat.format(Date()))

        val headerCellStyle = workbook.createCellStyle()
        headerCellStyle.fillForegroundColor = IndexedColors.SKY_BLUE.index
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER)
        headerCellStyle.setBorderTop(BorderStyle.MEDIUM)
        headerCellStyle.setBorderBottom(BorderStyle.MEDIUM)
        headerCellStyle.setBorderLeft(BorderStyle.MEDIUM)
        headerCellStyle.setBorderRight(BorderStyle.MEDIUM)

        createSheetHeader(headerCellStyle, sheet)

        val cellStyle = workbook.createCellStyle()
        cellStyle.setBorderTop(BorderStyle.MEDIUM)
        cellStyle.setBorderBottom(BorderStyle.MEDIUM)
        cellStyle.setBorderLeft(BorderStyle.MEDIUM)
        cellStyle.setBorderRight(BorderStyle.MEDIUM)

        val transactions = viewModel.transactions.value!!

        transactions.forEachIndexed { index, transaction ->
            addData(index + 1, transaction, cellStyle, sheet)
        }

        return workbook
    }

    private fun saveToExcel() {
        val workbook = createWorkbook()

        val appDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        val dateFormat = SimpleDateFormat("HH-mm-ss-dd-MM-yyyy", Locale.getDefault())

        var fileName = "Transaksi-" + dateFormat.format(Date())

        fileName += if (binding.radioButtonOptionXls.isChecked) {
            ".xls"
        } else {
            ".xlsx"
        }

        val excelFile = File(appDirectory, fileName)

        try {
            val fileOut = FileOutputStream(excelFile)
            workbook.write(fileOut)
            fileOut.close()

            Toast.makeText(
                requireContext(),
                "Spreadsheet saved in $appDirectory/$fileName",
                Toast.LENGTH_LONG
            ).show()

            Log.d("Settings Fragment", "Saved successfully in $appDirectory/$fileName")
        } catch (err: Exception) {
            Log.d("Settings Fragment", err.toString())
        }
    }

    private fun sendEmail() {
        val workbook = createWorkbook()

        val suffix = if (binding.radioButtonOptionXls.isChecked) {
            ".xls"
        } else {
            ".xlsx"
        }

        val excelFile = File.createTempFile("Transaksi_", suffix, requireContext().cacheDir)

        try {
            val fileOut = FileOutputStream(excelFile)
            workbook.write(fileOut)
            fileOut.close()

            Log.d("Settings Fragment", "Saved successfully")
        } catch (err: Exception) {
            Log.d("Settings Fragment", err.toString())
        }

        val fileUri: Uri =
            FileProvider.getUriForFile(requireContext(), "com.example.counter", excelFile)

        var intent = Intent(Intent.ACTION_SEND)
            .setType("application/excel")
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(preferenceManager.getEmail().toString()))
            .putExtra(Intent.EXTRA_SUBJECT, "Data Transaksi")
            .putExtra(Intent.EXTRA_TEXT, "This email is generated automatically")
            .putExtra(Intent.EXTRA_STREAM, fileUri)
            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(Intent.createChooser(intent, "Send Mail"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.radioButtonOptionXls.isChecked = true

        preferenceManager = context?.let { PreferenceManager(it) }!!
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewModel.getTransactions()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        )
        binding.randomizeButton.setOnClickListener {
            val intent = Intent("com.example.bondoman.action")
            intent.putExtra("message", "hello")
            requireContext().sendBroadcast(intent)
        }

        // Save spreadsheets
        binding.settingsButtonSaveSpreadsheet.setOnClickListener {
            Log.d("Settings Fragment", viewModel.transactions.value.toString())

            saveToExcel()
        }

        // Send email
        binding.settingsButtonSendEmail.setOnClickListener {
            Log.d("Settings Fragment", viewModel.transactions.value.toString())

            sendEmail()
        }

        // Logout
        binding.logoutButton.setOnClickListener {
            preferenceManager.removePref()
            Intent(requireContext(), TokenExpService::class.java).also {
                it.action = TokenExpService.Actions.STOP.toString()
                requireContext().startService(it)
            }
            Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}