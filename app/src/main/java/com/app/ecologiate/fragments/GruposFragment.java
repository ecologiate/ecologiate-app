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
import com.app.ecologiate.models.GrupoAdapter;
import com.app.ecologiate.models.Grupos;
import com.app.ecologiate.models.Tip;
import com.app.ecologiate.models.TipAdapter;

import java.util.ArrayList;
import java.util.List;


public class GruposFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.grupoFragment);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        List<Grupos> myDataset = new ArrayList<>();
        myDataset.add(new Grupos("Proyecto", "Gonzalo ", "1000","50","20","100"));
        myDataset.add(new Grupos("lalala", "evi ", "1000","2000","4","5000"));
        mAdapter = new GrupoAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return  view;
    }


    public interface OnFragmentInteractionListener {
            void onFragmentInteraction(Uri uri);
    }
}
