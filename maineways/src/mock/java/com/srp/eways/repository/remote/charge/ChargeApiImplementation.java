package com.srp.eways.repository.remote.charge;

import android.os.Handler;

import com.google.gson.Gson;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.ChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.charge.result.IrancellSpecialItemOffer;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.BaseApiImplementation;

import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;

public class ChargeApiImplementation extends BaseApiImplementation implements com.srp.eways.repository.remote.charge.ChargeApiService {

    private static ChargeApiImplementation sInstance;

    public static ChargeApiImplementation getInstance() {
        if (sInstance == null) {
            sInstance = new ChargeApiImplementation();
        }

        return sInstance;
    }

    private ChargeResult mChargeResult;

    private BuyChargeResult mBuyChargeResult;

    @Override
    public void getChargeData(final CallBackWrapper<ChargeResult> callBack) {
        if (handleCall(callBack)) {
            return;
        }

        mChargeResult = new Gson().fromJson(getMockChargeString(), ChargeResult.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(mChargeResult);
            }
        }, getResponseDelay());
    }

    private String getMockChargeString() {
        return mChargeResultString;
    }

    public ChargeResult getChargeResult() {
        return mChargeResult;
    }

    @Override
    public void buyCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyChargeResult> callBack) {
        if (handleCall(callBack)) {
            return;
        }

        handleBuyChargeCallInternally(buyChargeRequest, callBack);
    }

    @Override
    public void getTopInquiries(CallBackWrapper<TopInquiriesResult> callBack) {

        if(handleCall(callBack)){
            return;
        }
        handleGetTopInquiries(callBack);
    }

    @Override
    public void buyCashCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyCashChargeResult> callBack) {
        //todo
    }

    @Override
    public void getBankList(CallBackWrapper<BankListResponse> callBack) {

    }

    private void handleGetTopInquiries(final CallBackWrapper<TopInquiriesResult> callBack) {

        String data = "{\n" +
                "    \"Items\": [\n" +
                "        {\n" +
                "            \"DeliverStatus\": 2,\n" +
                "            \"CustomerMobile\": \"09356130201\",\n" +
                "            \"SaleUnitPrice\": 5450.0,\n" +
                "            \"RequestType\": 40,\n" +
                "            \"RequestId\": \"7151bf3f-68f9-4e34-90e0-9661a5dc426b\",\n" +
                "            \"RequestDate\": \"2020-01-12T10:21:58.9673526\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"DeliverStatus\": 2,\n" +
                "            \"CustomerMobile\": \"09356130201\",\n" +
                "            \"SaleUnitPrice\": 5450.0,\n" +
                "            \"RequestType\": 40,\n" +
                "            \"RequestId\": \"b59f1016-7f69-4123-9b50-0eb956909597\",\n" +
                "            \"RequestDate\": \"2020-01-12T10:21:23.264522\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"Operators\": [\n" +
                "        {\n" +
                "            \"OperatorKey\": \"MTN\",\n" +
                "            \"PerfixList\": [\n" +
                "                \"093\",\n" +
                "                \"090\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"OperatorKey\": \"MTNTD\",\n" +
                "            \"PerfixList\": [\n" +
                "                \"094\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"OperatorKey\": \"MCI\",\n" +
                "            \"PerfixList\": [\n" +
                "                \"091\",\n" +
                "                \"0990\",\n" +
                "                \"0991\",\n" +
                "                \"0992\",\n" +
                "                \"0994\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"OperatorKey\": \"RIGHTELPR\",\n" +
                "            \"PerfixList\": [\n" +
                "                \"0921\",\n" +
                "                \"0922\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"OperatorKey\": \"RIGHTELPO\",\n" +
                "            \"PerfixList\": [\n" +
                "                \"0920\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"Status\": 0,\n" +
                "    \"Description\": null\n" +
                "}";
        final TopInquiriesResult mockData = new Gson().fromJson(data, TopInquiriesResult.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(mockData);
            }
        }, getResponseDelay());
    }

    private void handleBuyChargeCallInternally(BuyChargeRequest buyChargeRequest, final CallBackWrapper<BuyChargeResult> callBack) {
        String ReqId = "mock requestId 0";
        long CustomerId = 0;
        String phoneNumber = buyChargeRequest.getNumber();
        long Debt = 0;
        long Price = buyChargeRequest.getAmount();
        long PaidPrice = buyChargeRequest.getPaidAmount();
        long Credit = 0;

        mBuyChargeResult = BuyChargeResult.getMoCkBuyChargeResult(ReqId, CustomerId, phoneNumber, Debt, Price, PaidPrice, Credit);
        mBuyChargeResult.setStatus(SUCCESS);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(mBuyChargeResult);
            }
        }, getResponseDelay());
    }

    public BuyChargeResult getBuyChargeResult() {
        return mBuyChargeResult;
    }

    @Override
    public void getIrancellSpecialOffers(String phoneNumber, final CallBackWrapper<IrancellSpecialOffersResult> callBack) {
        final IrancellSpecialOffersResult result;
        result = new IrancellSpecialOffersResult();
        result.setStatus(SUCCESS);
        result.setItems(IrancellSpecialItemOffer.getMockInstances(10));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        }, getResponseDelay());
    }

    private String mChargeResultString = "{\n" +
            "    \"Items\": [\n" +
            "        {\n" +
            "            \"OperatorKey\": \"MTN\",\n" +
            "            \"PName\": \"??????????????\",\n" +
            "            \"EName\": \"??????????????\",\n" +
            "            \"IsDisabled\": false,\n" +
            "            \"PerfixList\": [\n" +
            "                \"093\",\n" +
            "                \"090\"\n" +
            "            ],\n" +
            "            \"HaveTransport\": true,\n" +
            "            \"InternetProductId\": 33,\n" +
            "            \"Items\": [\n" +
            "                {\n" +
            "                    \"PName\": \"???????? ????????????\",\n" +
            "                    \"EName\": \"TopUp\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 0,\n" +
            "                    \"Childs\": [\n" +
            "                        {\n" +
            "                            \"ProductId\": 40,\n" +
            "                            \"PName\": \"???????? ????????????\",\n" +
            "                            \"EName\": \"Topup\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.9,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": false,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"9175\": \"9,175 (10,000)\",\n" +
            "                                \"18349\": \"18,349 (20,000)\",\n" +
            "                                \"45872\": \"45,872 (50,000)\",\n" +
            "                                \"91744\": \"91,744 (100,000)\",\n" +
            "                                \"183487\": \"183,487 (200,000)\"\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"ProductId\": 41,\n" +
            "                            \"PName\": \"???????? ???????? ??????????\",\n" +
            "                            \"EName\": \"Wonderful Topup Charge\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.9,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": true,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"10000\": \"10,000\",\n" +
            "                                \"20000\": \"20,000\",\n" +
            "                                \"50000\": \"50,000\",\n" +
            "                                \"60000\": \"60,000\",\n" +
            "                                \"100000\": \"100,000\",\n" +
            "                                \"200000\": \"200,000\"\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"ProductId\": 44,\n" +
            "                            \"PName\": \"?????? (????????) ??????????\",\n" +
            "                            \"EName\": \"Topup Postpaid\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.0,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": false,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"10000\": \"10,000\",\n" +
            "                                \"20000\": \"20,000\",\n" +
            "                                \"50000\": \"50,000\",\n" +
            "                                \"60000\": \"60,000\",\n" +
            "                                \"100000\": \"100,000\",\n" +
            "                                \"200000\": \"200,000\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"SubTypes\": null,\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                },\n" +
            "                {\n" +
            "                    \"PName\": \"???????? ??????????????\",\n" +
            "                    \"EName\": \"Internet Package\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 2,\n" +
            "                    \"Childs\": null,\n" +
            "                    \"SubTypes\": null,\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                },\n" +
            "\t\t\t\t{\n" +
            "                    \"PName\": \"?????????????? ????????\",\n" +
            "                    \"EName\": \"?????????????? ????????\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": \"GetMtnSpecialOffer\",\n" +
            "                    \"ChildType\": 3,\n" +
            "                    \"Childs\": [\n" +
            "                        {\n" +
            "                            \"ProductId\": 32,\n" +
            "                            \"Cofficient\": 1.0,\n" +
            "                            \"Tax\": 0.9\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"SubTypes\": null,\n" +
            "                    \"Description\": \"???? ?????????? ?????? 9% ???????? ???????????? ?????????? ??????????\",\n" +
            "                    \"Title\": \"?????? ???????? ??????????????\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "\t\t{\n" +
            "            \"OperatorKey\": \"MTNTD\",\n" +
            "            \"PName\": \"??????????????\",\n" +
            "            \"EName\": \"??????????????\",\n" +
            "            \"IsDisabled\": false,\n" +
            "            \"PerfixList\": [\n" +
            "                \"094\"\n" +
            "            ],\n" +
            "            \"HaveTransport\": false,\n" +
            "            \"InternetProductId\": 33,\n" +
            "            \"Items\": []\n" +
            "\t\t},\n" +
            "        {\n" +
            "            \"OperatorKey\": \"MCI\",\n" +
            "            \"PName\": \"?????????? ??????\",\n" +
            "            \"EName\": \"?????????? ??????\",\n" +
            "            \"IsDisabled\": false,\n" +
            "            \"PerfixList\": [\n" +
            "                \"091\",\n" +
            "                \"0990\",\n" +
            "                \"0991\",\n" +
            "                \"0992\",\n" +
            "                \"0994\"\n" +
            "            ],\n" +
            "            \"HaveTransport\": true,\n" +
            "            \"InternetProductId\": 184,\n" +
            "            \"Items\": [\n" +
            "\t\t\t\t{\n" +
            "                    \"PName\": \"???????? ????????????\",\n" +
            "                    \"EName\": \"Hamrahe Aval\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 0,\n" +
            "                    \"Childs\": [\n" +
            "                        {\n" +
            "                            \"ProductId\": 50,\n" +
            "                            \"PName\": \"???????? ????????????\",\n" +
            "                            \"EName\": \"Hamrahe Aval\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.0,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": false,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"10000\": \"10,000\",\n" +
            "                                \"20000\": \"20,000\",\n" +
            "                                \"50000\": \"50,000\",\n" +
            "                                \"60000\": \"60,000\",\n" +
            "                                \"100000\": \"100,000\",\n" +
            "                                \"200000\": \"200,000\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"SubTypes\": null,\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                },\n" +
            "\t\t\t\t{\n" +
            "                    \"PName\": \"???????? ?? ??????????????\",\n" +
            "                    \"EName\": \"Topup Interent Package\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 2,\n" +
            "                    \"Childs\": null,\n" +
            "                    \"SubTypes\":null,\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                }\n" +
            "\t\t\t]\n" +
            "        },\n" +
            "\t\t{\n" +
            "            \"OperatorKey\": \"RIGHTELPR\",\n" +
            "            \"PName\": \"?????????? ??????????????\",\n" +
            "            \"EName\": \"?????????? ??????????????\",\n" +
            "            \"IsDisabled\": false,\n" +
            "            \"PerfixList\": [\n" +
            "                \"0921\",\n" +
            "                \"0922\"\n" +
            "            ],\n" +
            "            \"HaveTransport\": true,\n" +
            "            \"InternetProductId\": 62,\n" +
            "            \"Items\": [\n" +
            "                {\n" +
            "                    \"PName\": \"???????? ????????????\",\n" +
            "                    \"EName\": \"Rightel\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 0,\n" +
            "                    \"Childs\": [\n" +
            "                        {\n" +
            "                            \"ProductId\": 60,\n" +
            "                            \"PName\": \"???????? ????????????\",\n" +
            "                            \"EName\": \"Topup\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.0,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": false,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"10000\": \"10,000\",\n" +
            "                                \"20000\": \"20,000\",\n" +
            "                                \"50000\": \"50,000\",\n" +
            "                                \"60000\": \"60,000\",\n" +
            "                                \"100000\": \"100,000\",\n" +
            "                                \"200000\": \"200,000\"\n" +
            "                            }\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"ProductId\": 61,\n" +
            "                            \"PName\": \"???????? ????????????????\",\n" +
            "                            \"EName\": \"Wonderful Topup\",\n" +
            "                            \"Cofficient\": 0.985,\n" +
            "                            \"Tax\": 0.0,\n" +
            "                            \"HaveAmount\": true,\n" +
            "                            \"HaveFixAmount\": true,\n" +
            "                            \"AmountList\": {\n" +
            "                                \"20000\": \"20,000\",\n" +
            "                                \"50000\": \"50,000\",\n" +
            "                                \"100000\": \"100,000\",\n" +
            "                                \"200000\": \"200,000\",\n" +
            "                                \"500000\": \"500,000\",\n" +
            "                                \"1000000\": \"1,000,000\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"SubTypes\": null,\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                }\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "            \"OperatorKey\": \"RIGHTELPO\",\n" +
            "            \"PName\": \"?????????? ??????????\",\n" +
            "            \"EName\": \"?????????? ??????????\",\n" +
            "            \"IsDisabled\": false,\n" +
            "            \"PerfixList\": [\n" +
            "                \"0920\"\n" +
            "            ],\n" +
            "            \"HaveTransport\": true,\n" +
            "            \"InternetProductId\": 62,\n" +
            "            \"Items\": [\n" +
            "                {\n" +
            "                    \"PName\": \"???????? ??????????????\",\n" +
            "                    \"EName\": \"Internet Package\",\n" +
            "                    \"IsDisabled\": false,\n" +
            "                    \"OnClick\": null,\n" +
            "                    \"ChildType\": 2,\n" +
            "                    \"Childs\": null,\n" +
            "                    \"SubTypes\": [\n" +
            "                        {\n" +
            "                            \"PName\": \"??????????\",\n" +
            "                            \"EName\": \"??????????\",\n" +
            "                            \"IsDisabled\": false,\n" +
            "                            \"OnClick\": null,\n" +
            "                            \"ChildType\": 1,\n" +
            "                            \"Childs\": [\n" +
            "                                {\n" +
            "                                    \"Id\": 2,\n" +
            "                                    \"Cofficient\": 0.985,\n" +
            "                                    \"Tax\": 0.0,\n" +
            "                                    \"PackageMasterList\": [\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 54,\n" +
            "                                            \"PName\": \"????????????\",\n" +
            "                                            \"EName\": \"????????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 10,\n" +
            "                                            \"PName\": \"6????????\",\n" +
            "                                            \"EName\": \"6????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 9,\n" +
            "                                            \"PName\": \"3????????\",\n" +
            "                                            \"EName\": \"3????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 3,\n" +
            "                                            \"PName\": \"????????????\",\n" +
            "                                            \"EName\": \"????????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 11,\n" +
            "                                            \"PName\": \"15????????\",\n" +
            "                                            \"EName\": \"15????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 2,\n" +
            "                                            \"PName\": \"??????????\",\n" +
            "                                            \"EName\": \"??????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 12,\n" +
            "                                            \"PName\": \"3????????\",\n" +
            "                                            \"EName\": \"3????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 1,\n" +
            "                                            \"PName\": \"????????????\",\n" +
            "                                            \"EName\": \"????????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 95,\n" +
            "                                            \"PName\": \"???????? ?????? ??????\",\n" +
            "                                            \"EName\": \"???????? ?????? ??????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 0,\n" +
            "                                            \"Id\": 93,\n" +
            "                                            \"PName\": \"????????\",\n" +
            "                                            \"EName\": \"????????\",\n" +
            "                                            \"Price\": 0.0,\n" +
            "                                            \"PaidPrice\": 0.0\n" +
            "                                        }\n" +
            "                                    ],\n" +
            "                                    \"PackageItems\": [\n" +
            "                                        {\n" +
            "                                            \"Parent\": 54,\n" +
            "                                            \"Id\": 1729,\n" +
            "                                            \"PName\": \"365???????? (10??????) (566,800)\",\n" +
            "                                            \"EName\": \"365???????? (10??????) (566,800) (566,800)\",\n" +
            "                                            \"Price\": 520000.0,\n" +
            "                                            \"PaidPrice\": 566800.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 54,\n" +
            "                                            \"Id\": 1730,\n" +
            "                                            \"PName\": \"365???????? (36 ??????) +2000 ?????????? ???????? ???????? ???????? (1,079,100)\",\n" +
            "                                            \"EName\": \"365???????? (36 ??????) +2000 ?????????? ???????? ???????? ???????? (1,079,100) (1,079,100)\",\n" +
            "                                            \"Price\": 990000.0,\n" +
            "                                            \"PaidPrice\": 1079100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 54,\n" +
            "                                            \"Id\": 1731,\n" +
            "                                            \"PName\": \"365???????? (42 ??????) +2000 ?????????? ???????? ???????? ???????? (1,253,500)\",\n" +
            "                                            \"EName\": \"365???????? (42 ??????) +2000 ?????????? ???????? ???????? ???????? (1,253,500) (1,253,500)\",\n" +
            "                                            \"Price\": 1150000.0,\n" +
            "                                            \"PaidPrice\": 1253500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 54,\n" +
            "                                            \"Id\": 1732,\n" +
            "                                            \"PName\": \"365???????? (74 ??????) (1,689,500)\",\n" +
            "                                            \"EName\": \"365???????? (74 ??????) (1,689,500) (1,689,500)\",\n" +
            "                                            \"Price\": 1550000.0,\n" +
            "                                            \"PaidPrice\": 1689500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 54,\n" +
            "                                            \"Id\": 1733,\n" +
            "                                            \"PName\": \"365???????? (200 ??????) (4,349,100)\",\n" +
            "                                            \"EName\": \"365???????? (200 ??????) (4,349,100) (4,349,100)\",\n" +
            "                                            \"Price\": 3990000.0,\n" +
            "                                            \"PaidPrice\": 4349100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1722,\n" +
            "                                            \"PName\": \"180???????? (9+9 ??????) +1000 ?????????? ???????? ???????? ???????? (457,800)\",\n" +
            "                                            \"EName\": \"180???????? (9+9 ??????) +1000 ?????????? ???????? ???????? ???????? (457,800) (457,800)\",\n" +
            "                                            \"Price\": 420000.0,\n" +
            "                                            \"PaidPrice\": 457800.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1723,\n" +
            "                                            \"PName\": \"180???????? (12 ??????) +1000 ?????????? ???????? ???????? ???????? (534,100)\",\n" +
            "                                            \"EName\": \"180???????? (12 ??????) +1000 ?????????? ???????? ???????? ???????? (534,100) (534,100)\",\n" +
            "                                            \"Price\": 490000.0,\n" +
            "                                            \"PaidPrice\": 534100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1724,\n" +
            "                                            \"PName\": \"180 ???????? (18+18 ??????) (632,200)\",\n" +
            "                                            \"EName\": \"180 ???????? (18+18 ??????) (632,200) (632,200)\",\n" +
            "                                            \"Price\": 580000.0,\n" +
            "                                            \"PaidPrice\": 632200.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1725,\n" +
            "                                            \"PName\": \"180???????? (21+21 ??????) +1000 ?????????? ???????? ???????? ???????? (763,000)\",\n" +
            "                                            \"EName\": \"180???????? (21+21 ??????) +1000 ?????????? ???????? ???????? ???????? (763,000) (763,000)\",\n" +
            "                                            \"Price\": 700000.0,\n" +
            "                                            \"PaidPrice\": 763000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1726,\n" +
            "                                            \"PName\": \"180 ???????? (42+42 ??????) (1,079,100)\",\n" +
            "                                            \"EName\": \"180 ???????? (42+42 ??????) (1,079,100) (1,079,100)\",\n" +
            "                                            \"Price\": 990000.0,\n" +
            "                                            \"PaidPrice\": 1079100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1727,\n" +
            "                                            \"PName\": \"180 ???????? (60+60 ??????) (1,362,500)\",\n" +
            "                                            \"EName\": \"180 ???????? (60+60 ??????) (1,362,500) (1,362,500)\",\n" +
            "                                            \"Price\": 1250000.0,\n" +
            "                                            \"PaidPrice\": 1362500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 10,\n" +
            "                                            \"Id\": 1728,\n" +
            "                                            \"PName\": \"180???????? (100+100??????) (2,169,100)\",\n" +
            "                                            \"EName\": \"180???????? (100+100??????) (2,169,100) (2,169,100)\",\n" +
            "                                            \"Price\": 1990000.0,\n" +
            "                                            \"PaidPrice\": 2169100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 9,\n" +
            "                                            \"Id\": 1717,\n" +
            "                                            \"PName\": \"90 ???????? (4 ??????) (261,600)\",\n" +
            "                                            \"EName\": \"90 ???????? (4 ??????) (261,600) (261,600)\",\n" +
            "                                            \"Price\": 240000.0,\n" +
            "                                            \"PaidPrice\": 261600.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 9,\n" +
            "                                            \"Id\": 1718,\n" +
            "                                            \"PName\": \"90 ???????? (9+9 ??????) (348,800)\",\n" +
            "                                            \"EName\": \"90 ???????? (9+9 ??????) (348,800) (348,800)\",\n" +
            "                                            \"Price\": 320000.0,\n" +
            "                                            \"PaidPrice\": 348800.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 9,\n" +
            "                                            \"Id\": 1719,\n" +
            "                                            \"PName\": \"90 ???????? (15+15 ??????) (457,800)\",\n" +
            "                                            \"EName\": \"90 ???????? (15+15 ??????) (457,800) (457,800)\",\n" +
            "                                            \"Price\": 420000.0,\n" +
            "                                            \"PaidPrice\": 457800.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 9,\n" +
            "                                            \"Id\": 1720,\n" +
            "                                            \"PName\": \"90 ???????? (21+21 ??????) (555,900)\",\n" +
            "                                            \"EName\": \"90 ???????? (21+21 ??????) (555,900) (555,900)\",\n" +
            "                                            \"Price\": 510000.0,\n" +
            "                                            \"PaidPrice\": 555900.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 9,\n" +
            "                                            \"Id\": 1721,\n" +
            "                                            \"PName\": \"90 ???????? (36+36 ??????) (839,300)\",\n" +
            "                                            \"EName\": \"90 ???????? (36+36 ??????) (839,300) (839,300)\",\n" +
            "                                            \"Price\": 770000.0,\n" +
            "                                            \"PaidPrice\": 839300.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1701,\n" +
            "                                            \"PName\": \"30 ???????? ( 300 ????) (59,950)\",\n" +
            "                                            \"EName\": \"30 ???????? ( 300 ????) (59,950) (59,950)\",\n" +
            "                                            \"Price\": 55000.0,\n" +
            "                                            \"PaidPrice\": 59950.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1700,\n" +
            "                                            \"PName\": \"30 ???????? (200+200 ????) (59,950)\",\n" +
            "                                            \"EName\": \"30 ???????? (200+200 ????) (59,950) (59,950)\",\n" +
            "                                            \"Price\": 55000.0,\n" +
            "                                            \"PaidPrice\": 59950.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1715,\n" +
            "                                            \"PName\": \"30 ???????? (20??????)- ???????? ?????????? ?????? ???????? (76,300)\",\n" +
            "                                            \"EName\": \"30 ???????? (20??????)- ???????? ?????????? ?????? ???????? (76,300) (76,300)\",\n" +
            "                                            \"Price\": 70000.0,\n" +
            "                                            \"PaidPrice\": 76300.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1716,\n" +
            "                                            \"PName\": \"30 ???????? (30??????)- ???????? ?????????? ?????? ???????? (109,000)\",\n" +
            "                                            \"EName\": \"30 ???????? (30??????)- ???????? ?????????? ?????? ???????? (109,000) (109,000)\",\n" +
            "                                            \"Price\": 100000.0,\n" +
            "                                            \"PaidPrice\": 109000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1702,\n" +
            "                                            \"PName\": \"30 ???????? (1+2 ??????) (114,450)\",\n" +
            "                                            \"EName\": \"30 ???????? (1+2 ??????) (114,450) (114,450)\",\n" +
            "                                            \"Price\": 105000.0,\n" +
            "                                            \"PaidPrice\": 114450.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1704,\n" +
            "                                            \"PName\": \"30 ???????? (1.5 ?????? +5000 ?????????? ???????? ???????? ????????) (136,250)\",\n" +
            "                                            \"EName\": \"30 ???????? (1.5 ?????? +5000 ?????????? ???????? ???????? ????????) (136,250) (136,250)\",\n" +
            "                                            \"Price\": 125000.0,\n" +
            "                                            \"PaidPrice\": 136250.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1703,\n" +
            "                                            \"PName\": \"30 ???????? (1.5+3 ??????) (136,250)\",\n" +
            "                                            \"EName\": \"30 ???????? (1.5+3 ??????) (136,250) (136,250)\",\n" +
            "                                            \"Price\": 125000.0,\n" +
            "                                            \"PaidPrice\": 136250.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1705,\n" +
            "                                            \"PName\": \"30 ???????? (2.5+5 ??????) (163,500)\",\n" +
            "                                            \"EName\": \"30 ???????? (2.5+5 ??????) (163,500) (163,500)\",\n" +
            "                                            \"Price\": 150000.0,\n" +
            "                                            \"PaidPrice\": 163500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1706,\n" +
            "                                            \"PName\": \"30 ???????? (3+6 ??????) (173,310)\",\n" +
            "                                            \"EName\": \"30 ???????? (3+6 ??????) (173,310) (173,310)\",\n" +
            "                                            \"Price\": 159000.0,\n" +
            "                                            \"PaidPrice\": 173310.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1707,\n" +
            "                                            \"PName\": \"30 ???????? (3.5 ??????) (185,300)\",\n" +
            "                                            \"EName\": \"30 ???????? (3.5 ??????) (185,300) (185,300)\",\n" +
            "                                            \"Price\": 170000.0,\n" +
            "                                            \"PaidPrice\": 185300.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1708,\n" +
            "                                            \"PName\": \"30 ???????? (4.5+9 ??????) (206,010)\",\n" +
            "                                            \"EName\": \"30 ???????? (4.5+9 ??????) (206,010) (206,010)\",\n" +
            "                                            \"Price\": 189000.0,\n" +
            "                                            \"PaidPrice\": 206010.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1709,\n" +
            "                                            \"PName\": \"30 ???????? (7+14 ??????) (260,510)\",\n" +
            "                                            \"EName\": \"30 ???????? (7+14 ??????) (260,510) (260,510)\",\n" +
            "                                            \"Price\": 239000.0,\n" +
            "                                            \"PaidPrice\": 260510.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1710,\n" +
            "                                            \"PName\": \"30 ???????? (8??????) (282,310)\",\n" +
            "                                            \"EName\": \"30 ???????? (8??????) (282,310) (282,310)\",\n" +
            "                                            \"Price\": 259000.0,\n" +
            "                                            \"PaidPrice\": 282310.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1711,\n" +
            "                                            \"PName\": \"30 ???????? (10+10 ??????) (321,550)\",\n" +
            "                                            \"EName\": \"30 ???????? (10+10 ??????) (321,550) (321,550)\",\n" +
            "                                            \"Price\": 295000.0,\n" +
            "                                            \"PaidPrice\": 321550.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1712,\n" +
            "                                            \"PName\": \"30 ???????? (12+24 ??????) (354,250)\",\n" +
            "                                            \"EName\": \"30 ???????? (12+24 ??????) (354,250) (354,250)\",\n" +
            "                                            \"Price\": 325000.0,\n" +
            "                                            \"PaidPrice\": 354250.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1713,\n" +
            "                                            \"PName\": \"30 ???????? (24+48 ??????) (531,920)\",\n" +
            "                                            \"EName\": \"30 ???????? (24+48 ??????) (531,920) (531,920)\",\n" +
            "                                            \"Price\": 488000.0,\n" +
            "                                            \"PaidPrice\": 531920.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 3,\n" +
            "                                            \"Id\": 1714,\n" +
            "                                            \"PName\": \"30 ???????? (50 ??????) (861,100)\",\n" +
            "                                            \"EName\": \"30 ???????? (50 ??????) (861,100) (861,100)\",\n" +
            "                                            \"Price\": 790000.0,\n" +
            "                                            \"PaidPrice\": 861100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1694,\n" +
            "                                            \"PName\": \"15 ???????? (200 ????) (47,960)\",\n" +
            "                                            \"EName\": \"15 ???????? (200 ????) (47,960) (47,960)\",\n" +
            "                                            \"Price\": 44000.0,\n" +
            "                                            \"PaidPrice\": 47960.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1695,\n" +
            "                                            \"PName\": \"15???????? (12 ??????) ???????? ?????????? 6 ???? 12 ?????? (54,500)\",\n" +
            "                                            \"EName\": \"15???????? (12 ??????) ???????? ?????????? 6 ???? 12 ?????? (54,500) (54,500)\",\n" +
            "                                            \"Price\": 50000.0,\n" +
            "                                            \"PaidPrice\": 54500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1696,\n" +
            "                                            \"PName\": \"15 ???????? (650 ????) (87,200)\",\n" +
            "                                            \"EName\": \"15 ???????? (650 ????) (87,200) (87,200)\",\n" +
            "                                            \"Price\": 80000.0,\n" +
            "                                            \"PaidPrice\": 87200.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1697,\n" +
            "                                            \"PName\": \"15 ???????? (1.5 ??????) (125,350)\",\n" +
            "                                            \"EName\": \"15 ???????? (1.5 ??????) (125,350) (125,350)\",\n" +
            "                                            \"Price\": 115000.0,\n" +
            "                                            \"PaidPrice\": 125350.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1699,\n" +
            "                                            \"PName\": \"15 ???????? ( 3 ??????) (152,600)\",\n" +
            "                                            \"EName\": \"15 ???????? ( 3 ??????) (152,600) (152,600)\",\n" +
            "                                            \"Price\": 140000.0,\n" +
            "                                            \"PaidPrice\": 152600.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 11,\n" +
            "                                            \"Id\": 1698,\n" +
            "                                            \"PName\": \"15 ???????? (5 ??????) (190,750)\",\n" +
            "                                            \"EName\": \"15 ???????? (5 ??????) (190,750) (190,750)\",\n" +
            "                                            \"Price\": 175000.0,\n" +
            "                                            \"PaidPrice\": 190750.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1684,\n" +
            "                                            \"PName\": \"7 ???????? (100 ????) (23,980)\",\n" +
            "                                            \"EName\": \"7 ???????? (100 ????) (23,980) (23,980)\",\n" +
            "                                            \"Price\": 22000.0,\n" +
            "                                            \"PaidPrice\": 23980.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1685,\n" +
            "                                            \"PName\": \"7 ???????? ( 150+150 ????) (35,970)\",\n" +
            "                                            \"EName\": \"7 ???????? ( 150+150 ????) (35,970) (35,970)\",\n" +
            "                                            \"Price\": 33000.0,\n" +
            "                                            \"PaidPrice\": 35970.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1686,\n" +
            "                                            \"PName\": \"7 ???????? (200 ????) (43,600)\",\n" +
            "                                            \"EName\": \"7 ???????? (200 ????) (43,600) (43,600)\",\n" +
            "                                            \"Price\": 40000.0,\n" +
            "                                            \"PaidPrice\": 43600.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1687,\n" +
            "                                            \"PName\": \"7 ???????? (300 ????) (53,410)\",\n" +
            "                                            \"EName\": \"7 ???????? (300 ????) (53,410) (53,410)\",\n" +
            "                                            \"Price\": 49000.0,\n" +
            "                                            \"PaidPrice\": 53410.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1688,\n" +
            "                                            \"PName\": \"7 ???????? (600????+1??????) (81,750)\",\n" +
            "                                            \"EName\": \"7 ???????? (600????+1??????) (81,750) (81,750)\",\n" +
            "                                            \"Price\": 75000.0,\n" +
            "                                            \"PaidPrice\": 81750.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1689,\n" +
            "                                            \"PName\": \"7 ???????? (700 ????) (87,200)\",\n" +
            "                                            \"EName\": \"7 ???????? (700 ????) (87,200) (87,200)\",\n" +
            "                                            \"Price\": 80000.0,\n" +
            "                                            \"PaidPrice\": 87200.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1690,\n" +
            "                                            \"PName\": \"7 ???????? (1.2+5??????) (109,000)\",\n" +
            "                                            \"EName\": \"7 ???????? (1.2+5??????) (109,000) (109,000)\",\n" +
            "                                            \"Price\": 100000.0,\n" +
            "                                            \"PaidPrice\": 109000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1691,\n" +
            "                                            \"PName\": \"7 ???????? (2.5+5 ??????) (136,250)\",\n" +
            "                                            \"EName\": \"7 ???????? (2.5+5 ??????) (136,250) (136,250)\",\n" +
            "                                            \"Price\": 125000.0,\n" +
            "                                            \"PaidPrice\": 136250.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1692,\n" +
            "                                            \"PName\": \"7 ???????? (3 ??????) (147,150)\",\n" +
            "                                            \"EName\": \"7 ???????? (3 ??????) (147,150) (147,150)\",\n" +
            "                                            \"Price\": 135000.0,\n" +
            "                                            \"PaidPrice\": 147150.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 2,\n" +
            "                                            \"Id\": 1693,\n" +
            "                                            \"PName\": \"7???????? (3.5+3.5??????) (163,500)\",\n" +
            "                                            \"EName\": \"7???????? (3.5+3.5??????) (163,500) (163,500)\",\n" +
            "                                            \"Price\": 150000.0,\n" +
            "                                            \"PaidPrice\": 163500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 12,\n" +
            "                                            \"Id\": 1678,\n" +
            "                                            \"PName\": \"3 ???????? (300+600 ????) (54,500)\",\n" +
            "                                            \"EName\": \"3 ???????? (300+600 ????) (54,500) (54,500)\",\n" +
            "                                            \"Price\": 50000.0,\n" +
            "                                            \"PaidPrice\": 54500.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 12,\n" +
            "                                            \"Id\": 1679,\n" +
            "                                            \"PName\": \"3 ???????? (350 ????) (56,680)\",\n" +
            "                                            \"EName\": \"3 ???????? (350 ????) (56,680) (56,680)\",\n" +
            "                                            \"Price\": 52000.0,\n" +
            "                                            \"PaidPrice\": 56680.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 12,\n" +
            "                                            \"Id\": 1681,\n" +
            "                                            \"PName\": \"3 ???????? (400+800 ????) (65,400)\",\n" +
            "                                            \"EName\": \"3 ???????? (400+800 ????) (65,400) (65,400)\",\n" +
            "                                            \"Price\": 60000.0,\n" +
            "                                            \"PaidPrice\": 65400.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 12,\n" +
            "                                            \"Id\": 1683,\n" +
            "                                            \"PName\": \"3 ???????? (3??????) (119,900)\",\n" +
            "                                            \"EName\": \"3 ???????? (3??????) (119,900) (119,900)\",\n" +
            "                                            \"Price\": 110000.0,\n" +
            "                                            \"PaidPrice\": 119900.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1670,\n" +
            "                                            \"PName\": \"1 ???????? (40 ????) (10,900)\",\n" +
            "                                            \"EName\": \"1 ???????? (40 ????) (10,900) (10,900)\",\n" +
            "                                            \"Price\": 10000.0,\n" +
            "                                            \"PaidPrice\": 10900.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1671,\n" +
            "                                            \"PName\": \"1 ???????? ( 100+100 ????) (21,800)\",\n" +
            "                                            \"EName\": \"1 ???????? ( 100+100 ????) (21,800) (21,800)\",\n" +
            "                                            \"Price\": 20000.0,\n" +
            "                                            \"PaidPrice\": 21800.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1672,\n" +
            "                                            \"PName\": \"1 ???????? (150 ????) (25,070)\",\n" +
            "                                            \"EName\": \"1 ???????? (150 ????) (25,070) (25,070)\",\n" +
            "                                            \"Price\": 23000.0,\n" +
            "                                            \"PaidPrice\": 25070.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1673,\n" +
            "                                            \"PName\": \"1 ???????? ( 200+300????) (32,700)\",\n" +
            "                                            \"EName\": \"1 ???????? ( 200+300????) (32,700) (32,700)\",\n" +
            "                                            \"Price\": 30000.0,\n" +
            "                                            \"PaidPrice\": 32700.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1674,\n" +
            "                                            \"PName\": \"1 ???????? (250 ????) (38,150)\",\n" +
            "                                            \"EName\": \"1 ???????? (250 ????) (38,150) (38,150)\",\n" +
            "                                            \"Price\": 35000.0,\n" +
            "                                            \"PaidPrice\": 38150.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1675,\n" +
            "                                            \"PName\": \"1 ???????? ( 300+700 ????) (42,510)\",\n" +
            "                                            \"EName\": \"1 ???????? ( 300+700 ????) (42,510) (42,510)\",\n" +
            "                                            \"Price\": 39000.0,\n" +
            "                                            \"PaidPrice\": 42510.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1676,\n" +
            "                                            \"PName\": \"1 ???????? (1.2??????) (68,670)\",\n" +
            "                                            \"EName\": \"1 ???????? (1.2??????) (68,670) (68,670)\",\n" +
            "                                            \"Price\": 63000.0,\n" +
            "                                            \"PaidPrice\": 68670.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 1,\n" +
            "                                            \"Id\": 1677,\n" +
            "                                            \"PName\": \"1 ???????? (3 ??????) (98,100)\",\n" +
            "                                            \"EName\": \"1 ???????? (3 ??????) (98,100) (98,100)\",\n" +
            "                                            \"Price\": 90000.0,\n" +
            "                                            \"PaidPrice\": 98100.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 95,\n" +
            "                                            \"Id\": 2742,\n" +
            "                                            \"PName\": \"?????? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? ) (120,000)\",\n" +
            "                                            \"EName\": \"?????? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? ) (120,000) (120,000)\",\n" +
            "                                            \"Price\": 110092.0,\n" +
            "                                            \"PaidPrice\": 120000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 95,\n" +
            "                                            \"Id\": 2743,\n" +
            "                                            \"PName\": \"?????? ???? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 130 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 130 ?????????? ???????? ???????? ) (150,000)\",\n" +
            "                                            \"EName\": \"?????? ???? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 130 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 130 ?????????? ???????? ???????? ) (150,000) (150,000)\",\n" +
            "                                            \"Price\": 137615.0,\n" +
            "                                            \"PaidPrice\": 150000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 95,\n" +
            "                                            \"Id\": 2744,\n" +
            "                                            \"PName\": \"?????? ???? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 130 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 130 ?????????? ???????? ???????? + 3GB ???????? + 3GB ???????? ?????????? ) (300,000)\",\n" +
            "                                            \"EName\": \"?????? ???? (???????????? : 10000 ?????????? ???????????? ???????? ???????? + 130 ?????????? ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 130 ?????????? ???????? ???????? + 3GB ???????? + 3GB ???????? ?????????? ) (300,000) (300,000)\",\n" +
            "                                            \"Price\": 275229.0,\n" +
            "                                            \"PaidPrice\": 300000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 95,\n" +
            "                                            \"Id\": 2747,\n" +
            "                                            \"PName\": \"?????? ?????????????? ( 10000 MIN ???????????? ???????? ???????? + 250 MIN ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 250 ?????????? ???????? ???????? + 5GB ???????? + 5GB ???????? ?????????? ) (400,000)\",\n" +
            "                                            \"EName\": \"?????? ?????????????? ( 10000 MIN ???????????? ???????? ???????? + 250 MIN ???????????? ???????? ???????? + 10000 ?????????? ???????? ???????? + 250 ?????????? ???????? ???????? + 5GB ???????? + 5GB ???????? ?????????? ) (400,000) (400,000)\",\n" +
            "                                            \"Price\": 366972.0,\n" +
            "                                            \"PaidPrice\": 400000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 93,\n" +
            "                                            \"Id\": 679,\n" +
            "                                            \"PName\": \"?????? ?????????????? (1??????) (76,300)\",\n" +
            "                                            \"EName\": \"?????? ?????????????? (1??????) (76,300) (76,300)\",\n" +
            "                                            \"Price\": 70000.0,\n" +
            "                                            \"PaidPrice\": 76300.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 93,\n" +
            "                                            \"Id\": 680,\n" +
            "                                            \"PName\": \"?????? ?????????????? (2??????) (109,000)\",\n" +
            "                                            \"EName\": \"?????? ?????????????? (2??????) (109,000) (109,000)\",\n" +
            "                                            \"Price\": 100000.0,\n" +
            "                                            \"PaidPrice\": 109000.0\n" +
            "                                        },\n" +
            "                                        {\n" +
            "                                            \"Parent\": 93,\n" +
            "                                            \"Id\": 681,\n" +
            "                                            \"PName\": \"?????? ?????????????? (5??????) (207,100)\",\n" +
            "                                            \"EName\": \"?????? ?????????????? (5??????) (207,100) (207,100)\",\n" +
            "                                            \"Price\": 190000.0,\n" +
            "                                            \"PaidPrice\": 207100.0\n" +
            "                                        }\n" +
            "                                    ]\n" +
            "                                }\n" +
            "                            ],\n" +
            "                            \"SubTypes\": null,\n" +
            "                            \"Description\": null,\n" +
            "                            \"Title\": null\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"Description\": null,\n" +
            "                    \"Title\": null\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"Status\": 0,\n" +
            "    \"Description\": null\n" +
            "}";

}
