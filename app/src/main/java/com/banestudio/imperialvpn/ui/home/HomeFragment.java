package com.banestudio.imperialvpn.ui.home;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.banestudio.imperialvpn.R;
import com.banestudio.imperialvpn.databinding.FragmentHomeBinding;
import com.banestudio.imperialvpn.databinding.UpperButtonBinding;
import com.banestudio.imperialvpn.ui.home.buttonvpn.ButtonVpnView;
import com.banestudio.imperialvpn.ui.home.buttonvpn.ProgressView;
import com.banestudio.imperialvpn.ui.home.buttonvpn.TextNotificationView;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private ButtonVpnView buttonVpnView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonVpnView = new ButtonVpnView(
            binding.buttonVpn,
            new ProgressView(binding.buttonVpn.circleloadingsize),
            new TextNotificationView(binding.textNotification),
            binding.dsCountryPicker,
            requireContext(),
            requireActivity(),
            this
        );

        initialization();
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialization() {
        setUpperButton(binding.upperUploadButton, R.drawable.icon_upload_circle, getString(R.string.upload));
        setUpperButton(binding.upperDownloadButton, R.drawable.icon_download_circle, getString(R.string.download));
    }

    private void setUpperButton(UpperButtonBinding upperButtonBinding, int image, String title) {
        upperButtonBinding.iconUpperButton.setImageResource(image);
        upperButtonBinding.titleUpperButton.setText(title);
    }

    private void setSpeedUpperButton(UpperButtonBinding upperButtonBinding, int value) {

        upperButtonBinding.speedUpperButton.setText(value + getString(R.string.speed_kb_current));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            buttonVpnView.startVPN();
        }
    }

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            buttonVpnView.updateProgressVPNConnection();
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

}