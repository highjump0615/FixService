package com.brainyapps.ezfix.activities.admin

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_admin_user.*

class AdminUserActivity : BaseActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    var aryFragment = ArrayList<AdminUserFragment>()

    companion object {
        val USER_ALL = 0
        val USER_BANNED = 1
    }

    private var currentTab = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user)

        setNavbar("USERS", true)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // init tab views
        this.aryFragment.add(AdminUserFragment.newInstance(USER_ALL))
        this.aryFragment.add(AdminUserFragment.newInstance(USER_BANNED))

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(PageChangeListener())
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    override fun onResume() {
        super.onResume()

        initUserList(true)
    }

    /**
     * get user list in fragment
     *
     * @param reload force reload user data
     */
    fun initUserList(reload: Boolean = false) {
        if (this.currentTab < 0) {
            return
        }

        // update list
        val fragment = this@AdminUserActivity.mSectionsPagerAdapter!!.getItem(this.currentTab) as AdminUserFragment
        if (reload || !fragment.isInitialized) {
            fragment.getUsers(false, true)
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return this@AdminUserActivity.aryFragment[position]
        }

        override fun getCount(): Int {
            return 2
        }
    }

    inner class PageChangeListener: TabLayout.TabLayoutOnPageChangeListener(tabs) {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            if (this@AdminUserActivity.currentTab != position) {
                this@AdminUserActivity.currentTab = position

                initUserList()
            }
        }
    }
}
