package com.linkkader.exoplayer.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.linkkader.exoplayer.Fragment.FragmentEpisode
import com.linkkader.exoplayer.Fragment.FragmentInfo

class ViewPagerAdapter(var fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getPageTitle(position: Int): String {
        return if (position==0) "Info" else "Episodes"
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) FragmentInfo() else FragmentEpisode()
    }

    override fun getCount(): Int {
        return 2
    }

}
