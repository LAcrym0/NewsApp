package esgi.com.newsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esgi.com.newsapp.R;

/**
 * Created by Grunt on 12/07/2017.
 */

public abstract class RootStackFragment extends RootFragment {

    private Intent result;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().overridePendingTransition(R.anim.transition_in_enter, R.anim.transition_in_exit);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
        view.setBackgroundColor(getResources().getColor(R.color.background));
    }

    public void pushFragmentForResult(RootStackFragment newFragment, int requestCode) {
        newFragment.setTargetFragment(this, requestCode);
        pushFragment(newFragment);
    }

    public void finish() {
        getMainActivity().finishFragment(this);
    }


    public void onFragmentResult(int requestCode, Intent data) {}

    public boolean consumeBackPressed() {
        return false;
    }

    public Intent getResult() {
        return result;
    }

    public void setResult(Intent result) {
        this.result = result;
    }

    public void finishWithResult(Intent result) {
        setResult(result);
        finish();
    }
}
