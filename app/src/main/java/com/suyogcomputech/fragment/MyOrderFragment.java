package com.suyogcomputech.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.helper.OnlineShopping;
import com.suyogcomputech.sms.R;

import java.util.ArrayList;

/**
 * Created by Pintu on 7/27/2016.
 */
public class MyOrderFragment extends Fragment {
    ConnectionDetector detector;
    RecyclerView rcvMyOrder;
    ArrayList<OnlineShopping> list;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        detector=new ConnectionDetector(getActivity());
        rcvMyOrder=(RecyclerView)view.findViewById(R.id.rcvMyOrder);

        return view;
    }
}
