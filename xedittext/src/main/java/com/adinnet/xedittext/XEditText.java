package com.adinnet.xedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.adinnet.xedittext.utils.RxRegUtils;

/**
 * 带删除按钮的EditText,支持手机号校验{@link #isPhoneCheckPass(),#getPhoneCheckInfo()}
 */
public class XEditText extends EditText implements TextWatcher, EditText.OnFocusChangeListener {
	private final int DRAWABLE_LEFT = 0;
    private final int DRAWABLE_TOP = 1;
    private final int DRAWABLE_RIGHT = 2;
    private final int DRAWABLE_BOTTOM = 3;

	/**
	 * 删除图标
	 */
	private Drawable mDeleteDrawable;

	/**
	 * true	对应{@link com.adinnet.xedittext.utils.RxRegUtils#isMobileSimple(String)}
	 * false	对应{@link com.adinnet.xedittext.utils.RxRegUtils#isMobile(String)}
	 * 默认为true
	 */
	private boolean isSimpleCheckNum = true;
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public XEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XEditText);
//		isPhone = a.getBoolean(a.getIndex(R.styleable.XEditText_isPhone), false);
		isSimpleCheckNum = a.getBoolean(R.styleable.XEditText_isSimpleCheckNum, true);

		for(int i=0; i<a.length(); i++) {
			int attr = a.getIndex(i);
			if(attr == R.styleable.XEditText_deleteDrawable) {
				mDeleteDrawable = a.getDrawable(attr);
				break;
			}
		}

		a.recycle();

		if(mDeleteDrawable == null)
			mDeleteDrawable = getContext().getResources().getDrawable(R.mipmap.icon_delete);
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public XEditText(Context context) {
		this(context, null);
	}

	private void init() {
		int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, Resources.getSystem().getDisplayMetrics());
		this.setPadding(getPaddingLeft(), 0, paddingRight, 0);
		setGravity(Gravity.CENTER_VERTICAL);
		this.addTextChangedListener(this);
		this.setOnFocusChangeListener(this);

		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void onTextChanged(CharSequence text, int start,
							  int lengthBefore, int lengthAfter) {
		if(TextUtils.isEmpty(text.toString())) this.setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT],
				getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
		else if(mDeleteDrawable != null)this.setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT],
				getCompoundDrawables()[DRAWABLE_TOP], mDeleteDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
				Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT] ;
				if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width() - getPaddingRight())) {
					clearText();
//					CDLog.i("TAG","----");
				}
			
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	private void clearText() {
		this.setText("");
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(!hasFocus && getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
			this.setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT], 
					getCompoundDrawables()[DRAWABLE_TOP], null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
		}else if(this.getText().toString().length() > 0 && mDeleteDrawable != null) this.setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT],
				getCompoundDrawables()[DRAWABLE_TOP], mDeleteDrawable, getCompoundDrawables()[DRAWABLE_BOTTOM]);
	}

	/**
	 * "" 表示验证通过
	 * @return
     */
	public String getPhoneCheckInfo() {

		String text = this.getText().toString().trim();
		if(TextUtils.isEmpty(text)) return "请输入手机号";
			if(!(isSimpleCheckNum ? RxRegUtils.isMobileSimple(text) : RxRegUtils.isMobile(text)))
			return "请输入正确的手机号";
		return "";
	}

	/**
	 * 检验是否符合手机号规则(大陆)
	 * @return
     */
	public boolean isPhoneCheckPass() {
		String text = this.getText().toString().trim();
		return !TextUtils.isEmpty(text) && RxRegUtils.isMobileSimple(text);
	}

	/**
	 * @see #isSimpleCheckNum
	 * @param simple
     */
	public void setSimpleCheck(boolean simple) {
		this.isSimpleCheckNum = simple;
	}

}