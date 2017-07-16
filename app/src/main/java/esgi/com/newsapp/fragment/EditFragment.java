package esgi.com.newsapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esgi.com.newsapp.R;
import esgi.com.newsapp.model.EditObject;
import esgi.com.newsapp.network.ApiResult;
import esgi.com.newsapp.network.RetrofitSession;

/**
 * Created by Grunt on 16/07/2017.
 */

public class EditFragment extends RootFragment {

    private String id;

    private static final int TAG_COMMENT = 1;
    private static final int TAG_TOPIC = 2;
    private static final int TAG_NEWS = 3;
    private static final int TAG_POST = 4;
    private int objectToEditType;

    @BindView(R.id.et_content)
    public EditText etContent;

    @BindView(R.id.et_title)
    public EditText etTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_res_edit, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        id = bundle.getString(getString(R.string.bundle_id));
        objectToEditType = bundle.getInt(getString(R.string.bundle_type));
        String content = bundle.getString(getString(R.string.bundle_content));
        String title = bundle.getString(getString(R.string.bundle_title));

        etContent.setText(content);
        etTitle.setText(title);

        return view;
    }

    private boolean verifyFields() {
        if (etTitle.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.fill_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etContent.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.fill_content, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private EditObject getEditObjectFromForm(){
        return new EditObject(etTitle.getText().toString(), etContent.getText().toString());
    }

    @OnClick(R.id.btn_edit)
    public void createTopic() {
        switch (objectToEditType) {
            case TAG_COMMENT:
                editComment();
                break;
            case TAG_TOPIC:
                editTopic();
                break;
            case TAG_NEWS:
                editNews();
                break;
            case TAG_POST:
                editPost();
                break;
        }

    }

    private void editTopic() {
        if (!verifyFields())
            return;
        final EditObject object = getEditObjectFromForm();
        RetrofitSession.getInstance().getTopicService().editTopic(id, object, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Log.d("EDIT TOPIC", "SUCCESS");
                editSuccess();
            }

            @Override
            public void error(int code, String message) {
                Log.d("EDIT TOPIC", "FAILED");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPost() {
        if (!verifyFields())
            return;
        final EditObject object = getEditObjectFromForm();
        RetrofitSession.getInstance().getPostService().editPost(id, object, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Log.d("EDIT POST", "SUCCESS");
                editSuccess();
            }

            @Override
            public void error(int code, String message) {
                Log.d("EDIT POST", "FAILED");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editComment() {
        if (!verifyFields())
            return;
        final EditObject object = getEditObjectFromForm();
        RetrofitSession.getInstance().getCommentService().editComment(id, object, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Log.d("EDIT COMMENT", "SUCCESS");
                editSuccess();
            }

            @Override
            public void error(int code, String message) {
                Log.d("EDIT COMMENT", "FAILED");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editNews() {
        if (!verifyFields())
            return;
        final EditObject object = getEditObjectFromForm();
        RetrofitSession.getInstance().getNewsService().editNews(id, object, new ApiResult<Void>() {
            @Override
            public void success(Void res) {
                Log.d("EDIT NEWS", "SUCCESS");
                editSuccess();
            }

            @Override
            public void error(int code, String message) {
                Log.d("EDIT NEWS", "FAILED");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editSuccess() {
        Toast.makeText(getContext(), getString(R.string.edited), Toast.LENGTH_SHORT).show();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStackImmediate();
    }

    @Override
    public String getTitle() {
        return getString(R.string.edition);
    }
}
