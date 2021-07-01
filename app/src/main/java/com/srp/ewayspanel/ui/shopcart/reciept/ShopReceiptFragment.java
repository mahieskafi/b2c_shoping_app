package com.srp.ewayspanel.ui.shopcart.reciept;

import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.ui.view.receipt.ReceiptListView;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse;
import com.srp.ewayspanel.model.shopcart.buy.Header;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;
import com.srp.ewayspanel.ui.transaction.order.detail.OrderTransactionDetailFragment;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.eways.ui.main.MainRootIds.ROOT_HOME;


public class ShopReceiptFragment extends NavigationMemberFragment<ShopCartViewModel> {

    private static final int STATUS_UNKNOWN = 9903;

    public static ShopReceiptFragment newInstance(BuyResponse buyResponse) {
        return new ShopReceiptFragment(buyResponse);
    }

    private ShopReceiptFragment(BuyResponse buyResponse){
        this.buyResponse = buyResponse;
    }

    private BuyResponse buyResponse;

    private WeiredToolbar mToolbar;
    private ButtonElement mDetailButton;
    private ButtonElement mMainPageButton;
    private LinearLayout mDetailContainer;
    private RelativeLayout mCreditContainer;
    private ReceiptListView mReceiptCredit;

    private ImageView mResultIcon;
    private AppCompatTextView mResultDescription;
    private ReceiptListView mResultReceipt;

    private int mCardMarginSides;
    private int mCardResultPaddingSides;

    private String mReceiptValueDescription;

    private String mCreditTitle;
    private String mOrderNumberTitle;
    private String mOrderPriceTitle;

    private FollowOrderItem mOrderItem;

    @Override
    public ShopCartViewModel acquireViewModel() {
        return DI.getViewModel(ShopCartViewModel.INSTANCE.getClass());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop_receipt;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = view.findViewById(R.id.toolbar);
        mDetailButton = view.findViewById(R.id.btn_detail);
        mMainPageButton = view.findViewById(R.id.btn_main_page);
        mDetailContainer = view.findViewById(R.id.card_view);
        mCreditContainer = view.findViewById(R.id.card_credit_view);
        mReceiptCredit = view.findViewById(R.id.receipt_credit);
        mResultIcon = view.findViewById(R.id.iv_result);
        mResultDescription = view.findViewById(R.id.tv_result_description);
        mResultReceipt = view.findViewById(R.id.result_receipt);

        final IABResources AB = DI.getABResources();

        mCardMarginSides = AB.getDimenPixelSize(R.dimen.shop_receipt_result_margin_side);
        mCardResultPaddingSides = AB.getDimenPixelSize(R.dimen.shop_receipt_result_card_padding_sides);
        int cardMarginTop = AB.getDimenPixelSize(R.dimen.shop_receipt_result_margin_top);

        mReceiptValueDescription = AB.getString(R.string.rial);

        mCreditTitle = AB.getString(R.string.shop_receipt_credit_title);
        mOrderNumberTitle = AB.getString(R.string.shop_receipt_order_number_title);
        mOrderPriceTitle = AB.getString(R.string.shop_receipt_order_price_title);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AB.getDimenPixelSize(R.dimen.shop_receipt_result_card_height));
        layoutParams.setMargins(mCardMarginSides, cardMarginTop, mCardMarginSides, 0);

        mDetailContainer.setLayoutParams(layoutParams);
        mDetailContainer.setBackground(AB.getDrawable(R.drawable.card_white_background));
        ViewCompat.setElevation(mDetailContainer, AB.getDimenPixelSize(R.dimen.card_elevation));

        setupToolbar();
        setupDetailButton();
        setupMainPageButton();
        setupCreditView(view);
        setupResultView();

        bindData(buyResponse);

        getViewModel().getBuyResponse().observe(this, new Observer<BuyResponse>() {
            @Override
            public void onChanged(BuyResponse buyResponse) {
                bindData(buyResponse);
            }
        });

        DI.getViewModel(UserInfoViewModel.class).getCreditLiveData().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long credit) {
                if (credit != null) {
                    mCreditContainer.setVisibility(View.VISIBLE);
                    mReceiptCredit.setReceiptItem(new ReceiptItem(mCreditTitle, Utils.toPersianPriceNumber(credit), mReceiptValueDescription, null));
                }
            }
        });
    }

    private void bindData(BuyResponse buyResponse){
        if (buyResponse != null) {

            final IABResources AB = DI.getABResources();

            int status = buyResponse.getStatus();

            long price = 0;
            String orderId;

            orderId = Utils.toPersianNumber(buyResponse.getOrderId());
            mMainPageButton.setVisibility(View.VISIBLE);

            if (status == NetworkResponseCodes.SUCCESS) {
                mResultIcon.setImageDrawable(AB.getDrawable(R.drawable.ic_receipt_success));
                mResultDescription.setText(AB.getString(R.string.shop_receipt_result_description_success_state));
            } else if (status == STATUS_UNKNOWN) {
                mResultIcon.setImageDrawable(AB.getDrawable(R.drawable.ic_receipt_unknown));
                mResultDescription.setText(AB.getString(R.string.shop_receipt_result_description_unknown_state));

                orderId = "ــــــ";
                mMainPageButton.setVisibility(View.GONE);
            } else {
                mResultIcon.setImageDrawable(AB.getDrawable(R.drawable.ic_receipt_failed));
                mResultDescription.setText(buyResponse.getDescription());
            }

            ArrayList<ReceiptItem> items = new ArrayList<>();

            items.add(new ReceiptItem(mOrderNumberTitle, orderId, null, null));
            if (buyResponse.getHeader() != null) {
                Header header = buyResponse.getHeader();
                price = header.getPrice() + header.getPostPrice() + header.getTaxPrice() - header.getDiscountPrice();
                items.add(new ReceiptItem(mOrderPriceTitle, Utils.toPersianPriceNumber(price), mReceiptValueDescription, null));
            }
            mResultReceipt.setReceiptItem(items);

            mOrderItem = new FollowOrderItem(buyResponse.getOrderId(), buyResponse.getHeader().getUserId(), price
                    , status, "", "", 0);

            getViewModel().consumeBuyResponseLiveData();
        }
    }

    private void setupDetailButton() {
        IABResources AB = DI.getABResources();

        mDetailButton.setText(AB.getString(R.string.shop_receipt_button_detail_title));
        mDetailButton.setTextSize(AB.getDimenPixelSize(R.dimen.shop_receipt_button_text_size));
        mDetailButton.setTextColor(AB.getColor(R.color.shop_receipt_button_detail_text_color));
        mDetailButton.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mDetailButton.setEnabledBackground(AB.getDrawable(R.drawable.button_background_enabled));
        mDetailButton.setDisableBackground(AB.getDrawable(R.drawable.button_background_disabled));
        mDetailButton.hasIcon(false);
        mDetailButton.setEnable(true);
        mDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(ROOT_HOME);

                OrderTransactionViewModel orderTransactionViewModel = DI.getViewModel(OrderTransactionViewModel.class);

                orderTransactionViewModel.setSelectedOrderTransaction(mOrderItem);
                orderTransactionViewModel.consumeOrderSummaryLiveData();
                orderTransactionViewModel.consumeOrderDetailLiveData();

                openFragment(OrderTransactionDetailFragment.newInstance(true));

            }
        });

    }

    private void setupMainPageButton() {
        IABResources AB = DI.getABResources();

        mMainPageButton.setText(AB.getString(R.string.shop_receipt_button_main_page_title));
        mMainPageButton.setTextSize(AB.getDimenPixelSize(R.dimen.shop_receipt_button_text_size));
        mMainPageButton.setTextColor(AB.getColor(R.color.shop_receipt_button_mainpage_text_color));
        mDetailButton.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mMainPageButton.setEnabledBackground(AB.getDrawable(R.drawable.button_main_page_shop_receipt));
        mMainPageButton.setDisableBackground(AB.getDrawable(R.drawable.button_main_page_shop_receipt));
        mMainPageButton.hasIcon(false);
        mMainPageButton.setEnable(true);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AB.getDimenPixelSize(R.dimen.shop_receipt_button_mainpage_width), AB.getDimenPixelSize(R.dimen.shop_receipt_button_mainpage_height));
        layoutParams.setMargins(0, 0, AB.getDimenPixelSize(R.dimen.shop_receipt_button_mainpage_margin_right), 0);

        mMainPageButton.setLayoutParams(layoutParams);
        mMainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(ROOT_HOME);
            }
        });

    }

    private void setupCreditView(View view) {
        IABResources AB = DI.getABResources();

        int cardCreditMarginTop = AB.getDimenPixelSize(R.dimen.shop_receipt_credit_card_margin_top);
        int cardCreditPadding = AB.getDimenPixelSize(R.dimen.shop_receipt_credit_card_padding);
        int receiptCreditMarginEnd = AB.getDimenPixelSize(R.dimen.shop_receipt_credit_margin_end);

        LinearLayout.LayoutParams creditCardLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AB.getDimenPixelSize(R.dimen.shop_receipt_credit_card_height));
        creditCardLayoutParams.setMargins(mCardMarginSides, cardCreditMarginTop, mCardMarginSides, 0);

        mCreditContainer.setLayoutParams(creditCardLayoutParams);

        mCreditContainer.setBackground(AB.getDrawable(R.drawable.card_white_background));
        ViewCompat.setElevation(mCreditContainer, AB.getDimenPixelSize(R.dimen.card_elevation));
        mCreditContainer.setPadding(0, 0, cardCreditPadding, 0);

        mReceiptCredit.setTextColor(AB.getColor(R.color.shop_receipt_text_color));
        mReceiptCredit.setItemViewLayoutId(R.layout.item_order_summary);
        mReceiptCredit.setTextSize(AB.getDimenPixelSize(R.dimen.shop_receipt_credit_text_size));
        mReceiptCredit.setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mReceiptCredit.setValueTextTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        mReceiptCredit.setValueDescrptionTextSize(AB.getDimen(R.dimen.shop_receipt_credit_value_description_text_size));
        mReceiptCredit.setMarginSides(receiptCreditMarginEnd);
        mReceiptCredit.setLayoutGravity(Gravity.CENTER_VERTICAL);

        mCreditContainer.setVisibility(View.GONE);
    }

    private void setupResultView() {

        IABResources AB = DI.getABResources();

        mResultDescription.setTextColor(AB.getColor(R.color.shop_receipt_text_color));
        mResultDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.shop_receipt_result_text_size));
        mResultDescription.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mResultDescription.setMinLines(2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(mCardResultPaddingSides, AB.getDimenPixelSize(R.dimen.shop_receipt_result_text_margin_top),
                mCardResultPaddingSides, 0);

        mResultDescription.setLayoutParams(layoutParams);

        mResultReceipt.setTextColor(AB.getColor(R.color.shop_receipt_text_color));
        mResultReceipt.setItemViewLayoutId(R.layout.item_order_summary);
        mResultReceipt.setTextSize(AB.getDimenPixelSize(R.dimen.shop_receipt_result_list_text_size));
        mResultReceipt.setTextTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mResultReceipt.setValueTextTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular));
        mResultReceipt.setValueDescrptionTextSize(AB.getDimen(R.dimen.shop_receipt_credit_value_description_text_size));
        mResultReceipt.setMarginSides(mCardResultPaddingSides);
        mResultReceipt.setItemsMargin(AB.getDimenPixelSize(R.dimen.shop_receipt_result_items_margin));

    }

    private void setupToolbar() {
        IABResources AB = DI.getABResources();

        mToolbar.setBackgroundColor(AB.getColor(R.color.toolbar_background));
        mToolbar.setTitle(AB.getString(R.string.shop_receipt_page_title));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.setTitleTextColor(AB.getColor(R.color.white));
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.title_toolbar_size));
        mToolbar.setShowDeposit(false);
        mToolbar.setShowNavigationDrawer(false);
        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
        mToolbar.setShowNavigationUp(false);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
