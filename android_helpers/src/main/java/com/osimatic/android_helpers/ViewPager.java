package com.osimatic.android_helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewPager extends androidx.viewpager.widget.ViewPager {
	private boolean enabled;

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return enabled && super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return enabled && super.onInterceptTouchEvent(event);
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isPagingEnabled() {
		return enabled;
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		return true;
	}
}
