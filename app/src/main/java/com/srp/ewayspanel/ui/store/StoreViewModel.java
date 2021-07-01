package com.srp.ewayspanel.ui.store;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse;
import com.srp.ewayspanel.repository.storepage.StorePageRepository;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ErfanG on 22/10/2019.
 */
public class StoreViewModel extends BaseViewModel {

    public interface CopyCategoryTreeListener {

        void onCopied(StorePageRepository.CategoryData categoryData, CategoryTreeNode copy);

    }

    private StorePageRepository mStorePageRepo;

    private MutableLiveData<CategoryListResponse> mCategoryDataLive = new MutableLiveData<>();

    private StorePageRepository.CategoryData mCategoryData;
    private MutableLiveData<StorePageRepository.CategoryData> mCategoryRawDataLive = new MutableLiveData<>();

    public StoreViewModel() {
        mStorePageRepo = DI.getStorePageRepo();
    }

    public void getCategoryList(long parentId) {
        if (parentId == 0) {
            setLoading(true);
        }

        mStorePageRepo.getCategoryList(parentId, new StorePageRepository.CategoryDataReadyListener() {
            @Override
            public void onCategoryDataReady(@NotNull CategoryListResponse categoryListResponse) {
                setLoading(false);

                if (categoryListResponse.getStatus() == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                    Boolean isNeededToLogin = isNeededToLogin().getValue();

                    if (isNeededToLogin != null && !isNeededToLogin) {
                        setIsNeededToLogin(true);
                        return;
                    }
                }
//                mCategoryData = categoryData;
                mCategoryDataLive.setValue(categoryListResponse);
            }
        });
    }

    public void getCategoryRawList() {
        setLoading(true);

        mStorePageRepo.getCategoryRawList( new StorePageRepository.CategoryRawDataReadyListener() {
            @Override
            public void onCategoryDataReady(@NotNull StorePageRepository.CategoryData categoryData) {
                setLoading(false);

                if (categoryData.status == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                    Boolean isNeededToLogin = isNeededToLogin().getValue();

                    if (isNeededToLogin != null && !isNeededToLogin) {
                        setIsNeededToLogin(true);
                        return;
                    }
                }
                mCategoryData = categoryData;
                mCategoryRawDataLive.setValue(categoryData);
            }
        });
    }

    public MutableLiveData<CategoryListResponse> getCategoryData() {
        return mCategoryDataLive;
    }

    public void setCategoryDataConsumed() {
        mCategoryDataLive.setValue(null);
    }

    public MutableLiveData<StorePageRepository.CategoryData> getCategoryRawData() {
        return mCategoryRawDataLive;
    }

    public void setCategoryRawDataConsumed() {
        mCategoryRawDataLive.setValue(null);
    }

    public void setLoadingCategoryDataConsumed() {
        isLoading().setValue(null);
    }

    public void copyCategoryTree(final CopyCategoryTreeListener callback) {

        setLoading(true);

        CategoryTreeNode.getCopy(new CategoryTreeNode.CopyCallback() {
            @Override
            public void onCopied(CategoryTreeNode copy) {
                setLoading(false);

                callback.onCopied(mCategoryData, copy);
            }
        });
    }

}
