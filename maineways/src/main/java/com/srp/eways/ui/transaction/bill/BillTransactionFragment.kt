package com.srp.eways.ui.transaction.bill


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.srp.eways.R

/**
 * A simple [Fragment] subclass.
 */
class BillTransactionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_transaction, container, false)
    }


}
