package com.sumo.fortwo2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sumo.fortwo2.R
import com.sumo.fortwo2.common.getMode
import com.sumo.fortwo2.common.getVibration
import com.sumo.fortwo2.common.setOnVibrationClickListener
import com.sumo.fortwo2.common.viewBinding
import com.sumo.fortwo2.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding { FragmentResultBinding.bind(it) }

    var result = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            result = it.getBoolean("result")
        }

        binding.apply {
            btnBackResult.setOnVibrationClickListener(getVibration()) {
                findNavController().navigate(R.id.action_resultFragment_to_menuFragment)
            }

            binding.tvGameResult.text = if (getMode()) {
                if (result) {
                    resources.getString(R.string.player_1_wins)
                } else {
                    resources.getString(R.string.player_2_wins)
                }
            } else {
                if (result) {
                    resources.getString(R.string.player_wins)
                } else {
                    resources.getString(R.string.bot_wins)
                }
            }
        }
    }

}