package com.srp.eways.ui.view.input

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain


/**
 * Created by ErfanG on 17/08/2019.
 */
class InputElement : ViewGroup {

    private lateinit var mInputText: AppCompatEditText
    private lateinit var mDescriptionText: AppCompatTextView
    private lateinit var mIcon: ImageView
    private lateinit var mCancelIcon: ImageView

    private lateinit var mCancelTextWatcher: TextWatcher

    private var mHasCancelIcon: Boolean = false
    private var mHasIcon: Boolean = false
    private var mHasDescription: Boolean = false


    private var mSelectedBackground: Drawable? = null
    private var mUnSelectedBackground: Drawable? = null

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAtr: Int) : super(context, attrs, defStyleAtr) {
        initialize(context, attrs, defStyleAtr)
    }

    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAtr: Int) {

        mInputText = AppCompatEditText(context)
        mIcon = ImageView(context)
        mCancelIcon = ImageView(context)
        mDescriptionText = AppCompatTextView(context)


        mInputText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mInputText.background = ColorDrawable(1)
        mInputText.textDirection = View.TEXT_DIRECTION_RTL
        mInputText.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        mInputText.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        mInputText.setSingleLine()
        mInputText.setLines(1)
        mInputText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                background = mSelectedBackground
            } else {
                background = mUnSelectedBackground
            }
        }

        mIcon.scaleType = ImageView.ScaleType.CENTER


        mCancelIcon.visibility = View.GONE
        mCancelIcon.setOnClickListener {

            mInputText.error = null

            mInputText.setText("")
            mCancelIcon.visibility = View.GONE
        }
        val padding: Int = (resources.displayMetrics.density * 6).toInt()
        mCancelIcon.setPadding(padding, padding, padding, padding)


        val resources = DIMain.getABResources()

        mSelectedBackground = resources.getDrawable(R.drawable.input_element_background_selected)
        mUnSelectedBackground = resources.getDrawable(R.drawable.input_element_background_unselected)
        background = mUnSelectedBackground
        clipToPadding = false

        mDescriptionText.visibility = View.GONE

        addView(mInputText)
        addView(mIcon)
        addView(mCancelIcon)
        addView(mDescriptionText)


        setAttrs(attrs)


        mCancelTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0)
                    mCancelIcon.visibility = View.VISIBLE
                else
                    mCancelIcon.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    }

    private fun setAttrs(attrs: AttributeSet?) {

        var typeface: Typeface? = null
        var descriptionTypeFace: Typeface?

        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.InputElement, 0, 0)

            if (attrArray.hasValue(R.styleable.InputElement_android_inputType)) {
                mInputText.inputType = attrArray.getInt(R.styleable.InputElement_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
            }

            if (attrArray.hasValue(R.styleable.InputElement_font)) {

                val typefaceResourceId = attrArray.getResourceId(R.styleable.InputElement_font, 0)

                typeface = ResourcesCompat.getFont(context, typefaceResourceId)
            }

            attrArray.recycle()
        }
        if (typeface == null) {

            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        }

        mInputText.typeface = typeface

        descriptionTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        if (descriptionTypeFace != null) {
            mDescriptionText.typeface = descriptionTypeFace
        }
    }

    fun hasGlobalIcon(has: Boolean) {

        mHasIcon = has
        requestLayout()

    }

    fun hasIcon(has: Boolean) {
        mHasCancelIcon = has


        mInputText.removeTextChangedListener(mCancelTextWatcher)

        if (mHasCancelIcon) {

            if (mInputText.text?.isNotEmpty()!!) {
                mCancelIcon.visibility = View.VISIBLE
            }
            else{
                mCancelIcon.visibility = View.GONE
            }

            mInputText.addTextChangedListener(mCancelTextWatcher)
        } else {
            mCancelIcon.visibility = View.GONE
        }

        mInputText.text = mInputText.text
        requestLayout()
    }

    fun setHasCancelIcon(hasCancelIcon: Boolean) {
        mHasCancelIcon = hasCancelIcon

        if (hasCancelIcon) {
            mCancelIcon.visibility = View.VISIBLE
        } else {
            mCancelIcon.visibility = View.GONE
        }
    }

    fun setCancelIconPadding(padding: Int) {
        mCancelIcon.setPadding(padding, padding, padding, padding)

    }

    fun switchInputType(visible: Boolean) {
        if (visible) {
            mInputText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            mInputText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val density = context.resources.displayMetrics.density
        val abResources = DIMain.getABResources()

        //connect this view to left side
        mIcon.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        if (mHasIcon) {
            mIcon.layout(
                    width - abResources.getDimenPixelSize(R.dimen.inputelement_icon_size),
                    (height - mIcon.measuredHeight) / 2,
                    width - abResources.getDimenPixelSize(R.dimen.inputelement_icon_margin_right),
                    height - (height - mIcon.measuredHeight) / 2)
        } else {
            mIcon.layout(
                    width,
                    (height - mIcon.measuredHeight) / 2,
                    width,
                    height - (height - mIcon.measuredHeight) / 2)
        }

        if (mHasCancelIcon) {
            mCancelIcon.measure(MeasureSpec.makeMeasureSpec(abResources.getDimenPixelSize(R.dimen.inputelement_cancel_icon_size),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(abResources.getDimenPixelSize(R.dimen.inputelement_cancel_icon_size), MeasureSpec.EXACTLY))
            mCancelIcon.layout(
                    abResources.getDimenPixelSize(R.dimen.inputelement_cancel_icon_left),
                    (height - mCancelIcon.measuredHeight) / 2,
                    mCancelIcon.measuredWidth + abResources.getDimenPixelSize(R.dimen.inputelement_cancel_icon_left),
                    height - (height - mCancelIcon.measuredHeight) / 2)

        }

        mInputText.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mInputText.layout(
                when (mCancelIcon.visibility == View.VISIBLE) {
                    true -> mCancelIcon.right
                    false -> 0
                } + abResources.getDimenPixelSize(R.dimen.inputelement_text_margin_left),
                (height / 2) - (mInputText.measuredHeight / 2),
                when (mHasIcon) {
                    true -> mIcon.left
                    false -> width - abResources.getDimenPixelSize(R.dimen.inputelement_text_margin_right)
                },
                (height / 2) + (mInputText.measuredHeight / 2))


        if (mHasDescription) {
            mDescriptionText.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
            mDescriptionText.layout(
                    (density * 40).toInt(),
                    (density * 8).toInt(),
                    (density * 40).toInt() + mDescriptionText.measuredWidthAndState,
                    height - (density * 8).toInt())

        }
    }


    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        mInputText.isEnabled = enabled
        mCancelIcon.isEnabled = enabled
    }

    fun setInputType(inputType: Int) {
        when (inputType) {
            InputType.TYPE_CLASS_NUMBER -> mInputText.inputType = InputType.TYPE_CLASS_NUMBER
            InputType.TYPE_CLASS_TEXT -> mInputText.inputType = InputType.TYPE_CLASS_TEXT
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> mInputText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            val heightSize = DIMain.getABResources().getDimenPixelSize(R.dimen.input_element_default_height)

            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY))
    }

    fun setText(text: String) {
        mInputText.setText(text)
    }

    fun getText(): String = mInputText.text.toString()

    fun clearText() {
        mInputText.setText("")
    }

    fun setInputText(text: String) {
        mInputText.setText(text)
    }

    fun getInputText(): String? = mInputText.text.toString()

    fun getEditText() = mInputText

    fun getCancelImageView() = mCancelIcon

    fun setHint(hint: CharSequence) {
        mInputText.hint = hint
    }

    fun getHint() = mInputText.hint

    fun setTextColor(color: Int) {
        mInputText.setTextColor(color)
    }

    fun getTextColor() = mInputText.currentTextColor

    fun setHintColor(color: Int) {
        mInputText.setHintTextColor(color)
    }

    fun getHintColor() = mInputText.currentHintTextColor

    fun setTextSize(size: Float) {
        mInputText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun getTextSize() = mInputText.textSize

    fun setImeOption(option: Int) {
        mInputText.imeOptions = option
    }

    fun addTextChangeListener(watcher: TextWatcher?) {
        mInputText.addTextChangedListener(watcher)
    }

    fun removeTextChangeListener(watcher: TextWatcher?) {
        mInputText.removeTextChangedListener(watcher)
    }

    fun setError(error: String?) {
        mInputText.error = error
    }

    fun getError() = mInputText.error


    fun setIconDrawable(drawable: Drawable) {
        mIcon.setImageDrawable(drawable)
        mHasIcon = true
    }

    fun getIconDrawable() = mIcon.drawable

    fun setCancelIcon(drawable: Drawable) {
        mCancelIcon.setImageDrawable(drawable)
    }

    fun getCancelIcon() = mCancelIcon.drawable

    fun setSelectedBackground(selectedBackground: Drawable) {
        mSelectedBackground = selectedBackground
    }

    fun getSelectedBackground() = mSelectedBackground

    fun setUnSelectedBackground(unSelectedBackground: Drawable) {
        mUnSelectedBackground = unSelectedBackground
        if (background == null) {
            background = mUnSelectedBackground
        }
    }

    fun getUnselectedBackground() = mUnSelectedBackground

    fun setTypeFace(typeFace: Typeface) {
        mInputText.typeface = typeFace
    }

    fun hasDescription(has: Boolean) {
        mHasDescription = has
        mDescriptionText.visibility = View.VISIBLE
    }

    fun setDescription(description: CharSequence) {
        mDescriptionText.visibility = View.VISIBLE
        mDescriptionText.setTextColor(getHintColor())
        mDescriptionText.text = description
    }

    fun setDescriptionSize(size: Float) {
        mDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setIconVisibility(visibility: Int) {
        mIcon.visibility = visibility
    }

    fun makeFocus() {
        mInputText.isFocusableInTouchMode = true
        mInputText.requestFocus()
    }

}