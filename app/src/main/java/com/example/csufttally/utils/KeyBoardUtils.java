package com.example.csufttally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.csufttally.R;

public class KeyBoardUtils {
    private final Keyboard k1;  //自定义键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener{
        public void onEnsure();
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);
    }
    KeyboardView.OnKeyboardActionListener listener=new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
        }
        @Override
        public void onRelease(int primaryCode) {
        }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable=editText.getText();
            int start = editText.getSelectionStart();
            switch(primaryCode){
                case Keyboard.KEYCODE_DELETE: //删除
                   if(editable!=null&&editable.length()>0){
                       if(start>0){
                           editable.delete(start-1,start);
                       }
                }
                    break;
                case Keyboard.KEYCODE_DONE: //完成
                    onEnsureListener.onEnsure();
                    break;
                default:
                    editable.insert(start, Character.toString((char)primaryCode));
                    break;
            }
        }
        @Override
        public void onText(CharSequence text) {
        }
        @Override
        public void swipeLeft() {
        }
        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeDown() {
        }
        @Override
        public void swipeUp() {
        }
    };

    public void showKeyboard(){
        int visibility=keyboardView.getVisibility();
        if(visibility== View.INVISIBLE||visibility==View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
}
