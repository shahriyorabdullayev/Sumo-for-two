package com.sumo.fortwo2.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sumo.fortwo2.R
import com.sumo.fortwo2.common.*
import com.sumo.fortwo2.databinding.FragmentGameBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextLong

class GameFragment : Fragment(R.layout.fragment_game) {

    private val TAG = "@@@"

    private var width: Int = 0
    private var height: Int = 0

    private val binding by viewBinding { FragmentGameBinding.bind(it) }
    private lateinit var timerItemList: ArrayList<String>

    private lateinit var topStartList: ArrayList<ImageView>
    private lateinit var bottomStartList: ArrayList<ImageView>


    private var jobTatami: Job? = null
    private var jobSumoRotate: Job? = null
    private var jobCheckResult: Job? = null
    private var jobBot: Job? = null
    private var timer: CountDownTimer?=null
    private var isRotate = false

    var redY1 = 0f
    var blueY1 = 0f

    var redY2 = 0f
    var blueY2 = 0f

    private var redScore = 0
    private var blueScore = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateDisplayMetrics()

        Log.d(TAG, "onViewCreated: ${binding.sumoRed.absY()}  ${binding.sumoRed.absX()}")
        Log.d(TAG, "onViewCreated: ${binding.sumoBlue.absY()}  ${binding.sumoBlue.absX()}")
        initTimerList()
        initTopStarList()
        initBottomStarList()

        startTimer()


    }

    private fun blueSumoClicked() {
        redY2 += 15
        blueY2 += 15
        redY1 -= 15
        blueY1 -= 15
        ObjectAnimator.ofFloat(binding.sumoRed, "translationY", -redY2).start()
        ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", -blueY2).start()
        binding.sumoBlue.clearAnimation()
        binding.sumoRed.clearAnimation()
    }

    private fun redSumoClicked() {
        redY1 += 15
        blueY1 += 15
        redY2 -= 15f
        blueY2 -= 15f
        ObjectAnimator.ofFloat(binding.sumoRed, "translationY", redY1).start()
        ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", blueY1).start()
        binding.sumoRed.clearAnimation()
        binding.sumoBlue.clearAnimation()
    }

    private fun jobTatamiHandle() {
        jobTatami = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (isActive) {
                val random = nextLong(1500, 2500)
                delay(random)
                binding.tatami.apply {
                    if (nextBoolean()) {
                        setImageResource(R.drawable.tatami_blue_1)
                        disablePinkButtons()
                        enableBlueButtons()
                        delay(random / 3)
                        setImageResource(R.drawable.tatami_blue_2)
                        delay(random / 3)
                        setImageResource(R.drawable.tatami_blue_3)
                    } else {
                        setImageResource(R.drawable.tatami_red_1)
                        disableBlueButtons()
                        enablePinkButtons()
                        delay(random / 3)
                        setImageResource(R.drawable.tatami_red_2)
                        delay(random / 3)
                        setImageResource(R.drawable.tatami_red_3)
                    }
                }


            }
        }
    }


    private fun disableBlueButtons() {
        binding.apply {
            topBluePush.disabled()
            bottomBluePush.disabled()
        }
    }

    private fun disablePinkButtons() {
        binding.apply {
            topPinkPush.disabled()
            bottomPinkPush.disabled()
        }
    }

    private fun enablePinkButtons() {
        binding.apply {
            topPinkPush.enabled()
            bottomPinkPush.enabled()
        }
    }

    private fun enableBlueButtons() {
        binding.apply {
            topBluePush.enabled()
            bottomBluePush.enabled()
        }
    }


    private fun startTimer() {
        var index = 0
        binding.apply {
            timer.visible()
            topBluePush.disabled()
            topPinkPush.disabled()
            bottomBluePush.disabled()
            bottomBluePush.disabled()
        }
        timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {
                binding.timer.text = timerItemList[index++]
            }

            override fun onFinish() {
                binding.apply {
                    timer.gone()
                    if (getMode()) {
                        topBluePush.enabled()
                        topPinkPush.enabled()
                        bottomBluePush.enabled()
                        bottomBluePush.enabled()
                    } else {
                        bottomBluePush.enabled()
                        bottomBluePush.enabled()
                    }

                }
                startGame(getMode())
            }

        }
        timer?.start()
    }

    private fun startGame(mode: Boolean) {
        binding.sumoRed.clearAnimation()
        binding.sumoBlue.clearAnimation()
        jobTatamiHandle()
        jobSumoRotate = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (isActive) {
                delay(500)
                binding.apply {
                    isRotate = !isRotate
                    rotateSumo(isRotate)
                }
            }
        }
        if (mode) {
            gameWithPlayer()
        } else {
            gameWithBot()
        }
    }

    private fun rotateSumo(rotate: Boolean) {
        binding.apply {
            val rotation = if (rotate) 7f else -7f
            sumoRed.animate().rotation(rotation).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()
            sumoBlue.animate().rotation(rotation).setDuration(200)
                .setInterpolator(LinearInterpolator()).start()
        }
    }

    private fun gameWithPlayer() {
        binding.apply {
            topBluePush.setOnVibrationClickListener(getVibration()) {
                redSumoClicked()
            }
            topPinkPush.setOnVibrationClickListener(getVibration()) {
                redSumoClicked()
            }

            bottomPinkPush.setOnVibrationClickListener(getVibration()) {
                blueSumoClicked()
            }

            bottomBluePush.setOnVibrationClickListener(getVibration()) {
                blueSumoClicked()
            }
        }

        jobCheckResult = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (isActive) {
                delay(500)
                if (binding.sumoRed.y <= binding.tatami.y) {
                    bottomStartList[blueScore++].setImageResource(R.drawable.star_selected)
                    jobCheckResult?.cancel()
                    jobBot?.cancel()
                    jobSumoRotate?.cancel()
                    jobTatami?.cancel()
                    ObjectAnimator.ofFloat(binding.sumoRed, "translationY", 0f).start()
                    ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", 0f).start()
                    redY1 = 0f
                    blueY1 = 0f
                    redY2 = 0f
                    blueY2 = 0f
                    if (blueScore >= 3) {
                        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundleOf("result" to true))
                    } else {
                        startTimer()
                    }

                }
                if (binding.sumoBlue.y + binding.sumoBlue.height >= binding.tatami.y + binding.tatami.height) {
                    topStartList[redScore++].setImageResource(R.drawable.star_selected)
                    jobCheckResult?.cancel()
                    jobBot?.cancel()
                    jobSumoRotate?.cancel()
                    jobTatami?.cancel()
                    ObjectAnimator.ofFloat(binding.sumoRed, "translationY", 0f).start()
                    ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", 0f).start()
                    redY1 = 0f
                    blueY1 = 0f
                    redY2 = 0f
                    blueY2 = 0f
                    if (redScore >= 3) {
                        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundleOf("result" to false))
                    } else {
                        startTimer()
                    }

                }
            }
        }
    }

    private fun gameWithBot() {

        jobBot = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (isActive) {
                delay(nextLong(200, 400))
                redSumoClicked()
            }
        }
        binding.bottomPinkPush.setOnVibrationClickListener(getVibration()) {
            blueSumoClicked()
        }

        binding.bottomBluePush.setOnVibrationClickListener(getVibration()) {
            blueSumoClicked()
        }

        jobCheckResult = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (isActive) {
                delay(500)
                if (binding.sumoRed.y <= binding.tatami.y) {
                    bottomStartList[blueScore++].setImageResource(R.drawable.star_selected)
                    jobCheckResult?.cancel()
                    jobBot?.cancel()
                    jobSumoRotate?.cancel()
                    jobTatami?.cancel()
                    ObjectAnimator.ofFloat(binding.sumoRed, "translationY", 0f).start()
                    ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", 0f).start()
                    redY1 = 0f
                    blueY1 = 0f
                    redY2 = 0f
                    blueY2 = 0f
                    if (blueScore >= 3) {
                        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundleOf("result" to true))
                    } else {
                        startTimer()
                    }

                }
                if (binding.sumoBlue.y + binding.sumoBlue.height >= binding.tatami.y + binding.tatami.height) {
                    topStartList[redScore++].setImageResource(R.drawable.star_selected)
                    jobCheckResult?.cancel()
                    jobBot?.cancel()
                    jobSumoRotate?.cancel()
                    jobTatami?.cancel()
                    ObjectAnimator.ofFloat(binding.sumoRed, "translationY", 0f).start()
                    ObjectAnimator.ofFloat(binding.sumoBlue, "translationY", 0f).start()
                    redY1 = 0f
                    blueY1 = 0f
                    redY2 = 0f
                    blueY2 = 0f
                    if (redScore >= 3) {
                        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundleOf("result" to false))
                    } else {
                        startTimer()
                    }

                }
            }
        }


    }


    private fun redSumoStartAnimation(sumoRed: ImageView) {
        val sumoAnimator = ValueAnimator.ofFloat(0f, 110f)
        sumoAnimator.addUpdateListener {
            val va = it.animatedValue as Float
            sumoRed.translationY = va
        }

        sumoAnimator.apply {
            duration = 1200
            interpolator = LinearInterpolator()

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.sumoRed.y =
                        (binding.tatami.height / 2 - binding.sumoRed.height).toFloat()
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {

                }

            })

            start()
        }
    }

    @SuppressLint("Recycle")
    private fun blueSumoStartAnimation(sumoBlue: ImageView) {
        val sumoAnimator = ValueAnimator.ofFloat(0f, -110f)
        sumoAnimator.addUpdateListener {
            val va = it.animatedValue as Float
            sumoBlue.translationY = va
        }

        sumoAnimator.apply {
            duration = 1200
            interpolator = LinearInterpolator()

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.sumoBlue.y = (binding.tatami.height / 2).toFloat()
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {
                }

            })
            start()
        }
    }

    private fun initTimerList() {
        timerItemList = ArrayList<String>().apply {
            add("3")
            add("2")
            add("1")
            add("Go")
        }
    }

    private fun initBottomStarList() {
        bottomStartList = ArrayList<ImageView>().apply {
            binding.apply {
                add(bottomStar1)
                add(bottomStar2)
                add(bottomStar3)
            }
        }
    }

    private fun initTopStarList() {
        topStartList = ArrayList<ImageView>().apply {
            binding.apply {
                add(topStar1)
                add(topStar2)
                add(topStar3)
            }
        }
    }

    private fun calculateDisplayMetrics() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
    }

    private fun View.absX(): Float {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[0].toFloat()
    }

    private fun View.absY(): Float {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[1].toFloat()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        jobCheckResult?.cancel()
        jobBot?.cancel()
        jobSumoRotate?.cancel()
        jobTatami?.cancel()
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobCheckResult?.cancel()
        jobBot?.cancel()
        jobSumoRotate?.cancel()
        jobTatami?.cancel()
        timer?.cancel()
    }
}