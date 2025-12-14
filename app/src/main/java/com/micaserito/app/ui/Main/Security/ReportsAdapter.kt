package com.micaserito.app.ui.Main.Security

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.micaserito.app.R
import com.micaserito.app.data.model.ReportSummary

class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    private val reports = mutableListOf<ReportSummary>()

    fun addList(newReports: List<ReportSummary>) {
        reports.addAll(newReports)
        notifyItemRangeInserted(reports.size - newReports.size, newReports.size)
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
        // IDs Corregidos para coincidir con item_report.xml
        private val imgReportIcon: ImageView = itemView.findViewById(R.id.imgReportIcon)
        private val tvReportType: TextView = itemView.findViewById(R.id.tvReportType)
        private val tvReportTitle: TextView = itemView.findViewById(R.id.tvReportTitle)
        private val tvReportSubtitle: TextView = itemView.findViewById(R.id.tvReportSubtitle)
        private val tvStatusBadge: TextView = itemView.findViewById(R.id.tvStatusBadge)
        private val tvReportDate: TextView = itemView.findViewById(R.id.tvReportDate)

        fun bind(report: ReportSummary) {
            tvReportTitle.text = report.titulo
            tvReportSubtitle.text = report.usuarioRelacionado
            tvReportDate.text = report.fecha

            // 1. Configurar Icono y Color según el tipo de reporte
            when (report.tipoReporte.lowercase()) {
                "problema" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_dialog_alert) // Reemplazar con tu ic_warning
                    imgReportIcon.setColorFilter(Color.parseColor("#FBC02D")) // Amarillo
                    tvReportType.text = "Problema"
                }
                "estafa" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_notification_clear_all) // Reemplazar con tu ic_block
                    imgReportIcon.setColorFilter(Color.parseColor("#E53935")) // Rojo
                    tvReportType.text = "Estafa"
                }
                "entrega" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_menu_myplaces) // Reemplazar con tu ic_inventory
                    imgReportIcon.setColorFilter(Color.parseColor("#3949AB")) // Azul oscuro
                    tvReportType.text = "Entrega"
                }
                "retraso" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_menu_recent_history) // Reemplazar con tu ic_schedule
                    imgReportIcon.setColorFilter(Color.parseColor("#546E7A")) // Gris azulado
                    tvReportType.text = "Retraso"
                }
                 "falta" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_dialog_info)
                    imgReportIcon.setColorFilter(Color.parseColor("#8E8E93")) // Gris
                    tvReportType.text = "Falta"
                }
                "advertencia" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                    imgReportIcon.setColorFilter(Color.parseColor("#FBC02D")) // Amarillo
                    tvReportType.text = "Advertencia"
                }
                "sanción" -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_notification_clear_all)
                    imgReportIcon.setColorFilter(Color.parseColor("#E53935")) // Rojo
                    tvReportType.text = "Sanción"
                }
                else -> {
                    imgReportIcon.setImageResource(android.R.drawable.ic_dialog_info)
                    imgReportIcon.setColorFilter(Color.GRAY)
                    tvReportType.text = report.tipoReporte
                }
            }

            // 2. Configurar Badge de Estado (Borde y Texto)
            val background = tvStatusBadge.background.mutate() as GradientDrawable
            tvStatusBadge.text = report.estado

            val (textColor, borderColor) = when (report.estado.lowercase()) {
                "pendiente" -> Pair(Color.parseColor("#9E9E9E"), Color.parseColor("#9E9E9E"))
                "en revisión" -> Pair(Color.parseColor("#007AFF"), Color.parseColor("#007AFF"))
                "resuelta" -> Pair(Color.parseColor("#34C759"), Color.parseColor("#34C759"))
                "rechazada" -> Pair(Color.parseColor("#FF3B30"), Color.parseColor("#FF3B30"))
                "expirada" -> Pair(Color.parseColor("#8E8E93"), Color.parseColor("#8E8E93"))
                "activa" -> Pair(Color.parseColor("#34C759"), Color.parseColor("#34C759"))
                else -> Pair(Color.GRAY, Color.GRAY)
            }

            tvStatusBadge.setTextColor(textColor)
            background.setStroke(3, borderColor)
        }
    }
}
