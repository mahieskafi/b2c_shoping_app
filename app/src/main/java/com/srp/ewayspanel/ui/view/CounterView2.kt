package com.srp.ewayspanel.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.yashoid.inputformatter.InputFormatter
import com.yashoid.sequencelayout.SequenceLayout


/**
 * Created by ErfanG on 8/23/2020.
 */
class CounterView2 : SequenceLayout {

    val ERRORCODE_MAX_EXCEEDED = -1

    interface Counter2ChangeListener {

        fun onCountAdded(newCount: Int, isTotalCount: Boolean)

        fun onCountRemoved(newCount: Int, isTotalCount: Boolean)

        fun onCountNotChanged(count: Int)
    }

    private val mAddButton: AppCompatImageView
    private val mSubtractButton: AppCompatImageView
    private val mCountEditText: AppCompatEditText


    private val mAddEnableButtonDrawable: Drawable
    private val mAddDisableButtonDrawable: Drawable
    private val mSubtractEnableButtonDrawable: Drawable
    private val mSubtractDisableButtonDrawable: Drawable

    private var mCount = 0
    private var mMaxCount = Integer.MAX_VALUE
    private var mMinCount = 0

    private var mListener: Counter2ChangeListener? = null

    private var mLastValue: Int = 0
    private var mNeedToUpdate: Boolean = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.counter_view_2, this, true)

        mAddButton = findViewById(R.id.add_button)
        mSubtractButton = findViewById(R.id.subtract_button)
        mCountEditText = findViewById(R.id.count)

        addSequences(R.xml.sequences_counter_view_2)


        val resources = DI.getABResources()

        mAddEnableButtonDrawable = resources.getDrawable(R.drawable.shop_item_add_icon_enable)
        mAddDisableButtonDrawable = resources.getDrawable(R.drawable.shop_item_add_icon_disable)
        mSubtractEnableButtonDrawable = resources.getDrawable(R.drawable.shop_item_remove_icon_enable)
        mSubtractDisableButtonDrawable = resources.getDrawable(R.drawable.shop_item_remove_icon_disable)

        mAddButton.setImageDrawable(mAddEnableButtonDrawable)
        mSubtractButton.setImageDrawable(mSubtractEnableButtonDrawable)

        mAddButton.setOnClickListener {
            setCount(mCount + 1)
        }

        mSubtractButton.setOnClickListener {
            setCount(mCount - 1)
        }


        mCountEditText.setTextColor(resources.getColor(R.color.product_detail_count))
        mCountEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        mCountEditText.background = resources.getDrawable(R.drawable.button_filter_header_cost_item_default_background)
        mCountEditText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mCountEditText.addTextChangedListener(InputFormatter(Utils.PersianNumberFormatter))
        mCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val text: String = mCountEditText.text.toString()
                if (text.isNotEmpty()) {
                    if (mLastValue != Integer.valueOf(text)) {
                        mLastValue = Integer.valueOf(text)
                        mNeedToUpdate = true
                        if (mLastValue > mMaxCount)
                            showError(ERRORCODE_MAX_EXCEEDED)
                    } else {
                        mNeedToUpdate = false
                    }

                    Handler().postDelayed({

                        if (mNeedToUpdate) {

                            setCount(mLastValue)
//                            mListener?.onHideKeyBoard(true)
                        }

                    }, 1100)
                }
            }
        })

        mCountEditText.setOnClickListener {
            mCountEditText.isCursorVisible = true
            mCountEditText.requestFocus()
        }

        mCountEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_BACK) {
                    mCountEditText.isCursorVisible = false
                }
                return false
            }
        })

        mCountEditText.setOnFocusChangeListener { view, b ->
            if (!b) {
                val mgr = mCountEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                mgr!!.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun setHorizantalyView(isHorizontal: Boolean) {
        if (isHorizontal) {
            addSequences(R.xml.sequences_horizontal_counter_view_2)
            mCountEditText.background = resources.getDrawable(R.drawable.edit_text_background_horizontal_counterview2)

        } else {
            addSequences(R.xml.sequences_counter_view_2)
            mCountEditText.background = resources.getDrawable(R.drawable.button_filter_header_cost_item_default_background)

        }
    }

    private fun showError(errorCode: Int) {
        Toast.makeText(context, String.format(DI.getABResources().getString(R.string.counterview_error_maxcount), Utils.toPersianNumber(mMaxCount)), Toast.LENGTH_SHORT).show()
    }


    fun setCount(count: Int, notify: Boolean = true) {

        var isAdd = -1
        var isTotalCount = false

        if (mCount < count && mCount < mMaxCount) {
            isAdd = 1
            if (count - mCount > 1) {
                isTotalCount = true
            }
        } else if (mCount > count) {
            isAdd = 0
            if (mCount - count > 1) {
                isTotalCount = true
            }
        } else {
            isAdd = -1
        }

        if (count > mMaxCount) {
            mCount = mMaxCount

        } else if (count < mMinCount) {
            mCount = mMinCount
        } else {
            mCount = count
        }

        mAddButton.setImageDrawable(mAddEnableButtonDrawable)
        mSubtractButton.setImageDrawable(mSubtractEnableButtonDrawable)

        mAddButton.isClickable = true
        mSubtractButton.isClickable = true

        if (mMinCount == mMaxCount) {
            mAddButton.setImageDrawable(mAddDisableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractDisableButtonDrawable)
            mAddButton.isClickable = false
            mSubtractButton.isClickable = false
        } else if (mCount == mMinCount) {
            mAddButton.setImageDrawable(mAddEnableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractDisableButtonDrawable)
            mAddButton.isClickable = true
            mSubtractButton.isClickable = false
        } else if (mCount == mMaxCount) {
            mAddButton.setImageDrawable(mAddDisableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractEnableButtonDrawable)
            mAddButton.isClickable = false
            mSubtractButton.isClickable = true
        } else {
            mAddButton.setImageDrawable(mAddEnableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractEnableButtonDrawable)
            mAddButton.isClickable = true
            mSubtractButton.isClickable = true
        }

        mCountEditText.setText(Utils.toPersianNumber(mCount))
        mCountEditText.setSelection(mCountEditText.text!!.length)

        if (count == 0) {
            mCountEditText.text!!.clear()
            mCountEditText.hint = "تعداد"
        }

        if (notify) {

            if (isAdd == -1) {
                mListener?.onCountNotChanged(mCount)
            } else if (isAdd == 0) {
                mListener?.onCountRemoved(mCount, isTotalCount)
            } else if (isAdd == 1) {
                mListener?.onCountAdded(mCount, isTotalCount)
            }
        }
    }

    fun counterViewDisable(isDisable: Boolean) {

        if (isDisable) {
            mAddButton.setImageDrawable(mAddDisableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractDisableButtonDrawable)
            mAddButton.isClickable = false
            mSubtractButton.isClickable = false
            mCountEditText.isClickable = false
            mCountEditText.isFocusable = false
            mCountEditText.isFocusableInTouchMode = false
            mCountEditText.isEnabled=false
            mCountEditText.text!!.clear()
            mCountEditText.hint = "تعداد"
        } else {

            mAddButton.setImageDrawable(mAddEnableButtonDrawable)
            mSubtractButton.setImageDrawable(mSubtractEnableButtonDrawable)
            mAddButton.isClickable = true
            mSubtractButton.isClickable = true
            mCountEditText.isEnabled=true
            mCountEditText.isFocusable = true
            mCountEditText.isFocusableInTouchMode = true

        }

    }

    fun setCountChangeListener(listener: Counter2ChangeListener) {
        mListener = listener
    }

    fun setMaxCount(maxCount: Int) {
        mMaxCount = maxCount
    }

    fun setMinCount(minCount: Int = 0) {
        mMinCount = minCount
    }

    fun getCount(): Int = mCount
}