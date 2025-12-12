package com.micaserito.app.ui.Main.Security

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ReportSummary

class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    private val reports = mutableListOf<ReportSummary>()

    fun addList(newReports: List<ReportSummary>) {
        val start = reports.size
        reports.addAll(newReports)
        notifyItemRangeInserted(start, newReports.size)
    }

    fun clear() {
        reports.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txtReportTitle)
        private val txtStatus: TextView = itemView.findViewById(R.id.txtReportStatus)
        private val txtDate: TextView = itemView.findViewById(R.id.txtReportDate)

        fun bind(report: ReportSummary) {
            txtTitle.text = report.titulo
            txtStatus.text = "Estado: ${report.estado}" // Se recomienda usar String Resources
            txtDate.text = report.fecha

            // Cambio de color simple según estado
            val color = if (report.estado.equals("pendiente", ignoreCase = true)) {
                "#FFA000" // Naranja
            } else {
                "#388E3C" // Verde
            }
            txtStatus.setTextColor(Color.parseColor(color)) // Se recomienda usar Color Resources
        }
    }
}
