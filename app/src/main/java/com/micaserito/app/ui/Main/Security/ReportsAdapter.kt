package com.micaserito.app.ui.Main.Security

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivReportIcon)
        private val tvType: TextView = itemView.findViewById(R.id.tvReportType)
        private val tvTitle: TextView = itemView.findViewById(R.id.txtReportTitle)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvReportSubtitle)
        private val tvStatus: TextView = itemView.findViewById(R.id.txtReportStatus)
        private val tvDate: TextView = itemView.findViewById(R.id.txtReportDate)

        fun bind(report: ReportSummary) {
            tvTitle.text = report.titulo
            tvSubtitle.text = report.usuarioRelacionado
            tvType.text = report.tipoReporte
            tvDate.text = report.fecha
            tvStatus.text = report.estado

            // --- Lógica para Iconos (CORREGIDO) ---
            val iconRes = when (report.tipoReporte.lowercase()) {
                "problema" -> android.R.drawable.ic_dialog_alert
                "estafa" -> android.R.drawable.ic_delete
                "entrega" -> android.R.drawable.ic_menu_myplaces
                "retraso" -> android.R.drawable.ic_menu_recent_history
                "falta" -> android.R.drawable.ic_dialog_info
                "advertencia" -> android.R.drawable.ic_dialog_alert
                "sanción" -> android.R.drawable.ic_delete
                else -> android.R.drawable.ic_dialog_info
            }
            ivIcon.setImageResource(iconRes)

            // --- Lógica para Colores de Estado ---
            val statusColor = when (report.estado.lowercase()) {
                "pendiente" -> "#E5E5EA"
                "en revisión" -> "#007AFF"
                "resuelta" -> "#34C759"
                "rechazada" -> "#FF3B30"
                "expirada" -> "#8E8E93"
                "activa" -> "#34C759"
                else -> "#E5E5EA"
            }
            val statusTextColor = when (report.estado.lowercase()) {
                "en revisión", "resuelta", "rechazada", "activa" -> Color.WHITE
                else -> Color.BLACK
            }

            tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor(statusColor))
            tvStatus.setTextColor(statusTextColor)
        }
    }
}
