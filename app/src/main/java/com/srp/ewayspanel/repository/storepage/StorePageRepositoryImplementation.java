package com.srp.ewayspanel.repository.storepage;

import android.os.Handler;
import android.os.Looper;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse;
import com.srp.ewayspanel.repository.remote.storepage.StorePageApiImplementation;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class StorePageRepositoryImplementation implements StorePageRepository {
    private static StorePageRepositoryImplementation sInstance;

    private StorePageApiImplementation mApi;

    private List<CategoryItem> mCategoryRawItems;
    private CategoryTreeNode mCategoryTreeCache;

    public static StorePageRepositoryImplementation getInstance() {
        if (sInstance == null) {
            sInstance = new StorePageRepositoryImplementation();
        }

        return sInstance;
    }

    private StorePageRepositoryImplementation() {
        mApi = DI.getStorePageApi();
    }

    @Override
    public void getCategoryList(final long parentId, @NotNull final CategoryDataReadyListener callback) {

        mApi.getCategoryList(parentId, new CallBackWrapper<CategoryListResponse>() {

            @Override
            public void onSuccess(final CategoryListResponse responseBody) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCategoryDataReady(responseBody);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                CategoryListResponse categoryData = new CategoryListResponse(errorMessage, null, errorCode);
                callback.onCategoryDataReady(categoryData);
            }

        });

    }

    @Override
    public void getCategoryRawList(final CategoryRawDataReadyListener callback) {
        mApi.getCategoryRawResponse(new CallBackWrapper<CategoryListResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                CategoryData categoryData = new CategoryData(null, null, errorCode, errorMessage);
                callback.onCategoryDataReady(categoryData);
            }

            @Override
            public void onSuccess(CategoryListResponse responseBody) {

                mCategoryRawItems = responseBody.getItems();
                mCategoryTreeCache = new CategoryTreeNode(responseBody.getItems());

                final CategoryData categoryData = new CategoryData(mCategoryRawItems, mCategoryTreeCache, NetworkResponseCodes.SUCCESS, null);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onCategoryDataReady(categoryData);
                    }
                });

            }
        });
    }


}
