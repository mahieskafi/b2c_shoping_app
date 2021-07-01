package com.srp.eways.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.network.NetworkUtil
import com.srp.eways.ui.view.LoadingStateView

//import com.srp.ewayspanel.di.MainDI
//import com.srp.ewayspanel.network.NetworkResponseCodes
//import com.srp.ewayspanel.network.NetworkUtil
//import com.srp.ewayspanel.ui.view.LoadingStateView
import java.util.*

/**
 * Created by ErfanG on 04/09/2019.
 */
open abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    interface RetryClickListener{
        fun onClicked()
    }

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1
    val VIEW_TYPE_RETRY = 2
    val VIEW_TYPE_NO_DATA = 3

    private val mContext : Context

    private var mData : ArrayList<T?> = ArrayList<T?>()

    internal val mHasPaging : Boolean
    internal var mIsLoading : Boolean = false
    internal var mIsInRetryMode = false

    private var mRetryHolder : RetryViewHolder? = null

    private var mRetryListener : RetryClickListener

    private var mErrorCode = NetworkResponseCodes.SUCCESS


    constructor(context : Context, retryListener: RetryClickListener): this(context, retryListener, true)

    constructor(context : Context, retryListener: RetryClickListener, hasPaging : Boolean): this(context,retryListener, ArrayList(), hasPaging)

    constructor(context : Context, retryListener: RetryClickListener, data: ArrayList<T?>): this(context,retryListener, data, true)

    constructor(context : Context, retryListener: RetryClickListener, data : ArrayList<T?>, hasPaging: Boolean){
        mContext = context
        mData = data
        mHasPaging = hasPaging
        mRetryListener = retryListener

        setIsLoading(true)
    }


    fun setNewData(newData: ArrayList<T>){

        if(newData != null){

            mData = ArrayList<T?>(newData)
        }
        else{

            mData = ArrayList<T?>()
        }

        mIsInRetryMode = mErrorCode != NetworkResponseCodes.SUCCESS

        notifyDataSetChanged()

    }

    fun notifyDataChangedAt(position : Int, data : T?){
        mData[position] = data
        notifyItemChanged(position)
    }
    fun notifyDataRemovedAt(position : Int){
        if(position < mData.size) {
            mData.remove(mData[position])
            notifyItemRemoved(position)
        }
    }

    fun appendData(appendData : List<T>){

        mData.addAll(appendData)

        mIsInRetryMode = mErrorCode != NetworkResponseCodes.SUCCESS

//        notifyItemRangeInserted(mData.size - appendData.size, appendData.size)
        notifyDataSetChanged()
    }
    fun getData() : ArrayList<T?> = mData

    override fun getItemViewType(position: Int): Int {

        if(position == mData.size) {

            if(mIsInRetryMode || (mIsLoading && mData.size == 0)) {
                return VIEW_TYPE_RETRY
            }
            else if(mData.size == 0){
                return VIEW_TYPE_NO_DATA
            }
            else {

                return VIEW_TYPE_LOADING
            }
        }

        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{

        if(viewType == VIEW_TYPE_LOADING) {

            return LoadingViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_loading, parent, false))
        }
        else if(viewType == VIEW_TYPE_RETRY) {

            if(mRetryHolder == null){

                mRetryHolder = RetryViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_retry, parent, false))
            }

            return mRetryHolder as RecyclerView.ViewHolder
        }
        else if(viewType == VIEW_TYPE_NO_DATA){

            return NoDataViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_no_data, parent, false))
        }

        return onCreateViewHolder2(parent, viewType)
    }

    abstract fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): VH

    override fun getItemCount(): Int {

        if(mIsInRetryMode) {

            return 1
        }

        if(mHasPaging && mIsLoading) {

            return mData.size + 1
        }

        if(mData.size == 0){
            return 1
        }
        else{
            return mData.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position < mData.size){

            onBindViewHolder2(holder, position)
        }
        else{

            if(mIsInRetryMode || (mIsLoading && mData.size == 0)) {

                val innerListener = object : LoadingStateView.RetryListener{
                    override fun onRetryButtonClicked() {
                        mIsInRetryMode = false
                        mRetryListener.onClicked()
                    }

                }
                (holder as RetryViewHolder).setListener(innerListener)

                if(mIsLoading ) {
                    setRetryState(LoadingStateView.STATE_LOADING)
                }
                else{
                    setRetryState(LoadingStateView.STATE_ERROR)
                }
            }
            //BIND LOADING
        }
    }

    abstract fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int)

    fun setIsLoading(isLoading : Boolean){

        mIsLoading = isLoading

        if(mIsLoading){
            mIsInRetryMode = false
        }
        else{
            mIsInRetryMode = mData.size == 0 && mErrorCode != NetworkResponseCodes.SUCCESS
        }

        notifyDataSetChanged()
    }
    fun setErrorCode(errorCode : Int){

        mErrorCode = errorCode

        if(mErrorCode != NetworkResponseCodes.SUCCESS){
            mIsInRetryMode = mData.size == 0
        }
        else{
            mIsInRetryMode = false
        }

        notifyDataSetChanged()
    }

    fun setRetryState(state : Int){

        if((mIsInRetryMode || (mIsLoading && mData.size == 0)) && mRetryHolder != null){

            val AB = DIMain.getABResources()

            if(state == LoadingStateView.STATE_ERROR) {
                (mRetryHolder as RetryViewHolder).retry.setStateAndDescription(state, NetworkUtil.getErrorText(mErrorCode), mErrorCode != NetworkResponseCodes.ERROR_NO_INTERNET)
            }
            else if(state == LoadingStateView.STATE_LOADING){

                (mRetryHolder as RetryViewHolder).retry.setStateAndDescription(state, AB.getString(R.string.loading_message), true)
            }
        }
    }

    class LoadingViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    }

    class RetryViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        var retry = view.findViewById<LoadingStateView>(R.id.retry_view)

        init {

            val AB = DIMain.getABResources()
            retry.setStateAndDescription(LoadingStateView.STATE_LOADING, AB.getString(R.string.loading_message), true)
        }

        fun setListener(listener : LoadingStateView.RetryListener){
            retry.setRetryListener(listener)
        }
    }

    class NoDataViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }
}