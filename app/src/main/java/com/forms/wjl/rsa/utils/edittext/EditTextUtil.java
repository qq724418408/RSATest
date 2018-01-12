package com.forms.wjl.rsa.utils.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author john
 * @date 2018/1/11
 */
public class EditTextUtil {

    private My[] mMys;
    private FocusListener mListener;

    private EditTextUtil(FocusListener listener, final TextView btn, EditText... editTexts) {
        mListener = listener;
        if (mMys == null) {
            mMys = new My[editTexts.length];
        }
        for (int i = 0; i < editTexts.length; i++) {
            mMys[i] = new My();
        }
        for (int i = 0; i < editTexts.length; i++) {
            mMys[i].editText = editTexts[i];
            final int finalI = i;
            mMys[i].watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mMys[finalI].isFocus = s.length() != 0;
                    onChange(btn);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            mMys[i].editText.addTextChangedListener(mMys[i].watcher);

        }
    }

    private void onChange(TextView btn) {
        int endPosition = -1;
        for (int i = 0; i < mMys.length; i++) {
            if (mMys[i].isFocus) {
                endPosition = i;
            } else {
                break;
            }
        }

        if (mListener != null) {
            mListener.onFocus(endPosition == mMys.length - 1);
        } else {
            if (null != btn) {
                btn.getBackground().setAlpha((endPosition == mMys.length - 1) ? 255 : 128);
                btn.setEnabled(endPosition == mMys.length - 1);
            }
        }

    }

    public TextWatcher getTextWatcher(int position) {
        return mMys[position].watcher;
    }

    public static EditTextUtil create(FocusListener listener, EditText... editTexts) {
        return new EditTextUtil(listener, null, editTexts);
    }

    public static EditTextUtil create(TextView btn, EditText... editTexts) {
        if (null != btn) {
            btn.getBackground().setAlpha((128)); // 255不透明，0透明
            btn.setEnabled(false);
        }
        return new EditTextUtil(null, btn, editTexts);
    }

    class My {
        EditText editText;
        boolean isFocus;
        TextWatcher watcher;
    }
}
