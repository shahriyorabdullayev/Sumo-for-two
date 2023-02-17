package com.sumo.fortwo2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sumo.fortwo2.R
import com.sumo.fortwo2.common.*
import com.sumo.fortwo2.databinding.FragmentModeBinding


class ModeFragment : Fragment(R.layout.fragment_mode) {

    private val binding by viewBinding { FragmentModeBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            if (getMode()) {
                shadowOne.gone()
                shadowTwo.visible()
            } else {
                shadowOne.visible()
                shadowTwo.gone()
            }
            btnBackMode.setOnVibrationClickListener(getVibration()) {
                requireActivity().onBackPressed()
            }

            btnOnePlayer.setOnVibrationClickListener(getVibration()) {
                saveMode(false)
                shadowOne.visible()
                shadowTwo.gone()
            }

            btnTwoPlayer.setOnVibrationClickListener(getVibration()) {
                saveMode(true)
                shadowTwo.visible()
                shadowOne.gone()
            }


        }
    }

}