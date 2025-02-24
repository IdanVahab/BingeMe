package com.example.bingeme.utils

import android.view.View

fun <T : View> setupButtonWithAnimation(button: T, action: () -> Unit) {
    button.setOnClickListener {
        button.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(150)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .withEndAction {
                        action()
                    }.start()
            }.start()
    }
}