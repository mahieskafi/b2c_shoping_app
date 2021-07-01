package com.srp.eways.ui.navigationdrawer

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.srp.eways.ui.main.MainRootIds
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.util.Utils
import android.text.Spanned
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.view.navigationdraweritem.NavigationDrawerItem
import com.srp.eways.ui.view.navigationdraweritem.NavigationDrawerSubItem
import com.srp.eways.util.Constants
import com.srp.eways.util.CustomTypeFaceSpan


/**
 * Created by ErfanG
 */
public abstract class MainNavigationDrawerFragment : NavigationMemberFragment<NavigationDrawerViewModel>() {

    private lateinit var mViewModel: NavigationDrawerViewModel
    private lateinit var userInfoViewModel: UserInfoViewModel

    private lateinit var mTopInfoBack: LinearLayout
    private lateinit var mHeader: ImageView
    private lateinit var mUserName: TextView
    private lateinit var mUserCodeTitle: TextView
    private lateinit var mUserCode: TextView
    private lateinit var mUserImage: ImageView
    private lateinit var mCredit: NavigationDrawerItem
    private lateinit var mPoints: NavigationDrawerItem
    private lateinit var mIncome: NavigationDrawerItem
    private lateinit var mDeposit: NavigationDrawerItem
    private lateinit var mHome: NavigationDrawerItem
    private lateinit var mStore: NavigationDrawerItem
    private lateinit var mTransactions: NavigationDrawerItem
    private lateinit var mSupport: NavigationDrawerItem
    private lateinit var mAboutUs: NavigationDrawerItem
    private lateinit var mSaleReport: NavigationDrawerItem
    private lateinit var mLogout: NavigationDrawerItem
    //TODO("Other items should add here")

    override fun acquireViewModel(): NavigationDrawerViewModel {
        mViewModel = DIMain.getViewModel(NavigationDrawerViewModel::class.java)

        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation_drawer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTopInfoBack = getView()!!.findViewById(R.id.top_info)
        mHeader = getView()!!.findViewById(R.id.header_back)
        mUserName = getView()!!.findViewById(R.id.text_name)
        mUserCodeTitle = getView()!!.findViewById(R.id.text_usercode_title)
        mUserCode = getView()!!.findViewById(R.id.text_usercode)
        mUserImage = getView()!!.findViewById(R.id.header_image)
        mCredit = getView()!!.findViewById(R.id.credit_item)
        mPoints = getView()!!.findViewById(R.id.points_item)
        mIncome = getView()!!.findViewById(R.id.income_item)
        mDeposit = getView()!!.findViewById(R.id.increase_deposit_item)
        mHome = getView()!!.findViewById(R.id.home_item)
        mStore = getView()!!.findViewById(R.id.store_item)
        mTransactions = getView()!!.findViewById(R.id.transactions_item)
        mSupport = getView()!!.findViewById(R.id.support_item)
        mAboutUs = getView()!!.findViewById(R.id.about_us_item)
        mSaleReport = getView()!!.findViewById(R.id.sale_report_item)
        mLogout = getView()!!.findViewById(R.id.exit_item)

        setAttrs()

        userInfoViewModel = DIMain.getViewModel(UserInfoViewModel::class.java)

        userInfoViewModel.getUserInfoLiveData().observe(this, Observer { t ->
            if (t != null) {
                setUserName(t.fullName)
                setUserCode(t.userId)
                setCredit(t.credit)
                setPoints(t.loyaltyScore)
                setIncome(t.revenue)
            }
        })
    }

    private fun setAttrs() {

        //region ABResources

        val AB = DIMain.getABResources()

        //Header
        mHeader.setBackgroundColor(AB.getColor(R.color.navigation_drawer_top_info_background))
        mHeader.setImageDrawable(AB.getDrawable(R.drawable.navigation_drawer_header))

        //UserName
        mUserName.setTextColor(AB.getColor(R.color.navigation_drawer_header_name_text_color))
        mUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.navigation_drawer_name_size).toFloat())

        //UserCodeTitle
        mUserCodeTitle.setTextColor(AB.getColor(R.color.navigation_drawer_header_code_title_text_color))
        mUserCodeTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.navigation_drawer_code_title_size).toFloat())
        mUserCodeTitle.setText(AB.getString(R.string.navigation_drawer_code_text))

        //UserCode
        mUserCode.setTextColor(AB.getColor(R.color.navigation_drawer_header_code_text_color))
        mUserCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.navigation_drawer_code_size).toFloat())

        //UserImage
        mUserImage.setImageDrawable(AB.getDrawable(R.drawable.navigation_drawer_header_image))


        //TopInfoBackground
        mTopInfoBack.setBackgroundColor(AB.getColor(R.color.navigation_drawer_top_info_background))

        //Credit
        mCredit.setIcon(AB.getDrawable(R.drawable.navigation_drawer_credit_icon), AB.getColor(R.color.navigation_drawer_solid_icon_color))
        mCredit.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color_unclickable))
        mCredit.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mCredit.setTitleText(AB.getString(R.string.navigation_drawer_title_1))

        mCredit.setDescriptionTextColor(AB.getColor(R.color.navigation_drawer_description_color_unclickable))
        mCredit.setDescriptionTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_description_size).toFloat())


        //Points
        mPoints.setIcon(AB.getDrawable(R.drawable.navigation_drawer_points_icon), AB.getColor(R.color.navigation_drawer_solid_icon_color))
        mPoints.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color_unclickable))
        mPoints.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mPoints.setTitleText(AB.getString(R.string.navigation_drawer_title_2))

        mPoints.setDescriptionTextColor(AB.getColor(R.color.navigation_drawer_description_color_unclickable))
        mPoints.setDescriptionTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_description_size).toFloat())
        mPoints.visibility = getPointsVisibility()


        //Income
        mIncome.setIcon(AB.getDrawable(R.drawable.navigation_drawer_income_icon), AB.getColor(R.color.navigation_drawer_solid_icon_color))
        mIncome.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color_unclickable))
        mIncome.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mIncome.setTitleText(AB.getString(R.string.navigation_drawer_title_3))

        mIncome.setDescriptionTextColor(AB.getColor(R.color.navigation_drawer_description_color_unclickable))
        mIncome.setDescriptionTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_description_size).toFloat())
        mIncome.visibility = getIncomeVisibility()

        //Home
        mHome.setIcon(AB.getDrawable(R.drawable.navigation_drawer_home_icon))
        mHome.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mHome.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mHome.setTitleText(AB.getString(R.string.navigation_drawer_home))

        mStore.setIcon(AB.getDrawable(R.drawable.ic_store_drawer))
        mStore.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mStore.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mStore.setTitleText(AB.getString(R.string.navigation_drawer_store))
        mStore.hasSubItems(true)
        var storeItem1 = NavigationDrawerSubItem(context!!)

        storeItem1.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        storeItem1.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_sub_item_title_size).toFloat())
        storeItem1.setTitleText(AB.getString(R.string.navigation_drawer_store_subitem1))
        storeItem1.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_TRANSACTIONS) })

        val subList= arrayListOf<NavigationDrawerSubItem>()
        subList.add(storeItem1)
        mStore.setSubItems(subList)

        //Deposit
        mDeposit.setIcon(AB.getDrawable(R.drawable.navigation_drawer_deposit_icon))
        mDeposit.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mDeposit.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mDeposit.setTitleText(AB.getString(R.string.navigation_drawer_title_4))

        //Transactions
        mTransactions.setIcon(AB.getDrawable(R.drawable.navigation_drawer_transactions_icon))
        mTransactions.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mTransactions.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mTransactions.setTitleText(AB.getString(R.string.navigation_drawer_title_6))

        //SaleReport
        mSaleReport.setIcon(AB.getDrawable(R.drawable.ic_sale_report))
        mSaleReport.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mSaleReport.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mSaleReport.setTitleText(AB.getString(R.string.navigation_drawer_title_10))
        mSaleReport.visibility = getSaleReportVisibility()

        //Support
        mSupport.setIcon(AB.getDrawable(R.drawable.navigation_drawer_support_icon))
        mSupport.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mSupport.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mSupport.setTitleText(AB.getString(R.string.navigation_drawer_title_7))
        mSupport.visibility = getSupportVisibility()

        //AboutUs
        mAboutUs.setIcon(AB.getDrawable(R.drawable.navigation_drawer_about_us_icon))
        mAboutUs.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mAboutUs.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mAboutUs.setTitleText(AB.getString(R.string.navigation_drawer_title_8))
        mAboutUs.visibility = getAboutUsVisibility()

        //Logout
        mLogout.setIcon(AB.getDrawable(R.drawable.navigation_drawer_logout_icon))
        mLogout.setTitleTextColor(AB.getColor(R.color.navigation_drawer_title_color))
        mLogout.setTitleTextSize(AB.getDimenPixelSize(R.dimen.navigation_drawer_title_size).toFloat())
        mLogout.setTitleText(AB.getString(R.string.navigation_drawer_title_9))

        //endregion

        //region ClickListeners

        mHome.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_HOME) })
        mDeposit.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_DEPOSIT) })
        mTransactions.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_TRANSACTIONS) })
        mSaleReport.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_SALE_REPORT) })
        mSupport.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_SUPPORT) })
        mAboutUs.setAction(View.OnClickListener { switchRoot(MainRootIds.ROOT_ABOUT_US) })
        mLogout.setAction(View.OnClickListener {
            logoutAction();
        })

        //endregion
    }

    private fun switchRoot(rootId: Int) {
        onSwitchRoot(rootId)
        toggleDrawer()
    }

    private fun setRialStyle(text: String, fontSize: Int, typeFace: Int): CharSequence {
        val AB = DIMain.getABResources()

        val font = context?.let { ResourcesCompat.getFont(it, typeFace) }
        val textSize = AB.getDimenPixelSize(fontSize)

        val text = SpannableStringBuilder(text)
        text.setSpan(CustomTypeFaceSpan("", font), text.length - 4, text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        text.setSpan(AbsoluteSizeSpan(textSize), text.length - 4, text.length, 0)

        return text
    }

    private fun setCredit(credit: Long?) {

        if (credit != null) {
            val newValue = Utils.toPersianNumber(String.format("%,d", credit)) + " " + resources.getString(R.string.rial).trim()
            mCredit.setDescriptionText(setRialStyle(newValue, R.dimen.navigation_drawer_rial_text_size, R.font.iran_yekan))
        }
    }

    private fun setPoints(points: Long?) {
        if (points != null) {
            val newValue = Utils.toPersianNumber(String.format("%,d", points))
            mPoints.setDescriptionText(newValue)
        }
    }

    private fun setIncome(income: Long?) {
        if (income != null) {
            val newValue = Utils.toPersianNumber(String.format("%,d", income)) + " " + resources.getString(R.string.rial).trim()
            mIncome.setDescriptionText(setRialStyle(newValue, R.dimen.navigation_drawer_rial_text_size, R.font.iran_yekan))
        }
    }

    private fun setUserCode(userCode: Long?) {
        if (userCode != null) {
            mUserCode.setText(Utils.toPersianNumber(userCode.toString()))
        }
    }

    private fun setUserName(userName: String?) {
        if (userName != null) {
            mUserName.setText(Utils.toPersianNumber(userName.trim()))
        }
    }

    override fun getDataFromServer() {
        userInfoViewModel.getCredit()
    }

    public abstract fun getIncomeVisibility(): Int;
    public abstract fun getSaleReportVisibility(): Int;
    public abstract fun getSupportVisibility(): Int;
    public abstract fun getAboutUsVisibility(): Int;
    public abstract fun getPointsVisibility(): Int;

    public abstract fun logoutAction();

    public fun getUserInfoViewModel(): UserInfoViewModel {
        return userInfoViewModel
    }
}
