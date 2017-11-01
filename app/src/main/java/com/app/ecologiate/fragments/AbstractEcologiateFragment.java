package com.app.ecologiate.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.app.ecologiate.R;
import com.app.ecologiate.services.UserManager;

/**
 * Created by ldzisiuk on 23/10/17.
 */

public abstract class AbstractEcologiateFragment extends Fragment {

    abstract public String getTitle();
    abstract public String getSubTitle();

    public String getFragmentTag(){
        return this.getClass().getCanonicalName();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(this.getTitle());
        if(this.getSubTitle() != null && !("".equals(this.getSubTitle()))) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(this.getSubTitle());
        }else{
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }
}
