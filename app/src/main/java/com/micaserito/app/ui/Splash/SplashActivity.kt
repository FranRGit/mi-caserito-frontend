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
import com.micaserito.app.R
import com.micaserito.app.data.Local.SessionManager
import com.micaserito.app.ui.Auth.AuthActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔴 CAMBIO CLAVE:
        // Limpia la sesión al iniciar la app (NO sesión persistente)
        SessionManager.clearSession(this)

        setContentView(R.layout.activity_splash)

        // Referencias
        val tvMi = findViewById<TextView>(R.id.tvMi)
        val tvCaserito = findViewById<TextView>(R.id.tvCaserito)
        val ivCart = findViewById<ImageView>(R.id.ivCart)
        val btnIniciar = findViewById<MaterialButton>(R.id.btnIniciar)

        // Animaciones iniciales
        ivCart.translationX = -800f
        ivCart.alpha = 0f
        ivCart.rotation = -20f

        tvMi.translationX = -400f
        tvMi.alpha = 0f
        tvCaserito.translationX = -400f
        tvCaserito.alpha = 0f

        btnIniciar.alpha = 0f
        btnIniciar.scaleX = 0f
        btnIniciar.scaleY = 0f
        btnIniciar.rotation = -360f

        ivCart.animate()
            .translationX(0f)
            .alpha(1f)
            .rotation(0f)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(1200)
            .start()

        tvMi.animate()
            .translationX(0f)
            .alpha(1f)
            .setStartDelay(400)
            .setDuration(1000)
            .start()

        tvCaserito.animate()
            .translationX(0f)
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(1000)
            .withEndAction {
                btnIniciar.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .rotation(0f)
                    .setInterpolator(OvershootInterpolator(1.2f))
                    .setDuration(800)
                    .withEndAction {
                        vibrarDispositivo(100)
                        iniciarLatido(btnIniciar)
                    }
                    .start()
            }
            .start()

        // Ir a Login / Registro
        btnIniciar.setOnClickListener {
            btnIniciar.clearAnimation()
            vibrarDispositivo(50)
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    private fun iniciarLatido(view: View) {
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.1f),
            PropertyValuesHolder.ofFloat("scaleY", 1.1f)
        )
        animator.duration = 800
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun vibrarDispositivo(ms: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    ms,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(ms)
        }
    }
}
