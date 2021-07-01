package com.srp.ewayspanel.ui.store.mainpage

import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.model.storepage.mainpage.Data
import com.srp.ewayspanel.model.storepage.mainpage.Item
import com.srp.ewayspanel.model.storepage.product.ProductInventoryAddCheckModel
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.store.filter.ProductListFragment
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog
import com.srp.ewayspanel.ui.store.product.ProductAdapter


/**
 * Created by ErfanG on 2/18/2020.
 */
class StoreMainPageFragment : NavigationMemberFragment<StoreMainPageViewModel> {

    companion object {
        const val ITEM_COUNT = 4
        const val FRAGMENT_ID = 0
    }

    private var mShopCartViewModel = DI.getViewModel(ShopCartViewModel::class.java)
    private lateinit var mCategoryList: RecyclerView
    private lateinit var mCategoryListAdapter: CategoryListAdapter

    private lateinit var listObserver: Observer<ArrayList<Data>>
    private lateinit var shopCartObserver: Observer<ArrayList<ShopCartItemModel>>

    private lateinit var mInventoryForAddObserver: Observer<ProductInventoryAddCheckModel>
    private var mFocusableItemPosition = -2

    constructor()

    override fun acquireViewModel(): StoreMainPageViewModel {
        return DI.getViewModel(StoreMainPageViewModel::class.java)
    }

    override fun getNavigationViewType(): Int {
        return -1
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_store_page
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val respurces = DI.getABResources()

        view.setBackgroundColor(resources.getColor(R.color.store_main_page_background_color))

        mCategoryList = view.findViewById(R.id.category_list)

        mCategoryListAdapter = CategoryListAdapter(context!!,
                object : BaseRecyclerAdapter.RetryClickListener {
                    override fun onClicked() {
                        viewModel.getMainPageData(ITEM_COUNT)
                    }
                },
                object : MainPageCategoryItemListAdapter.StoreMainPageListener {

                    override fun onItemClicked(position: Int, item: Item) {
                        val productInfo = ProductInfo(id = item.id, name = item.name, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                                minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point, discount = item.discount,
                                lawId = item.lawId, isSim = item.isSim)

                        mShopCartViewModel.getInventoryAndOpenDetail(productInfo)
                    }

                    override fun onLoadMoreClicked(data: Data) {

                        val filterProductRequest = FilterProductRequest(categoryId = data.id, order = 4, categoryNodeRootParent = -1)

                        openFragment(ProductListFragment.newInstance(filterProductRequest, false))

                    }

                    override fun onAddClicked(productInfo: com.srp.ewayspanel.model.shopcart.ProductInfo, newCount: Int, isTotalCount: Boolean) {
                        mShopCartViewModel.getInventoryAndAddProduct(productInfo, if (isTotalCount) newCount else 1, isTotalCount, FRAGMENT_ID)
                    }

                    override fun onRemoveClicked(productId: Int, newCount: Int, isTotalCount: Boolean) {
                        mShopCartViewModel.removeProduct(productId, if (isTotalCount) newCount else 1, isTotalCount)
                    }

                    override fun onDeleteClicked(productId: Int) {
                        mShopCartViewModel.removeProduct(productId, 0, true)
                    }

                    override fun onSaveInstaceState(state: Parcelable?, itemPosition: Int) {
                    }
                }
        )

        observeUserInfo()

        mCategoryList.layoutManager = LinearLayoutManager(context)
        mCategoryList.adapter = mCategoryListAdapter
        mCategoryList.addItemDecoration(mLastItemDecoration)

        listObserver = Observer<ArrayList<Data>> {

            val ll = it.filter { data -> data.id == -1L }

            if (ll.isEmpty()) {
                it.add(0, Data(id = -1))
            }
            mCategoryListAdapter.setNewData(it)
        }

        shopCartObserver = Observer {

            if (mCategoryListAdapter != null) {

                for (i in 1 until mCategoryListAdapter.getData().size - 1) {
                    val categoryItemViewHolder: CategoryListAdapter.CategoryItemViewHolder? = mCategoryList.findViewHolderForAdapterPosition(i) as CategoryListAdapter.CategoryItemViewHolder?

                    if (categoryItemViewHolder != null) {
                        val categoryItemAdapter: MainPageCategoryItemListAdapter = categoryItemViewHolder.mView.getAdapter()
                        categoryItemAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        viewModel.getMainPageData(ITEM_COUNT)

        viewModel.getBannerData()
        viewModel.getBannerLive().observe(viewLifecycleOwner, Observer<BannerResponse> {

            if (it.bannerList.isNotEmpty()) {
                mCategoryListAdapter.setSliderList(it.bannerList as java.util.ArrayList<com.srp.eways.model.banner.Data>)
            }

        })

        mInventoryForAddObserver = Observer { productInfo ->
            if (productInfo != null && productInfo.observerId == FRAGMENT_ID) {
                InventoryNotExistDialog(context!!).show()

                mCategoryListAdapter.notifyDataSetChanged()
                mShopCartViewModel.consumeInventoryForAddLiveData()
            }
        }

        mShopCartViewModel.getInventoryForAddLiveData().observe(viewLifecycleOwner, mInventoryForAddObserver)

        mShopCartViewModel.getMainPageUnavailableProductError().observe(viewLifecycleOwner, Observer<String> { error_message ->
            if (error_message != null) {
                if (error_message.isNotEmpty()) {
                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show()
                    mShopCartViewModel.consumeMainPageUnavailableProductError()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()

        viewModel.getMainPageData().observe(this, listObserver)
        mShopCartViewModel.getShopCartProductList().observe(this@StoreMainPageFragment, shopCartObserver)

    }

    override fun onStop() {
        super.onStop()

        viewModel.getMainPageData().removeObserver(listObserver)
        mShopCartViewModel.getShopCartProductList().removeObserver(shopCartObserver)
    }

    private val mLastItemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = DI.getABResources().getDimenPixelSize(R.dimen.store_main_page_category_last_margin_bottom)
            }
        }
    }

    private fun observeUserInfo() {
        DI.getViewModel(UserInfoViewModel::class.java).getUserInfoLiveData().observe(viewLifecycleOwner, object : Observer<UserInfo?> {
            override fun onChanged(userInfo: UserInfo?) {
                if (userInfo != null) {
                    val loyaltyScore = userInfo.loyaltyScore
                    if (loyaltyScore != null && mCategoryListAdapter != null) {
                        mCategoryListAdapter.setLoyaltyScore(userInfo.loyaltyScore!!)
                        mCategoryListAdapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mShopCartViewModel.getShopCartProductList().observe(this@StoreMainPageFragment, shopCartObserver)
        } else {
            mShopCartViewModel.getShopCartProductList().removeObserver(shopCartObserver)
        }
    }
}