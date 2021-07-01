package com.srp.ewayspanel.ui.login

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.work.Operation
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.address.City
import com.srp.ewayspanel.model.address.CityResponse
import com.srp.ewayspanel.model.address.Province
import com.srp.ewayspanel.model.address.ProvinceResponse
import com.srp.ewayspanel.model.login.Address
import com.srp.ewayspanel.repository.address.AddressRepository
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddressViewModel : UserInfoViewModel() {

    companion object {

        private var instance: AddressViewModel? = null
        fun getInstance(): AddressViewModel {

            if (instance == null) {
                instance = AddressViewModel()
            }
            return instance as AddressViewModel
        }
    }

    private val mAddressRepository: AddressRepository

    private var mAddressListLive: MutableLiveData<List<Address>>
    private var mInsertAddressStatusLive: MutableLiveData<Long>

    private var mProvincesLiveData: MutableLiveData<ArrayList<Province>>
    private var mCitiesLiveData: MutableLiveData<ArrayList<City>>

    init {
        mAddressRepository = DI.getAddressRepository()

        mInsertAddressStatusLive = MutableLiveData()
        mAddressListLive = MutableLiveData()

        mProvincesLiveData = MutableLiveData()
        mCitiesLiveData = MutableLiveData()
    }

    fun insertAddress(address: Address) {
        address.userId = getUserInfoLiveData().value?.userId

        mAddressRepository.insertAddress(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Long> {
                    override fun onSuccess(insertStatus: Long) {
                        mInsertAddressStatusLive.value = insertStatus
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        mInsertAddressStatusLive.value = -1
                    }

                })
    }

    fun insertAddress(userInfo: UserInfo, addressName: String) {

        insertAddress(getAddress(userInfo, addressName))
    }

    fun getAddress(userInfo: UserInfo, addressName: String): Address {
        var address = Address()

        address.addressName = addressName
        address.address = userInfo.address
        address.cityId = userInfo.townId!!
        address.cityName = userInfo.townName
        address.fullName = userInfo.fullName
        address.mobile = userInfo.mobile
        address.phoneNumber = userInfo.phone
        address.postCode = userInfo.postCode
        address.stateId = userInfo.stateId!!
        address.stateName = userInfo.stateName
        address.userId = userInfo.userId

        return address
    }

    fun getAllAddresses() {
        mAddressRepository.getAllAddress(getUserInfoLiveData().value?.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Address>> {
                    override fun onNext(addressList: List<Address>) {
                        mAddressListLive.value = addressList
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                    }
                })
    }

    fun deleteAddress(id: Int) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                mAddressRepository.deleteAddress(id)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getAllAddresses()
            }
        }.execute()

    }

    fun updateAddress(newAddress: Address) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                mAddressRepository.update(newAddress)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getAllAddresses()
            }
        }.execute()

    }

    fun getProvinces() {
        mAddressRepository.getProvinces(object : BaseCallBackWrapper<ProvinceResponse>(this) {
            override fun onSuccess(responseBody: ProvinceResponse) {
                if (responseBody.status == NetworkResponseCodes.SUCCESS) {
                    mProvincesLiveData.value = responseBody.data
                }
            }

        })
    }

    fun getCities(provinceId: Int) {
        mAddressRepository.getCities(provinceId,object : BaseCallBackWrapper<CityResponse>(this) {
            override fun onSuccess(responseBody: CityResponse) {
                if (responseBody.status == NetworkResponseCodes.SUCCESS) {
                    mCitiesLiveData.value = responseBody.data
                }
            }

        })
    }

    fun getAddressListLiveData(): MutableLiveData<List<Address>> {
        return mAddressListLive
    }

    fun consumeAddressListLiveData() {
        mAddressListLive.value = null
    }

    fun getInsertStatusLiveData(): MutableLiveData<Long> {
        return mInsertAddressStatusLive
    }

    fun consumeInsertStatusLiveData() {
        mInsertAddressStatusLive.value = -1
    }

    fun getProvinceListLiveData(): MutableLiveData<ArrayList<Province>> {
        return mProvincesLiveData
    }

    fun getCityListLiveData(): MutableLiveData<ArrayList<City>> {
        return mCitiesLiveData
    }
}