package com.mantoo.yican;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.mantoo.yican.application.AppCache;
import com.mantoo.yican.application.PDAApplication;
import com.mantoo.yican.config.AppCacheKey;
import com.mantoo.yican.config.Constants;
import com.mantoo.yican.config.MainUrl;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class SuggestedFeedbackActivity extends BaseActivity  implements View.OnClickListener{

    private EditText Suggested_feedback_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_feedback);
        findViewById(R.id.suggested_feedback_submit_return).setOnClickListener(this);
        findViewById(R.id.suggested_feedback_commit).setOnClickListener(this);
        Suggested_feedback_content = (EditText)findViewById(R.id.suggested_feedback_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.suggested_feedback_submit_return:
                Intent intent = new Intent(SuggestedFeedbackActivity.this, PersionalInfoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.suggested_feedback_commit:
                String content = Suggested_feedback_content.getText().toString();
                //请求开始
                AjaxParams params = new AjaxParams();
                params.put("driverid", AppCache.getString(AppCacheKey.driverid));
                params.put("content", content);
                String url = MainUrl.URL + MainUrl.toSuggestedFeedback;
                System.out.println(params + url);
                showLoadingDialog();
                PDAApplication.http.configTimeout(8000);
                PDAApplication.http.post(url, params, new AjaxCallBack<Object>() {
                    public void onSuccess(Object t) {
                        hideLoadingDialog();
                        JSONObject object;
                        try {
                            object = new JSONObject(t.toString());
                            if (object.optInt(Constants.RESULT_CODE) == 0) {
                                Intent intent1 = new Intent(SuggestedFeedbackActivity.this, PersionalInfoActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };

                    public void onFailure(Throwable t, String strMsg) {
                        hideLoadingDialog();
                        showToastMsg(R.string.error_network);
                    }

                    ;
                });
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent2 = new Intent(SuggestedFeedbackActivity.this, PersionalInfoActivity.class);
            startActivity(intent2);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
