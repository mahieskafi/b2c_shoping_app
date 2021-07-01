package com.srp.ewayspanel.ui.shopcart.address;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.model.login.UserInfo;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.button.LeftIconButtonView;
import com.srp.eways.ui.view.dialog.BottomDialog;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.address.City;
import com.srp.ewayspanel.model.address.Province;
import com.srp.ewayspanel.model.login.Address;
import com.srp.ewayspanel.model.shopcart.PostType;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.shopcart.ShopCartModel;
import com.srp.ewayspanel.ui.login.AddressViewModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.view.DeliveryOptionsView;
import com.srp.ewayspanel.ui.view.shopcart.ReceiverDetailView;
import com.srp.ewayspanel.ui.view.store.basket.OrderSummaryView;
import com.srp.ewayspanel.ui.view.store.sort.SortRadioGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 1/1/2020.
 */
public class AddressFragment extends NavigationMemberFragment<ShopCartViewModel> {

    private OrderSummaryView mOrderSummaryView;
    private BottomDialog mAddressDialog;
    private ReceiverDetailView mReceiverDetailView;
    private AppCompatTextView mReceiverTitleTextView;
    private LeftIconButtonView mAddAddressButton;
    private CardView mDetailCardView;
    private CardView mDeliveryOptionCardView;
    private CardView mDescriptionCardView;
    private AppCompatEditText mDescription;

    private DeliveryOptionsView mDeliveryOptionView;
    private AppCompatTextView mDeliveryTitleTextView;

    private AddressViewModel mUserInfoViewModel;
    private ShopCartViewModel mShopCartViewModel;

    private AppCompatImageView mEditAddressIcon;
    private AppCompatImageView mDeleteAddressIcon;

    private AppCompatTextView mAddressTitleView;
    private ButtonElement mSelectAddressButtonView;

    ShopCartFragment.ShopCartNextLevelListener mNextLevelListener;

    private List<Address> mAddressList;
    private RadioOptionModel mSelectedAddress;
    private int mSelectedAddressIndex = 0;
    private int mClickedAddressIndex = 0;
    private PostType mSelectedPostType = null;
    private int mSelectedPostTypeIndex = 0;
    private int mSelectedPostTypeIndexBeforeEdit = 0;

    private Typeface mMediumTypeface;
    private int mAddressTitleTextSize;
    private int mAddressTitleTextColor;

    private int mSelectAddressTextColor;
    private String mSelectAddressText;
    private Drawable mSelectAddressEnabledBackground;
    private Drawable mSelectAddressDisabledBackground;

    private IABResources abResources;

    private boolean misEditCanceled;

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }


    private Observer<UserInfo> mUserInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo.getAddress() != null) {
                Address address = mUserInfoViewModel.getAddress(userInfo, abResources.getString(R.string.default_address_name));

                mUserInfoViewModel.insertAddress(address);

                mShopCartViewModel.setSelectedAddress(address);

//                List<Address> addresses = new ArrayList<>();
//                addresses.add(address);

//                setAddressList(addresses);

                mUserInfoViewModel.getUserInfoLiveData().removeObserver(mUserInfoObserver);
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoViewModel = DI.getViewModel(AddressViewModel.class);

        abResources = DI.getABResources();

        mOrderSummaryView = view.findViewById(R.id.view_order_summary);
        mAddAddressButton = view.findViewById(R.id.button_add_address);
        mReceiverDetailView = view.findViewById(R.id.view_receiver_detail);
        mReceiverTitleTextView = view.findViewById(R.id.txt_title_receiver_detail);
        mDetailCardView = view.findViewById(R.id.card_view);
        mDeliveryOptionCardView = view.findViewById(R.id.delivery_view_card);
        mDescriptionCardView = view.findViewById(R.id.description_card_view);

        mDeliveryOptionView = view.findViewById(R.id.delivery_view);
        mDeliveryTitleTextView = view.findViewById(R.id.txt_delivery_title);
        mEditAddressIcon = view.findViewById(R.id.iv_edit);
        mDeleteAddressIcon = view.findViewById(R.id.iv_delete);
        mAddressTitleView = view.findViewById(R.id.address_title);
        mSelectAddressButtonView = view.findViewById(R.id.btn_change_address);
        mDescription = view.findViewById(R.id.edit_description_value);

        mAddressDialog = new BottomDialog();

        mDescription.setHint(abResources.getString(R.string.description_box_hint));

        mMediumTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium);
        mAddressTitleTextSize = abResources.getDimenPixelSize(R.dimen.address_detail_address_title_text_size);
        mAddressTitleTextColor = abResources.getColor(R.color.address_detail_view_title_text_color);

        mSelectAddressTextColor = abResources.getColor(R.color.select_address_button_text_color);
        mSelectAddressText = abResources.getString(R.string.select_address_button_text);
        mSelectAddressEnabledBackground = abResources.getDrawable(R.drawable.button_select_address_enabled_backgound);
        mSelectAddressDisabledBackground = abResources.getDrawable(R.drawable.button_select_address_disabled_backgound);

        ViewCompat.setElevation(mOrderSummaryView, abResources.getDimenPixelSize(R.dimen.shop_card_address_order_summary_elevation));
        mOrderSummaryView.collapseView();

        mReceiverTitleTextView.setText(abResources.getString(R.string.address_detail_title_text));
        mReceiverTitleTextView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        mReceiverTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.address_detail_title_text_size));
        mReceiverTitleTextView.setTextColor(abResources.getColor(R.color.address_detail_view_title_text_color));

        mDeliveryTitleTextView.setText(abResources.getString(R.string.delivery_detail_title_text));
        mDeliveryTitleTextView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        mDeliveryTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.delivery_detail_title_text_size));
        mDeliveryTitleTextView.setTextColor(abResources.getColor(R.color.delivery_detail_view_title_text_color));

        mShopCartViewModel.getShopCartItem().observe(getViewLifecycleOwner(), new Observer<ShopCartModel>() {
            @Override
            public void onChanged(ShopCartModel shopCartModel) {
                if (shopCartModel != null) {
                    List<PostType> postTypes = shopCartModel.getDeliverTypes().getPostTypes();
                    if (postTypes.size() > 0) {
                        mDeliveryOptionView.setDeliveryOptions(postTypes);
                        mDeliveryOptionView.setEnable(true);

                        if (getViewModel().getSelectedPostType() == null) {
                            mSelectedPostTypeIndex = 0;
                            mSelectedPostType = postTypes.get(0);
                        } else {
                            mSelectedPostTypeIndex = getPostTypeIndex(postTypes);
                        }

                        mDeliveryOptionView.setPostTypeSelected(mSelectedPostTypeIndex);
                        getViewModel().setSelectedPostType(mSelectedPostType);

                        if (shopCartModel.getStatus() != NetworkResponseCodes.SUCCESS) {
                            setDeliveryEnable(true);
                            return;
                        }

                        getViewModel().getShippingPrice(mSelectedPostType.getId());
                    }
                }
            }
        });

        mDeliveryOptionView.setOnItemSelectedListener(new DeliveryOptionsView.Listener() {
            @Override
            public void onItemSelected(int index, PostType data) {
                mSelectedPostType = data;
                mSelectedPostTypeIndex = index;
                getViewModel().setSelectedPostType(data);
                getViewModel().getShippingPrice(mSelectedPostType.getId());
            }
        });

        getViewModel().isLoadingShippingPrice().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mOrderSummaryView.showLoading();
                    setDeliveryEnable(false);


                } else {
                    setDeliveryEnable(true);
                }
            }
        });

        final LoadingStateView.RetryListener retryListener = new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mOrderSummaryView.showLoading();
                if (mSelectedPostType != null) {
                    getViewModel().getShippingPrice(mSelectedPostType.getId());
                } else {
                    getViewModel().callGetShopCartList(null);
                }
            }
        };
        getViewModel().getShippingPriceError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    mOrderSummaryView.showResult(true, errorMessage, retryListener);

                    getViewModel().consumeShippingPriceError();
                }

            }
        });

        getViewModel().getShippingPrice().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long price) {
                if (price != null) {

                    setDeliveryEnable(true);

                    if (mSelectedPostType != null) {
                        mOrderSummaryView.showResult(false, "", retryListener);
                        ArrayList<Long> extractedPrices = getViewModel().calculatePrices(price, getViewModel().getShopCartProductList().getValue());

                        if (extractedPrices != null && extractedPrices.size() > 0) {
                            mOrderSummaryView.resetData();
                            mOrderSummaryView.setTotalRealPrice(extractedPrices.get(0));
                            mOrderSummaryView.setDeliveryPrice(mSelectedPostType.getTitle(), price);
                            mOrderSummaryView.setTotalDiscount(extractedPrices.get(2));
                            mOrderSummaryView.setTotalPrice(extractedPrices.get(1));
                            mOrderSummaryView.setTotalScore(extractedPrices.get(3));
                        }
                    } else {
                        mOrderSummaryView.showResult(true, abResources.getString(R.string.network_error_undefined), retryListener);

                        getViewModel().consumeShippingPriceError();
                    }
                }
            }
        });

        setupAddAddressButton();

        setupSelectAddressView();

        setupAddressDetailView();

        setupDeliveryOptionView();

        mUserInfoViewModel.getAllAddresses();
        mUserInfoViewModel.getAddressListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Address>>() {
            @Override
            public void onChanged(final List<Address> addressList) {
                if (addressList != null) {
                    if (addressList.size() == 0) {
                        mUserInfoViewModel.invalidateCredit();
                        mUserInfoViewModel.getCredit();

                        mUserInfoViewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), mUserInfoObserver);
                    } else {
                        mUserInfoViewModel.getUserInfoLiveData().removeObserver(mUserInfoObserver);
                        setAddressList(addressList);
                    }
                }
            }
        });

        mUserInfoViewModel.getInsertStatusLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long status) {
                if (status > -1) {
                    mUserInfoViewModel.getAllAddresses();
                }
            }
        });

        mShopCartViewModel.getShopCartProductList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShopCartItemModel>>() {
            @Override
            public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {

                ArrayList<Long> extractedPrices = mShopCartViewModel.calculatePrices(0, shopCartItemModels);

                if (extractedPrices != null && extractedPrices.size() > 0) {
                    mOrderSummaryView.resetData();
                    mOrderSummaryView.setTotalRealPrice(extractedPrices.get(0));
                    mOrderSummaryView.setTotalPrice(extractedPrices.get(1));
                    mOrderSummaryView.setTotalDiscount(extractedPrices.get(2));
                    mOrderSummaryView.setTotalScore(extractedPrices.get(3));
                }
            }
        });
        mOrderSummaryView.setOnNextLevelButtonAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          if (mNextLevelListener != null) {
                    mShopCartViewModel.setDescription(mDescription.getText().toString());
                    mNextLevelListener.onLevelChanged(ShopCartFragment.SHOP_CART_ADDRESS, ShopCartFragment.SHOP_CART_CONFIRMATION);
                }
            }
        });
    }

    private int getPostTypeIndex(List<PostType> postTypes) {
        for (int i = 0; i < postTypes.size(); i++) {
            if (getViewModel().getSelectedPostType().equals(postTypes.get(i))) {
                return i;
            }
        }
        return 0;
    }

    private void setupAddAddressButton() {
        mAddAddressButton.setText(abResources.getString(R.string.add_address_view_add_button_text));
        mAddAddressButton.setTextSize(abResources.getDimenPixelSize(R.dimen.add_address_view_add_button_text_size));
        mAddAddressButton.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mAddAddressButton.setTextColor(abResources.getColor(R.color.add_address_view_add_button_text_color));
        mAddAddressButton.setEnabledBackground(abResources.getDrawable(R.drawable.button_add_address_background));
        mAddAddressButton.setEnable(true);
        mAddAddressButton.setIconNextLevel(abResources.getDrawable(R.drawable.shop_cart_add_address_icon));
        mAddAddressButton.setIconLeftMargin(abResources.getDimenPixelSize(R.dimen.add_address_view_icon_margin_left));
        mAddAddressButton.setTextMarginRight(abResources.getDimenPixelSize(R.dimen.add_address_view_add_button_text_right));
        mAddAddressButton.setVisibility(View.GONE);

        mAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddAddressFragment.Listener listener = new AddAddressFragment.Listener() {
                    @Override
                    public void onConfirmed(@Nullable Address address, @NotNull Province provinceId, @NotNull City cityId) {
                        //todo should send province and city for buy
                        if (address != null) {
                            mUserInfoViewModel.insertAddress(address);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onCanceled() {

                    }
                };

                AddAddressFragment addAddressFragment = AddAddressFragment.Companion.newInstance();
                addAddressFragment.setListener(listener);

                openFragment(addAddressFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
            }
        });

    }

    private void setupSelectAddressView() {

        mAddressTitleView.setTypeface(mMediumTypeface);
        mAddressTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAddressTitleTextSize);
        mAddressTitleView.setTextColor(mAddressTitleTextColor);

        mSelectAddressButtonView.setTextColor(mSelectAddressTextColor);
        mSelectAddressButtonView.setTextSize(mAddressTitleTextSize);
        mSelectAddressButtonView.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mSelectAddressButtonView.setEnabledBackground(mSelectAddressEnabledBackground);
        mSelectAddressButtonView.setDisableBackground(mSelectAddressDisabledBackground);
        mSelectAddressButtonView.setEnable(true);
        mSelectAddressButtonView.setText(mSelectAddressText);
        mSelectAddressButtonView.setVisibility(View.GONE);
        mSelectAddressButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddressDialog.setTitle(mSelectAddressText);
                mAddressDialog.setButtonEnable(false);

                mAddressDialog.show(getFragmentManager(), "");
            }
        });

    }

    private void setupAddressDialog() {
        List<RadioOptionModel> radioOptionModels = new ArrayList<>();
        for (Address address : mAddressList) {
            radioOptionModels.add(new RadioOptionModel(address.getAddressName(), false, address));
        }

        final SortRadioGroup mOneColumnRadioGroup = new SortRadioGroup(getContext());
        mOneColumnRadioGroup.setColumnCount(1);
        mOneColumnRadioGroup.setData(radioOptionModels);
        mOneColumnRadioGroup.setSelectedRadioButton(mSelectedAddressIndex);
        mOneColumnRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                mSelectedAddress = data;
                mClickedAddressIndex = index;

                if (mClickedAddressIndex == 0) {
                    mDeleteAddressIcon.setVisibility(View.GONE);
                } else {
                    mDeleteAddressIcon.setVisibility(View.VISIBLE);
                }

                mOneColumnRadioGroup.setSelectedRadioButton(index);
                mAddressDialog.setButtonEnable(true);
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {

            }
        });


        mAddressDialog.setChildContentView(mOneColumnRadioGroup);
        mAddressDialog.setContentViewMargin(abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_left),
                abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_top),
                abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_right),
                abResources.getDimenPixelSize(R.dimen.dialog_select_address_margin_bottom));

        mAddressDialog.setListener(new BottomDialog.ConfirmationBottomDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                mAddressDialog.dismiss();

                Address address = (Address) mSelectedAddress.option;

                setAddress(address);

                mShopCartViewModel.setSelectedAddress(address);

                mSelectedAddressIndex = mClickedAddressIndex;
            }

            @Override
            public void onCancelClicked() {
                mAddressDialog.dismiss();
            }
        });
    }

    private void setupAddressDetailView() {
        int detailCardPadding = abResources.getDimenPixelSize(R.dimen.address_detail_card_padding);

        mDetailCardView.setPadding(detailCardPadding, detailCardPadding, detailCardPadding, detailCardPadding);
        mDetailCardView.setBackground(abResources.getDrawable(R.drawable.address_detail_card_backgound));
        mDetailCardView.setRadius(abResources.getDimenPixelSize(R.dimen.address_detail_card_radius));

        mEditAddressIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_edit_address));
        mDeleteAddressIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_delete_address));

        mEditAddressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClicked();
            }
        });

        mDeleteAddressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClicked();
            }
        });
    }

    private void setupDeliveryOptionView() {
        mDeliveryOptionCardView.setBackground(abResources.getDrawable(R.drawable.address_detail_card_backgound));
        mDeliveryOptionCardView.setRadius(abResources.getDimenPixelSize(R.dimen.delivery_detail_background_radius));

        mDescriptionCardView.setBackground(abResources.getDrawable(R.drawable.address_detail_card_backgound));
        mDescriptionCardView.setRadius(abResources.getDimenPixelSize(R.dimen.delivery_detail_background_radius));

        mDescriptionCardView.setCardBackgroundColor(abResources.getColor(R.color.shopcart_detail_address_card_back_color));
        mDescriptionCardView.setCardElevation(abResources.getDimenPixelSize(R.dimen.delivery_detail_background_elevation));
    }

    private void setAddress(Address address) {

        mShopCartViewModel.setSelectedAddress(address);
        mShopCartViewModel.callGetShopCartList(mShopCartViewModel.getDiscountCode());
        mAddressTitleView.setText(address.getAddressName());

        mReceiverDetailView.setName(address.getFullName());
        mReceiverDetailView.setPhone(address.getPhoneNumber());
        mReceiverDetailView.setMobile(address.getMobile());
        mReceiverDetailView.setState(address.getStateName());
        mReceiverDetailView.setCity(address.getCityName());
        mReceiverDetailView.setPostCode(address.getPostCode());
        mReceiverDetailView.setAddress(address.getAddress());
    }

    private void setAddressList(List<Address> addressList) {
        if (addressList != null && mAddressList == null || mAddressList.size() == 0) {
            Address firstAddress = addressList.get(0);
            mSelectedAddress = new RadioOptionModel(firstAddress.getAddressName(), true, firstAddress);
            mSelectedAddressIndex = 0;

            mDeleteAddressIcon.setVisibility(View.GONE);

            setAddress(firstAddress);
        }

        mAddressList = new ArrayList<>();
        mAddressList.addAll(addressList);

        setupAddressDialog();
    }

    @Override
    public ShopCartViewModel acquireViewModel() {
        mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);
        return mShopCartViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address;
    }

    public void onEditClicked() {

        AddAddressFragment.Listener listener = new AddAddressFragment.Listener() {
            @Override
            public void onConfirmed(@Nullable Address address, Province province, City city) {
                if (address != null) {
                    address.setUserId(mUserInfoViewModel.getUserInfoLiveData().getValue().getUserId());
                    address.setStateId(province.getProvinceId());
                    address.setCityId(city.getCityId());
                    mUserInfoViewModel.updateAddress(address);

                    onBackPressed();
                    setAddress(address);
                    mShopCartViewModel.setSelectedProvinceAndCity(province.getProvinceId(), city.getCityId());

                    mDeliveryOptionView.setEnable(false);
                    mOrderSummaryView.showLoading();
                    mShopCartViewModel.callGetShopCartList(mShopCartViewModel.getDiscountCode());
                }
            }

            @Override
            public void onCanceled() {
            }
        };
        AddAddressFragment addAddressFragment = AddAddressFragment.Companion.newInstance();
        addAddressFragment.setListener(listener);
        mSelectedPostTypeIndexBeforeEdit = mSelectedAddressIndex;

        if (mShopCartViewModel.getSelectedAddress() != null) {
            addAddressFragment.setAddress(mShopCartViewModel.getSelectedAddress());
        }

        openFragment(addAddressFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
    }

    public void onDeleteClicked() {
        if (mSelectedAddress.option != null) {
            mUserInfoViewModel.deleteAddress(((Address) mSelectedAddress.option).getAddressId());

            if (mSelectedAddressIndex == mAddressList.size() - 1) {
                mSelectedAddressIndex = 0;
                setAddress(mAddressList.get(0));
            } else {
                mSelectedAddressIndex += 1;
                setAddress(mAddressList.get(mSelectedAddressIndex));
            }
        }

    }

    public void setNextLevelAction(@NotNull ShopCartFragment.ShopCartNextLevelListener nextLevelListener) {
        mNextLevelListener = nextLevelListener;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

//        if (!hidden) {
//            if (mShopCartViewModel.getIsEditAddress().getValue() != null
//                    && mShopCartViewModel.getIsEditAddress().getValue()) {
//                onEditClicked();
//                mShopCartViewModel.consumeIsEditAddress();
//            }
//        }
    }

    private void setDeliveryEnable(boolean isEnable) {

        mDeliveryOptionView.setEnable(isEnable);

        if (isEnable) {
            mDeliveryTitleTextView.setTextColor(abResources.getColor(R.color.delivery_detail_view_title_text_color));

            mDeliveryOptionCardView.setCardBackgroundColor(abResources.getColor(R.color.shopcart_detail_address_card_back_color));
            mDeliveryOptionCardView.setCardElevation(abResources.getDimenPixelSize(R.dimen.delivery_detail_background_elevation));
            //            setupDeliveryOptionView();
        } else {
            mDeliveryTitleTextView.setTextColor(abResources.getColor(R.color.delivery_detail_view_title_text_disabled_color));

            mDeliveryOptionCardView.setCardBackgroundColor(abResources.getColor(R.color.shopcart_detail_address_card_back_disabled_color));
            mDeliveryOptionCardView.setCardElevation(0);
            //            setupDeliveryOptionView();
        }

    }
}
