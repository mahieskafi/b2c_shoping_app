package com.srp.ewayspanel.ui.landing

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.Transformers.BaseTransformer
import com.squareup.picasso.Picasso
import com.srp.eways.di.DIMain
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.model.phonebook.UserPhoneBook
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.banner.BannerRepository
import com.srp.eways.ui.banner.BannerListView
import com.srp.eways.ui.charge.ChargeViewModel
import com.srp.eways.ui.charge.model.IOperator
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.main.MainRootIds
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.phonebook.ewayscontact.EwaysContactActivity
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.EwaysContactFragment
import com.srp.eways.ui.phonebook.mobilecontact.MobilePhoneBookActivity
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView.PhoneAndOperatorsViewListener
import com.srp.eways.ui.view.slider.SliderItemView
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.Constants
import com.srp.eways.util.Utils
import com.srp.ewayspanel.BuildConfig
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.appservice.AppService
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest
import com.srp.ewayspanel.ui.home.HomeFragment
import com.srp.ewayspanel.ui.home.HomeRootIds.*
import com.srp.ewayspanel.ui.main.GetBannerLink
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.store.search.SearchFragment
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_BILL_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_BILL_INQUIRY_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_CHARGE_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_CLUB_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_MOBILE_LIST_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.SERVICE_STORE_CODE
import com.srp.ewayspanel.ui.view.landing.LandingUtil.Companion.STORE_MOBILE_LIST_TAB_INDEX
import com.srp.ewayspanel.ui.view.shimmer.ErrorView
import com.srp.ewayspanel.ui.view.shimmer.Shimmer
import com.srp.ewayspanel.ui.view.shimmer.ShimmerFrameLayout
import java.util.*

class LandingFragment : NavigationMemberFragment<LandingViewModel>(), LandingServiceAdapter.AppServiceItemClickListener {

    private lateinit var mServiceListTitle: AppCompatTextView
    private lateinit var mServiceRecycler: RecyclerView
    private lateinit var mServicesAdapter: LandingServiceAdapter
    private lateinit var mBanner: BannerListView
    private lateinit var mPhoneNumberInputTitle: AppCompatTextView
    private lateinit var mPhoneNumberInput: PhoneAndOperatorsView
    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mMainLayout: LinearLayout

    private lateinit var mShimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mShimmerServicesView: LinearLayout
    private lateinit var mShimmerErrorView: ErrorView

    private lateinit var mChargeErrorView: RelativeLayout
    private lateinit var mServiceListContainer: FrameLayout
    private lateinit var mBannerContainer: CardView

    private lateinit var mUserInfoViewModel: UserInfoViewModel
    private lateinit var mChargeViewModel: ChargeViewModel

    private lateinit var mBanerUrl: GetBannerLink

    private var mDefaultPhoneNumber: String = ""
    private var mHasNoChargeDataError: Boolean = false
    private var mDownloadingUpdate: Boolean = false

    private lateinit var checkDataCompletedObserver: Observer<Boolean>

    companion object {
        private val REQUEST_CODE_SELECT_CONTACT = 1000
        private val REQUEST_CODE_SELECT_MOBILE_CONTACT = 2000

        private val STATUS_APP_VERSION_NO_UPDATE_AVAILABLE = 0
        private val STATUS_APP_VERSION_UPDATE_AVAILABLE = 1
        private val STATUS_APP_VERSION_FORCE_UPDATE = 2
        private const val OLD_VERSION_KEY = "old_version_key"

        @JvmStatic
        fun newInstance(): LandingFragment = LandingFragment()
    }

    override fun acquireViewModel(): LandingViewModel {
        return DI.getViewModel(LandingViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_landing
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mBanerUrl = activity as GetBannerLink
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener")
        }
    }

    private val mAppVersionChangesObserver = Observer<AppVersionChangesResponse> {
        if (it != null) {
            if (it.status == NetworkResponseCodes.SUCCESS && it.data!!.isNotEmpty()) {
                val dialog = RecentlyChangesDialog(context!!)
                dialog.setDescription(it.data!!)
                DI.getPreferenceCache().put(OLD_VERSION_KEY, 0)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBanner = view.findViewById(R.id.banner)
        mServiceListTitle = view.findViewById(R.id.list_title)
        mServiceRecycler = view.findViewById(R.id.recyclerview)
        mPhoneNumberInputTitle = view.findViewById(R.id.mobile_input_title)
        mPhoneNumberInput = view.findViewById(R.id.input_phonenumber)
        mToolbar = view.findViewById(R.id.toolbar)
        mMainLayout = view.findViewById(R.id.landingLayout)

        mShimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        mShimmerServicesView = view.findViewById(R.id.shimmer_services)
        mShimmerErrorView = view.findViewById(R.id.error_view)

        mChargeErrorView = view.findViewById(R.id.charge_error_view)

        mServiceListContainer = view.findViewById(R.id.serviceListContainer)
        mBannerContainer = view.findViewById(R.id.bannerContainer)

        val abResources = DI.getABResources()

        val cache = DI.getPreferenceCache()

        setRecyclerMargins()
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel::class.java)
        mChargeViewModel = DIMain.getViewModel(ChargeViewModel::class.java)

        mMainLayout.visibility = View.GONE
        mMainLayout.tag = mMainLayout.visibility

        mShimmerFrameLayout.visibility = View.VISIBLE

        val shimmer = Shimmer.AlphaHighlightBuilder()
        shimmer.setDuration(1000)
        shimmer.setShape(Shimmer.Shape.LINEAR)
        shimmer.setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
        shimmer.setTilt(180f)
        shimmer.setDropoff(2f)
        shimmer.setRepeatMode(ValueAnimator.REVERSE)
        mShimmerFrameLayout.shimmer = shimmer.build()

        viewModel.setLastSplashUpdateTime()
        viewModel.checkIsDataCompleted()
        checkDataCompletedObserver = Observer<Boolean> {
            if (it != null) {
                if (it) {
                    mShimmerFrameLayout.stopShimmer()
                    mShimmerFrameLayout.visibility = View.GONE
                    mMainLayout.visibility = View.VISIBLE

//                    Toast.makeText(context,DIMain.getPreferenceCache().getInt(Constants.LAST_VERSION_CODE).toString(),Toast.LENGTH_LONG).show()
                    if (DIMain.getPreferenceCache().getInt(Constants.LAST_VERSION_CODE) < BuildConfig.VERSION_CODE) {
                        //TODO show changes dialog
                        DIMain.getPreferenceCache().put(Constants.LAST_VERSION_CODE, BuildConfig.VERSION_CODE)
                    }
                } else {
                    Handler().postDelayed({
                        viewModel.checkIsDataCompleted()
                    }, 1000)
                }
            }
        }
        viewModel.getIsDataCompleted().observe(viewLifecycleOwner, checkDataCompletedObserver)

        mMainLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                var newVisibility = mMainLayout.visibility

                if (mMainLayout.tag != newVisibility) {
                    animateIn()

                    mMainLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
        setupToolbar()

        mServiceRecycler.layoutManager = ViewUtils.RtlGridLayoutManager(view.context, abResources.getInt(R.integer.landing_service_list_column_count))

        mServiceRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    if ((mServiceRecycler.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition() == mServicesAdapter.itemCount - 1) {
                        val layoutParams = mServiceListContainer.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_sides),
                                abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_top),
                                abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_sides),
                                abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_top))

                        mServiceListContainer.layoutParams = layoutParams
                    }
                } else {
                    var layoutParams = mServiceListContainer.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_sides),
                            abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_top),
                            abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_sides),
                            abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_bottom))

                    mServiceListContainer.layoutParams = layoutParams
                }

            }
        })

        viewModel.getAppServiceListLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.data != null) {
                    if (it.status == NetworkResponseCodes.SUCCESS && it.data!!.isNotEmpty()) {
                        viewModel.setIsServiceListReceived(true)

                        var serviceList = mutableListOf<AppService>()
                        for (appService in it.data!!) {
                            if (appService.isVisible) {
                                serviceList.add(appService)
                            }
                        }

                        mServicesAdapter = LandingServiceAdapter(view.context, serviceList, LandingFragment@ this)
                        mServiceRecycler.adapter = mServicesAdapter

                        val oldVersion = cache.getInt(OLD_VERSION_KEY)
                        if (oldVersion != 0 && oldVersion != BuildConfig.VERSION_CODE) {
                            viewModel.getAppVersionChanges(oldVersion)
                            viewModel.getAppVersionChangesLiveData().observe(viewLifecycleOwner, mAppVersionChangesObserver)
                        }

                    } else {
                        showShimmerError(it.description)
                    }
                } else {
                    var description = it.description
                    if (description.isEmpty()) {
                        description = abResources.getString(R.string.network_error_connect_to_server)
                    }

                    showShimmerError(description)

                }
                viewModel.consumeAppServiceListLiveData()
            }
        })

        mBanner.setCustomIndicator(view.findViewById(R.id.indicator) as PagerIndicator)
        viewModel.getBannerFromServer(BannerRepository.BANNER_TYPE_MAIN_PAGE)

        viewModel.getBannerLive().observe(viewLifecycleOwner, Observer<BannerResponse> {

            if (it != null) {
                viewModel.setIsBannerListReceived(true)
            } else {
                viewModel.setIsBannerListReceived(false)
            }
            mBanner.removeAllSliders()

            if (it.bannerList.size > 0) {

                for (bannerItem in it.bannerList) {

                    val textSliderView = SliderItemView(context)

                    textSliderView.picasso = Picasso.get()
                    textSliderView
                            .image(bannerItem.url).scaleType = BaseSliderView.ScaleType.Fit

                    textSliderView.image(bannerItem.url).setOnSliderClickListener {
                        mBanerUrl.getDeepUrl(bannerItem.target)
                    }

                    mBanner.addSlider(textSliderView)

                    if (it.bannerList.size == 1) {
                        mBanner.stopAutoCycle()
                        mBanner.indicatorVisibility = PagerIndicator.IndicatorVisibility.Invisible
                        mBanner.setPagerTransformer(false, object : BaseTransformer() {
                            override fun onTransform(view: View?, position: Float) {}
                        })
                    }
                }
            } else {
                val textSliderView = SliderItemView(context)
                textSliderView.picasso = Picasso.get()

                textSliderView
                        .image(R.mipmap.default_bnner)
                        .setScaleType(BaseSliderView.ScaleType.Fit)

                mBanner.addSlider(textSliderView)
                mBanner.stopAutoCycle()
                mBanner.indicatorVisibility = PagerIndicator.IndicatorVisibility.Invisible
                mBanner.setPagerTransformer(false, object : BaseTransformer() {
                    override fun onTransform(view: View?, position: Float) {}
                })
            }
        })

        mChargeViewModel.chargeDataLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (mHasNoChargeDataError) {

                    mHasNoChargeDataError = false
                    if (mChargeErrorView.visibility == View.VISIBLE) {
                        mPhoneNumberInput.enableEditText()
                        mChargeErrorView.visibility = View.GONE
                        mPhoneNumberInputTitle.setTextColor(abResources.getColor(R.color.landing_service_list_title_text_color))
                    }

                    if (mDefaultPhoneNumber.isNotEmpty()) {
                        openChargeFragmentWithDefaultNumber(mDefaultPhoneNumber)
                    }
                }

                viewModel.setChargeData(it)

                viewModel.setIsChargeDataReceived(true)
            } else {
                if (mHasNoChargeDataError) {
                    Utils.hideKeyboard(activity)
                    mPhoneNumberInput.disableEditText(abResources.getDrawable(com.srp.eways.R.drawable.phone_and_operator_input_disable_background))
                    mPhoneNumberInputTitle.setTextColor(abResources.getColor(R.color.landing_service_item_title_inactive_text_color))
                    mChargeErrorView.visibility = View.VISIBLE
                    mChargeViewModel.loadChargeData()
                }
            }
        })

        mChargeViewModel.topInquiries.observe(viewLifecycleOwner, Observer {
            if (it != null && it.items != null) {
                viewModel.setTopInquiresResult(it)

                viewModel.setIsTopInquiriesReceived(true)
            }
        })

        mPhoneNumberInputTitle.typeface = ResourcesCompat.getFont(view.context, R.font.iran_yekan_medium)
        mPhoneNumberInputTitle.setTextColor(abResources.getColor(R.color.landing_service_list_title_text_color))
        mPhoneNumberInputTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.landing_service_list_title_text_size))
        mPhoneNumberInputTitle.text = abResources.getString(R.string.landing_service_phonenumber_title_text)

        mPhoneNumberInput.setListener(object : PhoneAndOperatorsViewListener {
            override fun onUserPhoneBookClicked() {
                startActivityForResult(EwaysContactActivity.getIntent(context), REQUEST_CODE_SELECT_CONTACT)
            }

            override fun onEditTextGotFocus() {
                if (mChargeViewModel.chargeDataLive.value == null) {
                    noChargeDataError()
                }
            }

            override fun onMobilePhoneBookClicked() {
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_CONTACTS), Constants.READ_CONTACT_PERMISSION)
                } else {
                    startActivityForResult(MobilePhoneBookActivity.getIntent(context), REQUEST_CODE_SELECT_MOBILE_CONTACT)
                }
            }

            override fun onOperatorLoadAnimationEnded() {
            }

            override fun onRemovePhoneNumberClicked() {
            }

            override fun onPhoneNumberChanged(phoneNumber: String?) {
                if (mChargeViewModel.chargeDataLive.value == null) {
                    if (phoneNumber!!.length == 11) {
                        mDefaultPhoneNumber = phoneNumber
                    }

                    noChargeDataError()
                    return
                }

                openChargeFragmentWithDefaultNumber(phoneNumber)
            }

            override fun onOperatorSelected(operator: IOperator?) {
            }

        })


        mServiceListTitle.typeface = ResourcesCompat.getFont(view.context, R.font.iran_yekan_medium)
        mServiceListTitle.setTextColor(abResources.getColor(R.color.landing_service_list_title_text_color))
        mServiceListTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.landing_service_list_title_text_size))
        mServiceListTitle.text = abResources.getString(R.string.landing_service_list_title_text)

        mToolbar.setShopIconAction { v ->
            v.isClickable = false
            Handler().postDelayed({ v.isClickable = true }, 400)
            if (!DI.getViewModel(ShopCartViewModel::class.java).hasAnythingToChange()) {
                openFragment(ShopCartFragment.newInstance(),
                        NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
            } else {
                Toast.makeText(context, DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show()
            }
        }

        mToolbar.setDepositActionClick { v ->
            onSwitchRoot(MainRootIds.ROOT_DEPOSIT)
        }

        DI.getViewModel(ShopCartViewModel::class.java).getShopCartProductList().observe(viewLifecycleOwner, object : Observer<ArrayList<ShopCartItemModel>> {
            override fun onChanged(shopCartItemModels: ArrayList<ShopCartItemModel>) {
                mToolbar.setProductCount(shopCartItemModels.size)
            }
        })

        viewModel.getAppVersionLiveData().observe(viewLifecycleOwner, Observer<AppVersionResponse> {
            if (it != null) {
                if (it.status == NetworkResponseCodes.SUCCESS && it.data != null
                        && it.data?.url != null && it.data?.url!!.isNotEmpty()
                ) {
                    var status = it.data?.status

                    when (status) {
                        STATUS_APP_VERSION_NO_UPDATE_AVAILABLE -> {
                            getData()
                        }
                        STATUS_APP_VERSION_UPDATE_AVAILABLE -> {
                            showUpdateAvailable(false, it.data!!.url)
                        }
                        STATUS_APP_VERSION_FORCE_UPDATE -> {
                            showUpdateAvailable(true, it.data!!.url)
                        }
                    }
                } else {
                    getData()
                }
            }
        })

        if (!Utils.isInternetAvailable()) {
            showShimmerError(abResources.getString(R.string.network_error_no_internet), false)
            return
        } else {
            viewModel.getLastVersion()
        }
    }

    private fun getData() {
        resetAppServiceList()
        viewModel.getAppServiceList()
        viewModel.getBannerFromServer(BannerRepository.BANNER_TYPE_MAIN_PAGE)

        mChargeViewModel.loadChargeData()
        mChargeViewModel.loadTopInquiries()
    }

    private fun showShimmerError(errorDescription: String, isShowRetry: Boolean = true) {
        mShimmerServicesView.visibility = View.GONE
        mShimmerErrorView.visibility = View.VISIBLE
        mShimmerFrameLayout.stopShimmer()
        mShimmerFrameLayout.hideShimmer()

        mShimmerErrorView.setDescription(errorDescription)

        viewModel.consumeIsDataCompleted()
        viewModel.getIsDataCompleted().removeObserver(checkDataCompletedObserver)

        if (isShowRetry) {
            mShimmerErrorView.setMainButtonTitle(DIMain.getABResources().getString(R.string.loadingstateview_retry_text))
            mShimmerErrorView.setMainButtonClickListener(View.OnClickListener {
                resetAppServiceList()
                viewModel.getAppServiceList()
            })
        }
    }

    private fun showUpdateAvailable(isForce: Boolean, url: String) {
        mShimmerServicesView.visibility = View.GONE
        mShimmerErrorView.visibility = View.VISIBLE
        mShimmerFrameLayout.stopShimmer()
        mShimmerFrameLayout.hideShimmer()

        viewModel.consumeIsDataCompleted()
        viewModel.getIsDataCompleted().removeObserver(checkDataCompletedObserver)

        var abResources = DIMain.getABResources()

        mShimmerErrorView.setTitle(abResources.getString(R.string.landing_update_service_title))
        mShimmerErrorView.setMainButtonTitle(abResources.getString(R.string.landing_update_available_main_button_text))
        mShimmerErrorView.setMainButtonClickListener(View.OnClickListener {
            mDownloadingUpdate = true
            val updateFragment = UpdateFragment.newInstance()
            updateFragment.setUpdateUrl(url)
            openFragment(updateFragment)
        })

        if (isForce) {
            mShimmerErrorView.setDescription(abResources.getString(R.string.landing_update_service_force_update_description))
            mShimmerErrorView.setSecondButtonTitle(abResources.getString(R.string.landing_update_force_second_button_text))
            mShimmerErrorView.setSecondButtonClickListener(View.OnClickListener {
                activity?.finish()
            })
        } else {
            mShimmerErrorView.setDescription(abResources.getString(R.string.landing_update_service_update_available_description))
            mShimmerErrorView.setSecondButtonTitle(abResources.getString(R.string.landing_update_available_second_button_text))
            mShimmerErrorView.setSecondButtonClickListener(View.OnClickListener {
                resetAppServiceList()
                viewModel.getAppServiceList()
            })
        }


    }

    override fun onAppServiceClicked(appService: AppService) {
        val homeFragment = HomeFragment.newInstance(getFragmentRootId(appService.id))
        if (appService.id == SERVICE_BILL_INQUIRY_CODE) {
            homeFragment.setSubTabIndex(1)
        }
        if (appService.id == SERVICE_MOBILE_LIST_CODE) {
            homeFragment.setSubTabIndex(STORE_MOBILE_LIST_TAB_INDEX)
        }

        setChargeViewModelData()

        animateOut(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {

            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        openFragment(homeFragment)
    }

    private fun getFragmentRootId(serviceId: Int): Int {
        return when (serviceId) {
            SERVICE_CHARGE_CODE -> ROOT_CHARGE
            SERVICE_BILL_CODE -> ROOT_BILL
            SERVICE_BILL_INQUIRY_CODE -> ROOT_BILL
            SERVICE_CLUB_CODE -> ROOT_CLUB
            SERVICE_STORE_CODE -> ROOT_STORE
            SERVICE_MOBILE_LIST_CODE -> ROOT_STORE
            else -> ROOT_CHARGE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SELECT_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                val userPhoneBook: UserPhoneBook = data!!.getParcelableExtra(EwaysContactFragment.EXTRA_USERPHONEBOOK)!!
                mPhoneNumberInput.setPhoneNumber(userPhoneBook.cellPhone)
            }
        } else if (requestCode == REQUEST_CODE_SELECT_MOBILE_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                val userPhoneBook: UserPhoneBook = data!!.getParcelableExtra(MobilePhoneBookActivity.EXTRA_MOBILERPHONEBOOK)!!
                mPhoneNumberInput.setPhoneNumber(userPhoneBook.cellPhone)
            }
        }
    }

    private fun setupToolbar() {
        val resources = DI.getABResources()
        mToolbar.setTitle(resources.getString(R.string.store_page_category_title))
        mToolbar.setShowTitle(false)
        mToolbar.setShowDeposit(true)
        mToolbar.setShowShop(true)
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background))
        mToolbar.setBackgroundToolbarColor(resources.getColor(R.color.store_page_toolbar_background))
        mToolbar.setOnSearchClickListener { text ->
            val filterProductRequest = FilterProductRequest()
            filterProductRequest.text = text
            openFragment(SearchFragment.newInstance(filterProductRequest))
        }

        mToolbar.setOnNavigationDrawerClickListener {
            mPhoneNumberInput.setErrorVisible(false)
            toggleDrawer()
        }

        mUserInfoViewModel.getCreditLiveData().observe(viewLifecycleOwner, Observer<Long> {
            if (it != null) {
                mToolbar.setDeposit(it)
            }
        })

        val shopCartViewModel = DI.getViewModel(ShopCartViewModel::class.java)
        if (shopCartViewModel != null) {
            shopCartViewModel.getShopCartProductList().observe(viewLifecycleOwner, Observer<ArrayList<ShopCartItemModel>> { shopCartItemModels ->
                mToolbar.setProductCount(shopCartItemModels.size)
            })

            shopCartViewModel.getBasketLoading().observe(viewLifecycleOwner, Observer<Boolean?> { isLoading ->
                if (isLoading!!) {
                    mToolbar.showProgress()
                } else {
                    mToolbar.stopProgress()
                }
            })
        }
    }

    override fun onPause() {
        if (mShimmerFrameLayout.isShimmerVisible) {
            mShimmerFrameLayout.stopShimmer()
        }
        super.onPause()
    }

    private fun setChargeViewModelData() {
        mChargeViewModel = DIMain.getViewModel(ChargeViewModel::class.java)
        if (viewModel.getChargeData() != null) {
            mChargeViewModel.chargeData = viewModel.getChargeData()
            if (mChargeViewModel.topInquiries.value == null || mChargeViewModel.topInquiries.value!!.items.isEmpty()) {
                mChargeViewModel.setTopInquiries(viewModel.getTopInquiresResult())
            }
        }
    }

    override fun getDataFromServer() {
        if (viewModel.getAppVersionLiveData().value == null) {
            resetAppServiceList()
            viewModel.getLastVersion()
        } else if (viewModel.getIsDataCompleted().value == null || !(viewModel.getIsDataCompleted().value!!)) {
            mChargeViewModel = DIMain.getViewModel(ChargeViewModel::class.java)

            mChargeViewModel.loadChargeData()
            mChargeViewModel.loadTopInquiries()

            resetAppServiceList()
            viewModel.getAppServiceList()
            viewModel.getBannerFromServer(BannerRepository.BANNER_TYPE_MAIN_PAGE)
        }
    }

    private fun resetAppServiceList() {
        mShimmerServicesView.visibility = View.VISIBLE
        mShimmerErrorView.visibility = View.GONE
        mShimmerFrameLayout.startShimmer()
        mShimmerFrameLayout.showShimmer(true)

        viewModel.setLastSplashUpdateTime()
        viewModel.checkIsDataCompleted()
        viewModel.getIsDataCompleted().observe(viewLifecycleOwner, checkDataCompletedObserver)
    }

    private fun animateOut(animationListener: Animation.AnimationListener) {
        val slide: Animation?
        //TODO height should be -15
        slide = TranslateAnimation(0.0f, 0.0f, 0.0f, mServiceListContainer.height.toFloat())

        slide.setDuration(300)
        slide.setFillAfter(true)
        slide.setFillEnabled(true)
        mServiceListContainer.startAnimation(slide)

        slide.setAnimationListener(animationListener)
    }

    private fun animateIn() {
        val slideToolbar: Animation?
        slideToolbar = TranslateAnimation(0.0f, 0.0f, -(mToolbar.height.toFloat()), 0.0f)

        slideToolbar.setDuration(300)
        slideToolbar.setFillAfter(true)
        slideToolbar.setFillEnabled(true)
        mToolbar.startAnimation(slideToolbar)


        val slideList: Animation?
        slideList = TranslateAnimation(0.0f, 0.0f, (mServiceListContainer.height.toFloat()), 0.0f)

        slideList.setDuration(300)
        slideList.setFillAfter(true)
        slideList.setFillEnabled(true)
        mServiceListContainer.startAnimation(slideList)
        slideList.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                mServiceListContainer.clearAnimation()
                mServiceRecycler.visibility = View.VISIBLE

                val fadeIn = AlphaAnimation(0.1f, 1f)
                fadeIn.interpolator = DecelerateInterpolator()
                fadeIn.duration = 300

                mServiceRecycler.startAnimation(fadeIn)

            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })

        val fadeIn = AlphaAnimation(0.1f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()

        fadeIn.duration = 300

        mPhoneNumberInput.startAnimation(fadeIn)
        mPhoneNumberInputTitle.startAnimation(fadeIn)
        mBannerContainer.startAnimation(fadeIn)

    }

    private fun noChargeDataError() {
        mHasNoChargeDataError = true
        mPhoneNumberInput.disableEditText(DIMain.getABResources().getDrawable(R.drawable.phone_and_operator_input_disable_background))
        mPhoneNumberInputTitle.setTextColor(DIMain.getABResources().getColor(R.color.landing_service_item_title_inactive_text_color))
        mChargeViewModel.loadChargeData()
    }

    private fun openChargeFragmentWithDefaultNumber(phoneNumber: String?) {
        if (phoneNumber!!.length == 11) {
            setChargeViewModelData()

            mChargeViewModel.setDefaultPhoneNumber(phoneNumber)

            animateOut(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                }

                override fun onAnimationStart(p0: Animation?) {
                    openFragment(HomeFragment.newInstance(ROOT_CHARGE))
                }

            })

        }
    }

    private fun setRecyclerMargins() {
        val abResources = DIMain.getABResources()

        val width: Int = ViewUtils.getDisplayMetrics(context).widthPixels
        val recyclerMargin: Int = (width - (2 * abResources.getDimenPixelSize(R.dimen.landing_service_list_margin_sides))
                - (3 * abResources.getDimenPixelSize(R.dimen.landing_service_item_icon_width))) / 4

        val recyclerLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        recyclerLayoutParams.setMargins(recyclerMargin, 0, 0, abResources.getDimenPixelSize(R.dimen.landing_service_card_padding_bottom))
        mServiceRecycler.layoutParams = recyclerLayoutParams
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            if (mDownloadingUpdate) {
                mDownloadingUpdate = false
            } else {
                mServiceRecycler.visibility = View.GONE
                mServicesAdapter.resetListSelection()

                animateIn()

                mPhoneNumberInput.setPhoneNumber("")

                if (mShimmerFrameLayout.isShimmerVisible) {
                    mShimmerFrameLayout.stopShimmer()
                }
            }
        }
    }
}