package com.srp.ewayspanel.ui.store.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.ui.view.EmptyView
import com.srp.eways.ui.view.LoadingStateView
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest
import com.srp.ewayspanel.repository.storepage.StorePageRepository
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode
import com.srp.ewayspanel.ui.view.store.filter.FilterHeaderView

/**
 * Created by ErfanG on 04/11/2019.
 */
class CategoryListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface onLoadingRetryListener {
        fun onLoadingRetry()
    }

    companion object {
        private const val CATEGORY_VIEW_TYPE: Int = 1
        private const val FILTER_HEADER_VIEW_TYPE: Int = 0
    }

    private var mManager: CategoryTreeManager? = null

    private lateinit var mListener: CategoryNodeListener

    private var mRetryListener: onLoadingRetryListener? = null

    private var mHeaderView: FilterHeaderView? = null
    private var mView: View? = null
    private var mFilterProductRequest: FilterProductRequest? = null
    private var mSelectedNode: Long = 0

    private var mShowHeader: Boolean = false
    private var mLoadingViewState: Int

    constructor(data: StorePageRepository.CategoryData?, listener: CategoryNodeListener, retryListener: onLoadingRetryListener, loadingViewState: Int) {
        if (data == null) {
            mShowHeader = true
        } else {
            setNewData(data)
        }

        mListener = listener
        mRetryListener = retryListener
        mLoadingViewState = loadingViewState
    }

    fun setNewData(data: StorePageRepository.CategoryData) {
        mShowHeader = false
        mManager = CategoryTreeManager(data)

        mLoadingViewState = FilterFragment.VIEWSTATE_SHOW_CONTENT

        notifyDataSetChanged()
    }

    fun setCategorySelected(categoryId: Long) {

        mManager!!.setSelectedNode(categoryId)

        mSelectedNode = categoryId

        notifyDataSetChanged()
    }

    fun getSelectedCategory() = mSelectedNode

    fun setFilterHeader(filterHeaderView: FilterProductRequest) {
        mFilterProductRequest = filterHeaderView
    }

    fun resetSelection() {
        if(mManager!= null) {
            mManager!!.setSelectedNode(CategoryTreeManager.NO_POSITION_SELECTED)
        }

        mSelectedNode = -1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return FILTER_HEADER_VIEW_TYPE
        }
        return CATEGORY_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FILTER_HEADER_VIEW_TYPE) {
            if (mView == null) {
                mView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_holder_filter_header, parent, false)
                if (mHeaderView == null) {
                    mHeaderView = mView!!.findViewById(R.id.filter_header_view)
                }
            }
            return FilterHeaderViewHolder(mView!!, mFilterProductRequest!!, mRetryListener!!, mLoadingViewState)

        }
        return CategoryItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item, parent, false))
    }

    override fun getItemCount(): Int {
        if (mShowHeader) {
            return 1
        }
        return mManager!!.count + 1
    }

    public fun getHeaderView(): FilterHeaderView? {
        return mHeaderView
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == 0) {
            val viewHolder = holder as FilterHeaderViewHolder
            viewHolder.onViewStateChanged(mLoadingViewState)

            return
        }

        var pos = position - 1
        val item = mManager!!.getDataAt(pos)

        val viewHolder = holder as CategoryItemViewHolder
        viewHolder.setData(item)
        viewHolder.setDropDownClickListener(View.OnClickListener {

            val result = mManager!!.toggle(pos)

            if (result.second > -1) {
//                if (result.first == CategoryTreeNode.ToggleState.EXPANDED) {
//                    notifyItemRangeInserted(pos + 1, result.second)
//                } else {
//                    notifyItemRangeRemoved(pos + 1, result.second)
//                }
//                val brotherPositions = mManager.brotherPositions(pos)
//                for (i in brotherPositions) {
//                    notifyItemChanged(i)
//                }
                notifyDataSetChanged()

            }
            notifyItemChanged(pos)
        })
        viewHolder.setSelectedListener(View.OnClickListener {

            //            mManager.setSelectedNode(pos)
//
//            notifyDataSetChanged()

            setCategorySelected(item.categoryId)

            mListener.selectedNode(item)
            mListener.selectedNodeRootParent(mManager!!.getSelectedNodeRootParent(item))
        })

        if (mSelectedNode == item.categoryId) {
            mListener.selectedNode(item)
            mListener.selectedNodeRootParent(mManager!!.getSelectedNodeRootParent(item))
        }
    }


    class CategoryItemViewHolder : RecyclerView.ViewHolder {

        private var mItemView: CategoryItemView

        constructor(view: View) : super(view) {

            mItemView = view.findViewById(R.id.category_item)
        }

        fun setDropDownClickListener(clickListener: View.OnClickListener) {
            mItemView.setDropDownClickListener(clickListener)
        }

        fun setData(data: CategoryTreeNode) {
            mItemView.setData(data)
        }

        fun setSelectedListener(clickListener: View.OnClickListener) {
            mItemView.setSelectedClickListener(clickListener)
        }
    }

    fun setLoadingState(loadingViewState: Int) {
        mLoadingViewState = loadingViewState
        notifyDataSetChanged()
    }

    class FilterHeaderViewHolder : RecyclerView.ViewHolder {

        private var mLoadingView: LoadingStateView
        private var mEmptyView: EmptyView

        private var mFilterHeaderView: FilterHeaderView
        private var mRetryListener: onLoadingRetryListener? = null

        constructor(view: View, filterHeaderView: FilterProductRequest, retryListener: onLoadingRetryListener, loadingViewState: Int) : super(view) {

            mFilterHeaderView = view.findViewById(R.id.filter_header_view)

            mLoadingView = view.findViewById(R.id.loadingstateview)
            mEmptyView = view.findViewById(R.id.emptyview)

            if (filterHeaderView != null) {
                mFilterHeaderView.setSearchText(filterHeaderView.text)
                mFilterHeaderView.setIsAvailability(filterHeaderView.onlyAvailable)
                filterHeaderView.minPrice?.let { mFilterHeaderView.setMinPrice(it) }
            }

            onViewStateChanged(loadingViewState);
            mRetryListener = retryListener
            mLoadingView.setRetryListener {
                if (mRetryListener != null) {
                    mRetryListener!!.onLoadingRetry()
                }
            }
        }

        public fun onViewStateChanged(chargePageViewState: Int) {
            when (chargePageViewState) {
                FilterFragment.VIEWSTATE_NO_INTERNET -> {
                    mEmptyView.visibility = View.VISIBLE
                    mLoadingView.visibility = View.GONE
                    mEmptyView.setEmptyText(R.string.loadingstateview_text_network_unavailable);
                }
                FilterFragment.VIEWSTATE_SHOW_ERROR -> {
                    mLoadingView.visibility = View.VISIBLE
                    mEmptyView.visibility = View.GONE
                    mLoadingView.setStateAndDescription(
                            LoadingStateView.STATE_ERROR, DI.getABResources().getString(R.string.buycharge_failed_specialoffers), true)
                }
                FilterFragment.VIEWSTATE_SHOW_LOADING -> {
                    mLoadingView.visibility = View.VISIBLE
                    mEmptyView.visibility = View.GONE
                    mLoadingView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                            DI.getABResources().getString(R.string.loading_message),
                            true);
                }
                FilterFragment.VIEWSTATE_SHOW_CONTENT -> {
                    mEmptyView.visibility = View.GONE
                    mLoadingView.visibility = View.GONE
                }
            }
        }
    }
}