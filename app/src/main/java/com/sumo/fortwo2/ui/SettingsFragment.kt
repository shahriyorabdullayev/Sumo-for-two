package com.sumo.fortwo2.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sumo.fortwo2.R
import com.sumo.fortwo2.common.*
import com.sumo.fortwo2.common.Constants.EN
import com.sumo.fortwo2.common.Constants.RU
import com.sumo.fortwo2.databinding.FragmentSettingsBinding
import com.sumo.fortwo2.service.SoundService
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.Reword
import java.util.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding { FragmentSettingsBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sound(getSound())
        vibration(getVibration())
        locale(getLocale())


        with(binding) {

            btnBackSettings.setOnVibrationClickListener(getVibration()) {
                requireActivity().onBackPressed()
            }

            btnSound.setOnVibrationClickListener(getVibration()) {
                if (!getSound()) {
                    saveSound(true)
                    sound(true)
                    requireActivity().startService(Intent(requireContext(),
                        SoundService::class.java))
                } else {
                    saveSound(false)
                    sound(false)
                    requireActivity().stopService(Intent(requireContext(),
                        SoundService::class.java))
                }
            }

            btnVibration.setOnVibrationClickListener(getVibration()) {
                if (!getVibration()) {
                    vibration(true)
                    saveVibration(true)
                } else {
                    saveVibration(false)
                    vibration(false)
                }
            }

            btnEn.setOnVibrationClickListener(getVibration()) {
                if (getLocale() == EN) {
                    saveLocale(RU)
                    locale(RU)
                    AppLocale.desiredLocale = Locale(RU)
                    Reword.reword(root)
                } else {
                    saveLocale(EN)
                    locale(EN)
                    AppLocale.desiredLocale = Locale.ENGLISH
                    Reword.reword(root)
                }
            }
        }
    }

    private fun sound(isSound: Boolean) {
        val soundBtn = if (isSound) R.drawable.btn_selected else R.drawable.btn_unselected
        binding.btnSound.setImageResource(soundBtn)
    }

    private fun vibration(isVibration: Boolean) {
        val vibrationBtn = if (isVibration) R.drawable.btn_selected else R.drawable.btn_unselected
        binding.btnVibration.setImageResource(vibrationBtn)
    }

    private fun locale(locale: String) {
        val localeBtn = if (locale == EN) R.drawable.btn_en else R.drawable.btn_ru
        binding.btnEn.setImageResource(localeBtn)
    }
}