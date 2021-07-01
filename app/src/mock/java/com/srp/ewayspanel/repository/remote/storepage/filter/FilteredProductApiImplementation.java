package com.srp.ewayspanel.repository.remote.storepage.filter;

import android.os.Handler;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.eways.repository.remote.BaseApiImplementation;

/**
 * Created by Eskafi on 10/28/2019.
 */
public class FilteredProductApiImplementation extends BaseApiImplementation implements FilteredProductApiService {

    private static FilteredProductApiImplementation sInstance;

    public static FilteredProductApiImplementation getInstance() {
        if (sInstance == null) {
            sInstance = new FilteredProductApiImplementation();
        }
        return sInstance;
    }

    @Override
    public void getFilteredProductList(FilterProductRequest request, final CallBackWrapper<FilteredProduct> callBack) {
        if (handleCall(callBack))
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(createSuccessResultFilteredProduct());
            }
        }, getResponseDelay());
    }

    @Override
    public void getProductDetail(long productId, CallBackWrapper<ProductDetailModel> callBack) {
        //TODO
    }

    @Override
    public void searchProducts(FilterProductRequest request, final CallBackWrapper<FilteredProduct> filteredProductCallBackWrapper) {
        if (handleCall(filteredProductCallBackWrapper))
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                filteredProductCallBackWrapper.onSuccess(createSuccessResultFilteredProduct());
            }
        }, getResponseDelay());
    }

    private String getResponseJson() {
        return "{\n" +
                "  \"Text\": null,\n" +
                "  \"Category\": {\n" +
                "    \"CategoryId\": 1398,\n" +
                "    \"Title\": \"فلش مموری (ظرفیت)\",\n" +
                "    \"SeoTitle\": \"فلش-مموری-(ظرفیت)\",\n" +
                "    \"ProductTags\": \"1104,1108,1112,1119,1122,1126,1127,1130,1134,1138,1140,1142,1145,1148,1153,1157,1158,1163,1167,1170,1174,1782,2560,2619,2632,2674,2843,3116,3286,4047,4496,1106,1109,1113,1120,1123,1128,1131,1135,1139,1141,1143,1149,1154,1159,1164,1168,1171,1175,1783,2081,2561,1949,2633,2673,3117,3287,4048,4498,4622,1107,1110,1121,1124,1129,1132,1136,1144,1147,1150,1155,1160,1165,1169,1172,1846,2562,1947,2634,2844,3059,3118,3288,4049,2458,4499,4623,5025,1111,1133,1137,1151,1156,1166,1173,1176,2082,1984,2457,1948,2845,3289,3369,3876,4500,4624,1834,3068,2617,3067,3290,3903,4837,4869,\",\n" +
                "    \"ProductTagList\": [\n" +
                "      1104,\n" +
                "      1108,\n" +
                "      1112,\n" +
                "      1119,\n" +
                "      1122,\n" +
                "      1126,\n" +
                "      1127,\n" +
                "      1130,\n" +
                "      1134,\n" +
                "      1138,\n" +
                "      1140,\n" +
                "      1142,\n" +
                "      1145,\n" +
                "      1148,\n" +
                "      1153,\n" +
                "      1157,\n" +
                "      1158,\n" +
                "      1163,\n" +
                "      1167,\n" +
                "      1170,\n" +
                "      1174,\n" +
                "      1782,\n" +
                "      2560,\n" +
                "      2619,\n" +
                "      2632,\n" +
                "      2674,\n" +
                "      2843,\n" +
                "      3116,\n" +
                "      3286,\n" +
                "      4047,\n" +
                "      4496,\n" +
                "      1106,\n" +
                "      1109,\n" +
                "      1113,\n" +
                "      1120,\n" +
                "      1123,\n" +
                "      1128,\n" +
                "      1131,\n" +
                "      1135,\n" +
                "      1139,\n" +
                "      1141,\n" +
                "      1143,\n" +
                "      1149,\n" +
                "      1154,\n" +
                "      1159,\n" +
                "      1164,\n" +
                "      1168,\n" +
                "      1171,\n" +
                "      1175,\n" +
                "      1783,\n" +
                "      2081,\n" +
                "      2561,\n" +
                "      1949,\n" +
                "      2633,\n" +
                "      2673,\n" +
                "      3117,\n" +
                "      3287,\n" +
                "      4048,\n" +
                "      4498,\n" +
                "      4622,\n" +
                "      1107,\n" +
                "      1110,\n" +
                "      1121,\n" +
                "      1124,\n" +
                "      1129,\n" +
                "      1132,\n" +
                "      1136,\n" +
                "      1144,\n" +
                "      1147,\n" +
                "      1150,\n" +
                "      1155,\n" +
                "      1160,\n" +
                "      1165,\n" +
                "      1169,\n" +
                "      1172,\n" +
                "      1846,\n" +
                "      2562,\n" +
                "      1947,\n" +
                "      2634,\n" +
                "      2844,\n" +
                "      3059,\n" +
                "      3118,\n" +
                "      3288,\n" +
                "      4049,\n" +
                "      2458,\n" +
                "      4499,\n" +
                "      4623,\n" +
                "      5025,\n" +
                "      1111,\n" +
                "      1133,\n" +
                "      1137,\n" +
                "      1151,\n" +
                "      1156,\n" +
                "      1166,\n" +
                "      1173,\n" +
                "      1176,\n" +
                "      2082,\n" +
                "      1984,\n" +
                "      2457,\n" +
                "      1948,\n" +
                "      2845,\n" +
                "      3289,\n" +
                "      3369,\n" +
                "      3876,\n" +
                "      4500,\n" +
                "      4624,\n" +
                "      1834,\n" +
                "      3068,\n" +
                "      2617,\n" +
                "      3067,\n" +
                "      3290,\n" +
                "      3903,\n" +
                "      4837,\n" +
                "      4869\n" +
                "    ],\n" +
                "    \"ParentId\": 1394,\n" +
                "    \"ParentIDTitle\": null,\n" +
                "    \"ChildGroupCount\": 6,\n" +
                "    \"ProductCount\": 7,\n" +
                "    \"hasChildren\": true,\n" +
                "    \"IsVisible\": true,\n" +
                "    \"Status\": 1,\n" +
                "    \"StatusIsVisible\": true,\n" +
                "    \"Priority\": 1,\n" +
                "    \"CategoryPicture\": null,\n" +
                "    \"BaseProductTag\": null,\n" +
                "    \"ChannelType\": 0,\n" +
                "    \"InStock\": null,\n" +
                "    \"Resources\": [],\n" +
                "    \"DefaultResourceJson\": \"[{\\\"Id\\\":0,\\\"CategoryId\\\":null,\\\"Culture\\\":\\\"Fa\\\",\\\"Title\\\":null,\\\"CreateDate\\\":null},{\\\"Id\\\":0,\\\"CategoryId\\\":null,\\\"Culture\\\":\\\"En\\\",\\\"Title\\\":null,\\\"CreateDate\\\":null}]\",\n" +
                "    \"ResourceJson\": \"[]\"\n" +
                "  },\n" +
                "  \"Products\": [],\n" +
                "  \"GoodsCount\": 0,\n" +
                "  \"CatId\": 1398,\n" +
                "  \"Order\": 0,\n" +
                "  \"Sort\": 0,\n" +
                "  \"PageIndex\": 0,\n" +
                "  \"OnlyAvailable\": true,\n" +
                "  \"MinPrice\": 0,\n" +
                "  \"MaxPrice\": 0,\n" +
                "  \"Categories\": null,\n" +
                "  \"Brands\": null,\n" +
                "  \"SelectedBrand\": [\n" +
                "    0\n" +
                "  ],\n" +
                "  \"Status\": 0,\n" +
                "  \"Description\": \"\"\n" +
                "}";
    }

    private FilteredProduct createSuccessResultFilteredProduct() {
        return DI.getGson().fromJson(getResponseJson(),FilteredProduct.class);
    }
}
