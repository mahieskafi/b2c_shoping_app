package com.srp.eways.model.charge.result;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.srp.eways.R;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class IrancellSpecialItemOffer implements IChargeChoice {

    private static final double TAX = 0.09;

    @Expose
    private String OfferCode;

    @Expose
    private String Description;

    @Expose
    private String DescriptionEn;

    @Expose
    private Long Amount;

    private int productId;

    private boolean isSelected = false;

    public static List<IrancellSpecialItemOffer> getMockInstances(int numOfMockOffers) {
        List<IrancellSpecialItemOffer> offers = new ArrayList<>();

        for (int i = 0; i < numOfMockOffers; ++i) {
            IrancellSpecialItemOffer mockItemOffer = new IrancellSpecialItemOffer();
            mockItemOffer.setAmount((long) (1000 + (i * 1000)));
            mockItemOffer.setDescription("مورد ویژه شماره " + i);
            mockItemOffer.setDescriptionEn("case number " + i);
            mockItemOffer.setOfferCode("" + i);
            mockItemOffer.setProductId(i);

            offers.add(mockItemOffer);
        }

        return offers;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getOfferCode() {
        return OfferCode;
    }

    public void setOfferCode(String offerCode) {
        OfferCode = offerCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescriptionEn() {
        return DescriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        DescriptionEn = descriptionEn;
    }

    public void setAmount(Long amount) {
        Amount = amount;
    }

    @Override
    public String getName() {
        return Description;
    }

    @Override
    public String getDescription(Context context) {
        return Description;
    }

    @Override
    public long getProductId() {
        return productId;
    }

    @Override
    public String getPackageId() {
        return OfferCode;
    }

    public long getAmount() {
        return Amount == null ? 0 : Amount;
    }

    @Override
    public long getPaidAmount() {
        return Amount == null ? 0 : Amount;
    }

    @Override
    public double getTax() {
        return TAX;
    }

    @Override
    public double getCoef() {
        return 0;
    }

    @Override
    public String getDisplayChargeAmount() {
        return null;
    }

    @Override
    public String getDisplayTaxAmount() {
        return null;
    }

    @Override
    public String getDisplaySum() {
        return null;
    }

    @Override
    public String getDisplayPaidAmount() {
        int paidAmount = (int) (Amount + (Amount * TAX));

        return Utils.toPersianPriceNumber(paidAmount);
    }

    @Override
    public int getProductType() {
        return 2; // Why ARJI? :D TopupTypeItem.TYPE_SPECIALOFFERS;
    }

    @Override
    public String toJsonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("productId", getProductId());
        jsonObject.addProperty("packageId", getPackageId());
        jsonObject.addProperty("amount", getAmount());
        jsonObject.addProperty("paidAmount", getPaidAmount());
        jsonObject.addProperty("productType", getProductType());
        jsonObject.addProperty("isUserInputChoice", isUserInputChoice());
        jsonObject.addProperty("isUserInputChoice", isUserInputChoice());

        return jsonObject.toString();
    }

    @Override
    public boolean isUserInputChoice() {
        return false;
    }

    @Override
    public int getChoiceTypeHintIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getChoiceTypeHintResId() {
        return R.string.specialoffers_type_hint;
    }

    @Override
    public int getChoiceTypeTitleIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getChoiceTypeTitleResId() {
        return R.string.specialoffers_type_title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_IRANCELL_SPECIALOFFER_CHOICE;
    }

    @Override
    public boolean isIrancell() {
        return false;
    }

    @Override
    public boolean isAddedToUserChoices() {
        return true;
    }

}
