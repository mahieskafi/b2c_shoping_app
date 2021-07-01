package com.srp.ewayspanel.ui.shopcart.address

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.srp.eways.di.DIMain
import com.srp.eways.di.DIMainCommon
import com.srp.eways.ui.charge.buy.RadioOptionModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.NColumnRadioGroup.RadioGroupListener
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.dialog.BottomDialog
import com.srp.eways.ui.view.dialog.BottomDialog.ConfirmationBottomDialogItemClickListener
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.address.City
import com.srp.ewayspanel.model.address.Province
import com.srp.ewayspanel.model.login.Address
import com.srp.ewayspanel.ui.login.AddressViewModel
import com.srp.ewayspanel.ui.view.shopcart.AddressContentView
import com.srp.ewayspanel.ui.view.store.sort.SortRadioGroup

class AddAddressFragment : NavigationMemberFragment<AddressViewModel>(), View.OnClickListener {

    interface Listener {
        fun onConfirmed(address: Address?, provinceId: Province, cityId: City)
        fun onCanceled()
    }

    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mContentView: AddressContentView
    private lateinit var mListener: Listener
    private var mAddress: Address? = null

    private lateinit var mButtonConfirm: ButtonElement
    private lateinit var mButtonCancel: ButtonElement

    private lateinit var mProvinceList: ArrayList<Province>
    private lateinit var mProvinceDialog: BottomDialog
    private lateinit var mCityDialog: BottomDialog

    private lateinit var mProvinceId: Province
    private lateinit var mCityId: City

    companion object {

        fun newInstance(): AddAddressFragment {
            return AddAddressFragment()
        }
    }

    override fun acquireViewModel(): AddressViewModel {
        return DI.getViewModel(AddressViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_address
    }


    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setAddress(address: Address) {
        mAddress = address
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var resources = DIMain.getABResources()
        mToolbar = view.findViewById(R.id.toolbar)
        mContentView = view.findViewById(R.id.content_view)
        mButtonConfirm = mContentView.findViewById(R.id.button_confirm)
        mButtonCancel = mContentView.findViewById(R.id.button_cancel)

        mButtonConfirm.setOnClickListener(this)
        mButtonCancel.setOnClickListener(this)

        mProvinceDialog = BottomDialog()
        mCityDialog = BottomDialog()

        with(mButtonConfirm) {
            setText(resources.getString(R.string.address_confirm_button_text))
            setTextSize(resources.getDimenPixelSize(R.dimen.address_content_confirm_button_text_size).toFloat())
            setTextColor(resources.getColor(R.color.addaddress_confirm_button_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.button_element_default_background_enabled))
            setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
            setLoadingColorFilter(resources.getColor(R.color.addaddress_confirm_button_text_color))
            hasIcon(false)
            setEnable(true)
        }

        with(mButtonCancel) {
            setText(resources.getString(R.string.address_cancel_button_text))
            setTextSize(resources.getDimenPixelSize(R.dimen.address_content_confirm_button_text_size).toFloat())
            setTextColor(resources.getColor(R.color.addaddress_cancel_button_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled))
            setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
            setLoadingColorFilter(resources.getColor(R.color.addaddress_cancel_button_text_color))
            hasIcon(false)
            setEnable(true)
        }

        setUpToolbar()

        if (mAddress != null) {
            mContentView.address = mAddress
        }

        mContentView.setItemClickListener(object : AddressContentView.ItemClickListener {
            override fun onCityClicked() {
                mCityDialog.setTitle(DIMainCommon.getABResources().getString(R.string.shop_cart_address_city_dialog_title))
                mCityDialog.setButtonEnable(false)

                if (!mCityDialog.isAdded) {
                    mCityDialog.show(fragmentManager!!, "")
                }
            }

            override fun onProvinceClicked() {
                mProvinceDialog.setTitle(DIMainCommon.getABResources().getString(R.string.shop_cart_address_province_dialog_title))
                mProvinceDialog.setButtonEnable(false)

                if (!mProvinceDialog.isAdded) {
                    mProvinceDialog.show(fragmentManager!!, "")
                }
            }

        })

        viewModel.getProvinceListLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                setupProvinceDialog(it)
            }
        })

        viewModel.getCityListLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                setupCityDialog(it)
            }
        })

        if (viewModel.getProvinceListLiveData().value == null) {
            viewModel.getProvinces()
        }
    }

    private fun setUpToolbar() {

        val resources = DI.getABResources()

        mToolbar.setShowShop(false)
        mToolbar.setShowTitle(true)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowNavigationDrawerMenu(false)
        mToolbar.setOnNavigationDrawerClickListener { toggleDrawer() }
        mToolbar.setOnBackClickListener { onBackPressed() }

        mToolbar.setBackgroundColor(resources.getColor(R.color.add_address_fragment_toolbar_back_color))
        if (mAddress != null) {
            mToolbar.setTitle(resources.getString(R.string.addressdialog_toolbar_title2))
        } else {
            mToolbar.setTitle(resources.getString(R.string.addressdialog_toolbar_title))
        }
//        mToolbar.setTitleIcon(resources.getDrawable(R.drawable.ic_address_feild_toolbar_icon))

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_confirm -> mListener.onConfirmed(mContentView.address, mProvinceId, mCityId)
            R.id.button_cancel -> {
                mListener.onCanceled()
                onBackPressed()
            }
        }
    }

    private fun setupProvinceDialog(provinceList: ArrayList<Province>) {
        val abResources = DIMain.getABResources()
        val radioOptionModels: MutableList<RadioOptionModel> = ArrayList()
        var selectedProvince: Province? = null

        for (province in provinceList) {
            radioOptionModels.add(RadioOptionModel(province.provinceName, false, province))
            if (province.provinceName.trim() == mAddress!!.stateName.trim()) {
                selectedProvince = province
            }
        }
        val mOneColumnRadioGroup = SortRadioGroup(context)
        mOneColumnRadioGroup.setColumnCount(1)
        mOneColumnRadioGroup.setData(radioOptionModels)
        if (selectedProvince != null) {
            mOneColumnRadioGroup.setSelectedRadioButton(provinceList.indexOf(selectedProvince))
            mProvinceId = selectedProvince
            viewModel.getCities(selectedProvince.provinceId)
        } else {
            mOneColumnRadioGroup.setSelectedRadioButton(0)
            mProvinceId = provinceList[0]
            viewModel.getCities(provinceList[0].provinceId)
        }
        mOneColumnRadioGroup.setOnItemSelectedListener(object : RadioGroupListener {
            override fun onItemSelected(index: Int, data: RadioOptionModel) {
                mOneColumnRadioGroup.setSelectedRadioButton(index)
                mProvinceDialog.setButtonEnable(true)

                mProvinceId = data.option as Province
            }

            override fun onItemRemoved(index: Int, removedData: RadioOptionModel) {}
        })
        mProvinceDialog.setChildContentView(mOneColumnRadioGroup)
        mProvinceDialog.setContentViewMargin(abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_left),
                abResources.getDimenPixelSize(R.dimen.dialog_select_province_margin_top),
                abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_right),
               0)

        mProvinceDialog.setListener(object : ConfirmationBottomDialogItemClickListener {
            override fun onConfirmClicked() {
                mProvinceDialog.dismiss()

                mContentView.setProvince(mProvinceId.provinceName)
                viewModel.getCities(mProvinceId.provinceId)
            }

            override fun onCancelClicked() {
                mProvinceDialog.dismiss()
            }
        })
    }

    private fun setupCityDialog(cityList: ArrayList<City>) {
        val abResources = DIMain.getABResources()
        val radioOptionModels: MutableList<RadioOptionModel> = ArrayList()
        var selectedCity: City? = null

        for (city in cityList) {
            radioOptionModels.add(RadioOptionModel(city.cityName, false, city))
            if (city.cityName.trim() == mAddress!!.cityName.trim()) {
                selectedCity = city
            }
        }
        val mOneColumnRadioGroup = SortRadioGroup(context)
        mOneColumnRadioGroup.setColumnCount(1)
        mOneColumnRadioGroup.setData(radioOptionModels)
        if (selectedCity != null) {
            mOneColumnRadioGroup.setSelectedRadioButton(cityList.indexOf(selectedCity))
            mCityId = selectedCity
        } else {
            mOneColumnRadioGroup.setSelectedRadioButton(0)
            mCityId = cityList.get(index = 0)
        }
        mOneColumnRadioGroup.setOnItemSelectedListener(object : RadioGroupListener {
            override fun onItemSelected(index: Int, data: RadioOptionModel) {
                mOneColumnRadioGroup.setSelectedRadioButton(index)
                mCityDialog.setButtonEnable(true)

                mCityId = data.option as City
            }

            override fun onItemRemoved(index: Int, removedData: RadioOptionModel) {}
        })
        mCityDialog.setChildContentView(mOneColumnRadioGroup)
        mCityDialog.setContentViewMargin(abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_left),
                abResources.getDimenPixelSize(R.dimen.dialog_select_province_margin_top),
                abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_right),
               0)

        mCityDialog.setListener(object : ConfirmationBottomDialogItemClickListener {
            override fun onConfirmClicked() {
                mCityDialog.dismiss()

                mContentView.setCity(mCityId.cityName)
            }

            override fun onCancelClicked() {
                mCityDialog.dismiss()
            }
        })
    }
}