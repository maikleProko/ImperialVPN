package com.banestudio.imperialvpn.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.banestudio.imperialvpn.R
import com.banestudio.imperialvpn.databinding.FragmentHomeBinding
import com.banestudio.imperialvpn.databinding.UpperButtonBinding
import com.banestudio.imperialvpn.ui.home.buttonvpn.ButtonVpnView
import com.banestudio.imperialvpn.ui.home.buttonvpn.ProgressView
import com.banestudio.imperialvpn.ui.home.buttonvpn.TextNotificationView

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var buttonVpnView: ButtonVpnView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        buttonVpnView = ButtonVpnView(
            binding!!.buttonVpn,
            ProgressView(binding!!.buttonVpn.circleloadingsize),
            TextNotificationView(binding!!.textNotification),  //binding.dsCountryPicker,
            requireContext(),
            requireActivity(),
            binding!!.ccpPhone,
            this
        )
        initialization()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initialization() {
        setUpperButton(
            binding!!.upperUploadButton,
            R.drawable.icon_upload_circle,
            getString(R.string.upload)
        )
        setUpperButton(
            binding!!.upperDownloadButton,
            R.drawable.icon_download_circle,
            getString(R.string.download)
        )
    }

    private fun setUpperButton(upperButtonBinding: UpperButtonBinding, image: Int, title: String) {
        upperButtonBinding.iconUpperButton.setImageResource(image)
        upperButtonBinding.titleUpperButton.text = title
    }

    private fun setSpeedUpperButton(upperButtonBinding: UpperButtonBinding, value: Int) {
        upperButtonBinding.speedUpperButton.text =
            value.toString() + getString(R.string.speed_kb_current)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            buttonVpnView!!.startVPN()
        }
    }

    private val mHandler = Handler()
    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            buttonVpnView!!.updateProgressVPNConnection()
            mHandler.postDelayed(this, 1000)
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(mRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }
}