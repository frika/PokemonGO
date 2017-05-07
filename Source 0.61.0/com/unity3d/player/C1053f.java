package com.unity3d.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* renamed from: com.unity3d.player.f */
public final class C1053f extends Dialog implements TextWatcher, OnClickListener {
    private static int f686c;
    private static int f687d;
    private Context f688a;
    private UnityPlayer f689b;

    /* renamed from: com.unity3d.player.f.1 */
    class C10501 implements OnFocusChangeListener {
        final /* synthetic */ C1053f f683a;

        C10501(C1053f c1053f) {
            this.f683a = c1053f;
        }

        public final void onFocusChange(View view, boolean z) {
            if (z) {
                this.f683a.getWindow().setSoftInputMode(5);
            }
        }
    }

    /* renamed from: com.unity3d.player.f.2 */
    class C10512 extends EditText {
        final /* synthetic */ C1053f f684a;

        C10512(C1053f c1053f, Context context) {
            this.f684a = c1053f;
            super(context);
        }

        public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (i != 4) {
                return i != 84 ? super.onKeyPreIme(i, keyEvent) : true;
            } else {
                this.f684a.m555a(this.f684a.m551a(), true);
                return true;
            }
        }

        public final void onWindowFocusChanged(boolean z) {
            super.onWindowFocusChanged(z);
            if (z) {
                ((InputMethodManager) this.f684a.f688a.getSystemService("input_method")).showSoftInput(this, 0);
            }
        }
    }

    /* renamed from: com.unity3d.player.f.3 */
    class C10523 implements OnEditorActionListener {
        final /* synthetic */ C1053f f685a;

        C10523(C1053f c1053f) {
            this.f685a = c1053f;
        }

        public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6) {
                this.f685a.m555a(this.f685a.m551a(), false);
            }
            return false;
        }
    }

    static {
        f686c = 1627389952;
        f687d = -1;
    }

    public C1053f(Context context, UnityPlayer unityPlayer, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        super(context);
        this.f688a = null;
        this.f689b = null;
        this.f688a = context;
        this.f689b = unityPlayer;
        getWindow().setGravity(80);
        getWindow().requestFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(createSoftInputView());
        getWindow().setLayout(-1, -2);
        getWindow().clearFlags(2);
        EditText editText = (EditText) findViewById(1057292289);
        Button button = (Button) findViewById(1057292290);
        m553a(editText, str, i, z, z2, z3, str2);
        button.setOnClickListener(this);
        editText.setOnFocusChangeListener(new C10501(this));
    }

    private static int m550a(int i, boolean z, boolean z2, boolean z3) {
        int i2 = 0;
        int i3 = (z2 ? AccessibilityNodeInfoCompat.ACTION_SET_SELECTION : 0) | (z ? AccessibilityNodeInfoCompat.ACTION_PASTE : 0);
        if (z3) {
            i2 = AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        }
        i2 |= i3;
        return (i < 0 || i > 7) ? i2 : i2 | new int[]{1, 16385, 12290, 17, 2, 3, 97, 33}[i];
    }

    private String m551a() {
        EditText editText = (EditText) findViewById(1057292289);
        return editText == null ? null : editText.getText().toString().trim();
    }

    private void m553a(EditText editText, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        editText.setImeOptions(6);
        editText.setText(str);
        editText.setHint(str2);
        editText.setHintTextColor(f686c);
        editText.setInputType(C1053f.m550a(i, z, z2, z3));
        editText.addTextChangedListener(this);
        editText.setClickable(true);
        if (!z2) {
            editText.selectAll();
        }
    }

    private void m555a(String str, boolean z) {
        Selection.removeSelection(((EditText) findViewById(1057292289)).getEditableText());
        this.f689b.reportSoftInputStr(str, 1, z);
    }

    public final void m557a(String str) {
        EditText editText = (EditText) findViewById(1057292289);
        if (editText != null) {
            editText.setText(str);
            editText.setSelection(str.length());
        }
    }

    public final void afterTextChanged(Editable editable) {
        this.f689b.reportSoftInputStr(editable.toString(), 0, false);
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    protected final View createSoftInputView() {
        View relativeLayout = new RelativeLayout(this.f688a);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(f687d);
        View c10512 = new C10512(this, this.f688a);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(0, 1057292290);
        c10512.setLayoutParams(layoutParams);
        c10512.setId(1057292289);
        relativeLayout.addView(c10512);
        c10512 = new Button(this.f688a);
        c10512.setText(this.f688a.getResources().getIdentifier("ok", "string", "android"));
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(11);
        c10512.setLayoutParams(layoutParams);
        c10512.setId(1057292290);
        c10512.setBackgroundColor(0);
        relativeLayout.addView(c10512);
        ((EditText) relativeLayout.findViewById(1057292289)).setOnEditorActionListener(new C10523(this));
        relativeLayout.setPadding(16, 16, 16, 16);
        return relativeLayout;
    }

    public final void onBackPressed() {
        m555a(m551a(), true);
    }

    public final void onClick(View view) {
        m555a(m551a(), false);
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
