package com.srp.ewayspanel.ui.login.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import static com.srp.ewayspanel.ui.login.otp.OtpActivity.OTP_INTENT_KEY;

/**
 * Created by ErfanG on 4/20/2020.
 */
public class OTPSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;

            if (bundle != null) {

                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        String sourceNumber = DI.getABResources().getString(R.string.otp_sms_resource_phonenumber);
                        String sourceNumber2 = DI.getABResources().getString(R.string.otp_sms_resource_phonenumber2);

                        String phoneNumber = msgs[i].getOriginatingAddress().replaceAll("\\s", "");

                        if (sourceNumber.equals(phoneNumber) || sourceNumber2.equals(phoneNumber)) {

                            Intent intentCall = new Intent(OtpActivity.OTP_INTENT_FILTER);
                            intentCall.putExtra(OTP_INTENT_KEY, msgs[i].getMessageBody().replaceAll("\\s", "").substring(15, 19));

                            LocalBroadcastManager.getInstance(context).sendBroadcast(intentCall);
                        }

                    }
                } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }


    }
}
