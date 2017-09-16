package com.app.ecologiate.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ecologiate.R;
import com.app.ecologiate.models.MyAdapter;
import com.app.ecologiate.models.MyModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TipsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.tipFragment);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //creo mis mails
        List<MyModel> myDataset = new ArrayList<MyModel>();

        myDataset.add(new MyModel("Cumpleaños", "Utiliza los rollos de papel higiénico para hacer ingeniosas decoraciones ", R.drawable.tip1));
        myDataset.add(new MyModel("Bolsas de Te", "Guarda tus bolsas de té usadas en el refrigerador y utilízalas a la mañana siguiente para ponerla sobre tus ojos y aliviar las bolsas debajo de ellos. ", R.drawable.tip2));
        myDataset.add(new MyModel("Individuales", "Tus jeans también podrían transformarse en atractivos individuales para proteger tu mesa.", R.drawable.tip3));
        myDataset.add(new MyModel("Neumaticos", "Puedes utilizar tus neumaticos para hacer tachos de basura.", R.drawable.tip4));
        myDataset.add(new MyModel("Disquete", "Utiliza tus disquetes como decorativas macetas.", R.drawable.tip5));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return  view;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
