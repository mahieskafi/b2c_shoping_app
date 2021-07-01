package com.srp.ewayspanel.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.shopcart.PostType;
import com.srp.eways.ui.charge.buy.RadioOptionModel;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOptionsView extends LinearLayout {

    public interface Listener {

        void onItemSelected(int index, PostType selectedPostType);

    }

    private NColumnRadioGroup mDeliveryOptionsView;
    private TextView mTextDescription;

    private Listener mListener;

    private List<PostType> mPostTypes;

    public DeliveryOptionsView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public DeliveryOptionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public DeliveryOptionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);

        Resources resources = getResources();

        mDeliveryOptionsView = new NColumnRadioGroup(context);
        mDeliveryOptionsView.setColumnCount(3);

        mTextDescription = new AppCompatTextView(context);
        mTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.deliveryoptions_selectedoption_description_text));
        mTextDescription.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( resources.getDimensionPixelSize(R.dimen.delivery_detail_title_text_margin_right),
                resources.getDimensionPixelSize(R.dimen.delivery_detail_title_text_margin_right),
                resources.getDimensionPixelSize(R.dimen.delivery_detail_title_text_margin_right),
                resources.getDimensionPixelSize(R.dimen.delivery_detail_title_text_margin_right));
        mTextDescription.setLayoutParams(layoutParams);

        Drawable descriptionBackground = resources.getDrawable(R.drawable.deliverymethods_description_background);
        mTextDescription.setBackground(descriptionBackground);
        mTextDescription.setTextColor(resources.getColor(R.color.deliveryoptions_selectedoption_description_text));


        addView(mDeliveryOptionsView);
        addView(mTextDescription);

    }

    public void setOnItemSelectedListener(final Listener listener) {
        mListener = listener;

        mDeliveryOptionsView.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                setPostTypeSelected(index);

                PostType selectedPostType = getPostTypeAt(index);
                listener.onItemSelected(index, selectedPostType);
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) { }
        });
    }

    public void setDeliveryOptions(List<PostType> postTypes) {
        mPostTypes = postTypes;

        mDeliveryOptionsView.setData(createDeliveryOptionsFromPostTypes(postTypes));

//        setPostTypeSelected(0);
    }

    public void setPostTypeSelected(int index) {
        mDeliveryOptionsView.setSelectedRadioButton(index);

        PostType selectedPostType = mPostTypes.get(index);
        String postTypeDescription = selectedPostType.getDescription();
        mListener.onItemSelected(index, selectedPostType);

        if (!TextUtils.isEmpty(postTypeDescription)) {
            mTextDescription.setVisibility(VISIBLE);
            mTextDescription.setText(postTypeDescription);
        }
        else {
            mTextDescription.setVisibility(GONE);
            mTextDescription.setText("");
        }
    }

    private List<RadioOptionModel> createDeliveryOptionsFromPostTypes(List<PostType> postTypes) {
        List<RadioOptionModel> radioOptionModels = new ArrayList<>(postTypes.size());

        for (PostType postType: postTypes) {
            radioOptionModels.add(new RadioOptionModel(postType.getTitle(), false, postType));
        }

        return radioOptionModels;
    }

    public PostType getPostTypeAt(int index) {
        return mPostTypes.get(index);
    }

    public void setEnable(boolean isEnable){

        Resources resources = getResources();

        setEnabled(isEnable);
        setClickable(isEnable);
        mDeliveryOptionsView.setEnable(isEnable);

        if(isEnable){
            Drawable descriptionBackground = resources.getDrawable(R.drawable.deliverymethods_description_background);
            mTextDescription.setBackground(descriptionBackground);
            mTextDescription.setTextColor(resources.getColor(R.color.deliveryoptions_selectedoption_description_text));

        }
        else{
            Drawable descriptionBackground = resources.getDrawable(R.drawable.deliverymethods_description_disabled_background);
            mTextDescription.setBackground(descriptionBackground);
            mTextDescription.setTextColor(resources.getColor(R.color.deliveryoptions_selectedoption_description_disabled_text));
        }

    }

}
