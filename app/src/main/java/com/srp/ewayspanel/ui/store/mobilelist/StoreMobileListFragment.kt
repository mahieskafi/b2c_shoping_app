package com.srp.ewayspanel.ui.store.mobilelist

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.srp.eways.di.DIMain
import com.srp.eways.ui.IContentLoadingStateManager
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.EmptyView
import com.srp.eways.ui.view.LoadingStateView
import com.srp.eways.util.HidingScrollListener
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.model.storepage.product.ProductInventoryAddCheckModel
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener
import java.util.*

class StoreMobileListFragment : NavigationMemberFragment<StoreMobileListViewModel>() {

    companion object {

        const val FRAGMENT_ID = 3

        public fun newInstance(): StoreMobileListFragment {
            return StoreMobileListFragment()
        }
    }

    private val mShopCartViewModel = DI.getViewModel(ShopCartViewModel::class.java)

    private lateinit var mRecyclerBrand: RecyclerView
    private lateinit var mRecyclerProduct: RecyclerView

    private lateinit var mEmptyView: EmptyView
    private lateinit var mLoadingStateView: LoadingStateView

    private lateinit var mBrandAdapter: MobileBrandAdapter
    private lateinit var mProductAdapter: StoreMobileListBrandAdapter

    private lateinit var mBrandViewObserver: ViewTreeObserver.OnGlobalLayoutListener
    private lateinit var mProductRecyclerScrollListener: RecyclerView.OnScrollListener

    private lateinit var mProductList: List<StoreMobileListBrandAdapter.MobileListItem>
    private var mFilterHeaderViewHeight = 0
    private var mBrandSelected = false
    private var mErrorMessage = ""

    private var mCurrentPosition = -1

    private lateinit var mInventoryForAddObserver: Observer<ProductInventoryAddCheckModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var abResources = DI.getABResources()

        mRecyclerBrand = view.findViewById(R.id.recycler_brand)
        mRecyclerProduct = view.findViewById(R.id.recycler_mobile)

        mLoadingStateView = view.findViewById(R.id.loadingstateview)
        mEmptyView = view.findViewById(R.id.emptyview)

        mBrandViewObserver = ViewTreeObserver.OnGlobalLayoutListener {

            mFilterHeaderViewHeight = mRecyclerBrand.height

            mRecyclerBrand.viewTreeObserver.removeOnGlobalLayoutListener(mBrandViewObserver);

            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.height = mFilterHeaderViewHeight

            mRecyclerBrand.layoutParams = layoutParams

            mProductAdapter.setHeaderViewHeight(mFilterHeaderViewHeight)
            mProductAdapter.setData(mProductList)
            mRecyclerProduct.adapter = mProductAdapter
        }

        mProductRecyclerScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy - mFilterHeaderViewHeight)

                val firstVisibleItemPosition: Int = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (firstVisibleItemPosition > 0 && !mBrandSelected && !(mRecyclerProduct.layoutManager as LinearLayoutManager).isSmoothScrolling) {
                    mBrandAdapter.resetListSelection()
                    mBrandAdapter.getData()[firstVisibleItemPosition - 1].isSelected = true
                    mBrandAdapter.notifyDataSetChanged()

                    mRecyclerBrand.scrollToPosition(firstVisibleItemPosition - 1)
                }
            }
        }

        mRecyclerBrand.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        mBrandAdapter = MobileBrandAdapter(context!!, object : MobileBrandAdapter.BrandItemClickListener {
            override fun onBrandClicked(brandId: MobileBrandAdapter.Brand, position: Int) {
                mRecyclerProduct.removeOnScrollListener(mProductRecyclerScrollListener)

                mBrandAdapter.resetListSelection()
                mBrandAdapter.getData()[position].isSelected = true
                mBrandAdapter.notifyDataSetChanged()

                mBrandSelected = true

                val smoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return LinearSmoothScroller.SNAP_TO_START
                    }

                    override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {
                        return super.calculateDyToMakeVisible(view, snapPreference) + mFilterHeaderViewHeight
                    }

                }

                mRecyclerProduct.isNestedScrollingEnabled = false;
                smoothScroller.targetPosition = position + 1
                (mRecyclerProduct.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller)
//                (mRecyclerProduct.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position + 1, mFilterHeaderViewHeight)
            }
        })

        mRecyclerProduct.layoutManager = LinearLayoutManager(context)
        mProductAdapter = StoreMobileListBrandAdapter(context!!, object : ProductItemClickListener {

            override fun onDeleteClicked(productId: Int) {
                mShopCartViewModel.removeProduct(productId, 0, true)
            }

            override fun onRemoveClicked(productId: Int, newCount: Int, isTotalCount: Boolean) {
                mShopCartViewModel.removeProduct(productId, if (isTotalCount) newCount else 1, isTotalCount)
            }

            override fun onAddClicked(productInfo: com.srp.ewayspanel.model.shopcart.ProductInfo?, newCount: Int, isTotalCount: Boolean) {
                mShopCartViewModel.getInventoryAndAddProduct(productInfo!!, if (isTotalCount) newCount else 1, isTotalCount, FRAGMENT_ID)
            }

            override fun onItemClick(position: Int, item: ProductInfo?) {
                mCurrentPosition = position
                mShopCartViewModel.getInventoryAndOpenDetail(item!!)
            }

        })

        mRecyclerProduct.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
                if (!(mRecyclerProduct.layoutManager as LinearLayoutManager).isSmoothScrolling && !mBrandSelected) {
                    mRecyclerBrand.animate().translationY(-mRecyclerBrand.height.toFloat()).setInterpolator(AccelerateInterpolator(2F)).start()
                }
            }

            override fun onShow() {
                mRecyclerBrand.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2F)).start()
            }
        })

        mRecyclerProduct.setOnTouchListener { p0, p1 ->
            mRecyclerProduct.addOnScrollListener(mProductRecyclerScrollListener)

            mBrandSelected = false
            false
        }

        viewModel.getBrandLiveData().observe(viewLifecycleOwner, Observer { result ->
            if (result != null && result.isNotEmpty()) {
                mBrandAdapter.setData(result.toList().map {
                    MobileBrandAdapter.Brand(brandId = it.first, brandName = it.second, isSelected = false)
                }, 0)

                mRecyclerBrand.adapter = mBrandAdapter
                mRecyclerBrand.viewTreeObserver.addOnGlobalLayoutListener(mBrandViewObserver)

            } else {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY)
            }
        })

        viewModel.getProductLiveData().observe(viewLifecycleOwner, Observer { result ->
            if (result != null && result.isNotEmpty()) {
                mProductList = result.toList().map { pair ->
                    val list: MutableList<ProductInfo> = pair.second
                    StoreMobileListBrandAdapter.MobileListItem(brandId = pair.first, brandName = result[pair.first]!![0].brandName, productList = list.sortedWith(compareBy(ProductInfo::price)).toMutableList())
                }

                val handler = Handler()
                handler.postDelayed({
                    setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_CONTENT)
                }, 500)


            } else {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY)
            }
        })

        viewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mErrorMessage = it
                setLoadingState(LoadingStateView.STATE_ERROR)
                viewModel.consumeErrorLiveData()
            }
        })

        if (Utils.isInternetAvailable()) {
            setLoadingState(LoadingStateView.STATE_LOADING)
            viewModel.getFilteredProductList(FilterProductRequest(4285L, pageSize = 1000, onlyAvailable = true))
        } else {
            setLoadingState(IContentLoadingStateManager.VIEWSTATE_NO_INTERNET_ERROR)
        }

        mLoadingStateView.setRetryListener {
            setLoadingState(LoadingStateView.STATE_LOADING)
            val handler = Handler()
            handler.postDelayed({
                viewModel.getFilteredProductList(FilterProductRequest(4285L, pageSize = 1000, onlyAvailable = true))
            }, 1500)

        }

        mInventoryForAddObserver = Observer { productInfo ->
            if (productInfo != null && productInfo.observerId == FRAGMENT_ID) {
                InventoryNotExistDialog(context!!).show()

                mProductAdapter.notifyDataSetChanged()
                mShopCartViewModel.consumeInventoryForAddLiveData()
            }
        }

        mShopCartViewModel.getInventoryForAddLiveData().observe(viewLifecycleOwner, mInventoryForAddObserver)

        mShopCartViewModel.getShopCartProductList().observe(viewLifecycleOwner, Observer<ArrayList<ShopCartItemModel>> {
            if (mCurrentPosition != -1) {
                if (mRecyclerProduct.itemAnimator != null && mRecyclerProduct.itemAnimator is SimpleItemAnimator) {
                    (mRecyclerProduct.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                }
                mProductAdapter.notifyItemChanged(mCurrentPosition)
            } else {
                mProductAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun setLoadingState(loadingState: Int) {
        var abResources = DIMain.getABResources();
        when (loadingState) {
            IContentLoadingStateManager.VIEWSTATE_SHOW_LOADING -> {
                mLoadingStateView.visibility = View.VISIBLE
                mEmptyView.visibility = View.GONE
                mRecyclerBrand.visibility = View.INVISIBLE
                mRecyclerProduct.visibility = View.INVISIBLE
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, abResources.getString(R.string.loading_message), false)
            }
            IContentLoadingStateManager.VIEWSTATE_SHOW_CONTENT -> {
                mLoadingStateView.visibility = View.GONE
                mEmptyView.visibility = View.GONE
                mRecyclerBrand.visibility = View.VISIBLE
                mRecyclerProduct.visibility = View.VISIBLE
            }
            LoadingStateView.STATE_ERROR, IContentLoadingStateManager.VIEWSTATE_SHOW_ERROR -> {
                mLoadingStateView.visibility = View.VISIBLE
                mEmptyView.visibility = View.GONE
                mRecyclerBrand.visibility = View.GONE
                mRecyclerProduct.visibility = View.GONE
                val errorDescription = if (mErrorMessage != null && mErrorMessage.isNotEmpty()) mErrorMessage else abResources.getString(R.string.network_error_undefined)
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorDescription, true)
            }
            IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY -> {
                mLoadingStateView.visibility = View.GONE
                mEmptyView.visibility = View.VISIBLE
                mRecyclerBrand.visibility = View.GONE
                mRecyclerProduct.visibility = View.GONE
            }
            IContentLoadingStateManager.VIEWSTATE_NO_INTERNET_ERROR -> {
                mLoadingStateView.visibility = View.VISIBLE
                mEmptyView.visibility = View.GONE
                mRecyclerBrand.visibility = View.GONE
                mRecyclerProduct.visibility = View.GONE
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, abResources.getString(com.srp.eways.R.string.network_error_no_internet), false)
            }
        }

    }

    override fun acquireViewModel(): StoreMobileListViewModel {
        return DI.getViewModel(StoreMobileListViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_store_mobile_list
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mShopCartViewModel.getInventoryForAddLiveData().observe(viewLifecycleOwner, mInventoryForAddObserver)
        } else {
            mShopCartViewModel.getInventoryForAddLiveData().removeObserver(mInventoryForAddObserver)
        }
    }
}