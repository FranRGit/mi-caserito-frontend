package com.micaserito.app.ui.Splash

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.micaserito.app.ui.Main.MainActivity
import com.micaserito.app.R
import com.micaserito.app.ui.Auth.AuthActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Referencias
        val tvMi = findViewById<TextView>(R.id.tvMi)
        val tvCaserito = findViewById<TextView>(R.id.tvCaserito)
        val ivCart = findViewById<ImageView>(R.id.ivCart)
        val btnIniciar = findViewById<MaterialButton>(R.id.btnIniciar)

        // Carrito: Empieza lejos y un poco inclinado
        ivCart.translationX = -800f
        ivCart.alpha = 0f
        ivCart.rotation = -20f

        // Texto: Empieza desplazado
        tvMi.translationX = -400f
        tvMi.alpha = 0f
        tvCaserito.translationX = -400f
        tvCaserito.alpha = 0f

        // Botón: Invisible y pequeño
        btnIniciar.alpha = 0f
        btnIniciar.scaleX = 0f
        btnIniciar.scaleY = 0f
        btnIniciar.rotation = -360f // Dará una vuelta completa

        // ENTRADA DEL CARRITO
        ivCart.animate()
            .translationX(0f)
            .alpha(1f)
            .rotation(0f) // Se endereza al llegar
            .setInterpolator(DecelerateInterpolator()) // Frena suave al final
            .setDuration(1200)
            .start()

        // ENTRADA DEL TEXTO
        val textDuration = 1000L
        val textDelay = 400L

        tvMi.animate()
            .translationX(0f).alpha(1f)
            .setStartDelay(textDelay)
            .setDuration(textDuration)
            .setInterpolator(DecelerateInterpolator())
            .start()

        tvCaserito.animate()
            .translationX(0f).alpha(1f)
            .setStartDelay(textDelay + 100) // "Caserito" sale un pelín después de "Mi"
            .setDuration(textDuration)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                btnIniciar.animate()
                    .scaleX(1f).scaleY(1f).alpha(1f)
                    .rotation(0f)
                    .setInterpolator(OvershootInterpolator(1.2f)) // Rebote elegante
                    .setDuration(800)
                    .withEndAction {
                        // AQUÍ TERMINA TODA LA INTRO:

                        // 1. Vibrar para avisar que está listo
                        vibrarDispositivo(100)

                        // 2. Iniciar animación de "Latido" para pedir clic
                        iniciarLatido(btnIniciar)
                    }
                    .start()
            }.start()
        btnIniciar.setOnClickListener {
            // Cancelamos el latido para que no interfiera
            btnIniciar.clearAnimation()
            // Vibración corta de confirmación
            vibrarDispositivo(50)
            // Animación de salida rápida
            btnIniciar.animate()
                .scaleX(0.8f).scaleY(0.8f) // Se encoge al presionar
                .setDuration(100)
                .withEndAction {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .start()
        }
    }
    // Animación infinita
    private fun iniciarLatido(view: View) {
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.1f), // Crece un 10%
            PropertyValuesHolder.ofFloat("scaleY", 1.1f)
        )
        scaleDown.duration = 800 // Velocidad del latido
        scaleDown.repeatCount = ObjectAnimator.INFINITE // Nunca para
        scaleDown.repeatMode = ObjectAnimator.REVERSE // Crece y decrece
        scaleDown.start()
    }
    private fun vibrarDispositivo(milisegundos: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milisegundos, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milisegundos)
        }
    }
}