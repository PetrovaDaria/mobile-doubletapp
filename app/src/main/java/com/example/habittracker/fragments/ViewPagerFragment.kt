package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.habittracker.Habit
import com.example.habittracker.R
import com.example.habittracker.enums.Type
import com.google.android.material.tabs.TabLayout

class ViewPagerFragment: Fragment() {
    val habits = mutableListOf<Habit>()
    val goodListFragment = ListFragment() // ListFragment.newInstance(Type.Good, habits)
    val badListFragment = ListFragment() // ListFragment.newInstance(Type.Bad, habits)

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MyPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_pager_fragment, container, false)

        viewPager = view.findViewById(R.id.view_pager)
        pagerAdapter = MyPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        var habit: Habit? = null
        arguments?.let {
            habit = it.getParcelable("habit")
        }
        if (habit != null) {
            if (habit!!.Type == Type.Good) {
                goodListFragment.arguments = arguments
            }
            if (habit!!.Type == Type.Bad) {
                badListFragment.arguments = arguments
            }
        }

        return view
    }

    inner class MyPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> goodListFragment
                else -> badListFragment
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Хорошие"
                else -> "Плохие"
            }
        }
    }
}