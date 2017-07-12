package esgi.com.newsapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import esgi.com.newsapp.activity.MainActivity;

/**
 * Created by Grunt on 12/07/2017.
 */

public abstract class RootFragment extends Fragment {
    private RootFragment.Interface anInterface;
    private boolean paused;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            anInterface = (RootFragment.Interface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RootFragment.Interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getTitle());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!hasMenuItem()) {
            menu.clear();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    protected MainActivity getMainActivity() {
        return anInterface.getMainActivity();
    }

    protected void pushFragment(RootStackFragment rootStackFragment) {
        anInterface.pushFragment(this, rootStackFragment);
    }

    public abstract String getTitle();

    public interface Interface {
        MainActivity getMainActivity();
        void pushFragment(RootFragment rootFragment, RootStackFragment rootStackFragment);
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean hasMenuItem() {
        return false;
    }

}
