package com.example.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private lateinit var reflectionView: View

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }

    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }

    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    private var isDown = 0

    private var animatorSet = AnimatorSet()

    private var sunStartSave: Float = 0.0f
    private var reflectionStartSave: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)
        reflectionView = findViewById(R.id.sun_reflection)

        rotateAnimation()

        sceneView.setOnClickListener {
            when (isDown) {
                0 -> {startAnimation(); isDown++}
                1 -> {reverseAnimation(); isDown = 0}
            }
        }
    }

    private fun rotateAnimation() {
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "rotation", 0f, 360f)
        heightAnimator.apply {
            duration = 3000
            repeatCount = Animation.INFINITE
            interpolator = null
        }
        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
        animatorSet.start()
    }

    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        sunStartSave = sunYStart
        val sunYEnd = skyView.height.toFloat()

        val reflectionYStart = reflectionView.top.toFloat()
        reflectionStartSave = reflectionYStart
        val reflectionYEnd = skyView.top.toFloat() - reflectionView.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val reflectionAnimator = ObjectAnimator
            .ofFloat(reflectionView, "y", reflectionYStart, reflectionYEnd)
            .setDuration(3000)
        reflectionAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)

        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .with(reflectionAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
    }

    private fun reverseAnimation() {

        val sunYStart = skyView.height.toFloat()
        val sunYEnd = sunStartSave

        val reflectionYStart =  skyView.top.toFloat() - reflectionView.height.toFloat()
        val reflectionYEnd = reflectionStartSave

        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val reflectionAnimator = ObjectAnimator
            .ofFloat(reflectionView, "y", reflectionYStart, reflectionYEnd)
            .setDuration(3000)
        reflectionAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        animatorSet = AnimatorSet()
        animatorSet.play(nightSkyAnimator)
            .before(sunsetSkyAnimator)
            .before(reflectionAnimator)
            .before(heightAnimator)
        animatorSet.start()
    }

}