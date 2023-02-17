package com.sumo.fortwo2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sumo.fortwo2.R
import com.sumo.fortwo2.common.getVibration
import com.sumo.fortwo2.common.setOnVibrationClickListener
import com.sumo.fortwo2.common.viewBinding
import com.sumo.fortwo2.databinding.FragmentMenuBinding


class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding { FragmentMenuBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSettings.setOnVibrationClickListener(getVibration()){
                findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
            }

            btnMode.setOnVibrationClickListener(getVibration()){
                findNavController().navigate(R.id.action_menuFragment_to_modeFragment)
            }

            btnGame.setOnVibrationClickListener(getVibration()){
                findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
            }
        }

    }

}