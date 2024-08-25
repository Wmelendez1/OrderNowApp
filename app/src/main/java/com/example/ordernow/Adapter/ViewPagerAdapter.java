package com.example.ordernow.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ordernow.Fragments.BillingSettingTabFragment;
import com.example.ordernow.Fragments.OverViewSettingsTabFragment;
import com.example.ordernow.Fragments.PaymentDetailsSettingsTabFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0: return new OverViewSettingsTabFragment();
            case 1: return new BillingSettingTabFragment();
            case 2: return new PaymentDetailsSettingsTabFragment();
            default: return new OverViewSettingsTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
