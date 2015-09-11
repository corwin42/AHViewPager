package de.amberhome.viewpager;

/*
 * Copyright (C) 2011 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import anywheresoftware.b4a.BA;

public class FixedTabsView extends LinearLayout implements ViewPager.OnPageChangeListener {

	@SuppressWarnings("unused")
	private static final String TAG = "com.astuetz.viewpager.extensions";

	private Context mContext;
	protected BA mBa;
	private String mEventName;

	private CustomViewPager mPager;

	private ArrayList<View> mTabs = new ArrayList<View>();

	private Drawable mDividerDrawable;

	private int mDividerColor = 0xFF636363;

	private int mDividerMarginTop = 12;
	private int mDividerMarginBottom = 21;

	private int mLineColor = 0xFF6F8FC7;
	private int mLineColorSelected = 0xFF6F8FC7;

	private int mLineHeight = 2;
	private int mLineHeightSelected = 6;

	private int mTabPaddingLeft = 0;
	private int mTabPaddingTop = 0;
	private int mTabPaddingRight = 0;
	private int mTabPaddingBottom = 0;

	private int mTextColor = 0xFFFFFFFF;
	private float mTextSize = 14;
	private Typeface mTextTypeface = Typeface.create((String) null, Typeface.BOLD);

	private boolean mUpperCaseTitle = false;	
	
	public FixedTabsView(BA ba) {
		this(ba, null);
	}

	public FixedTabsView(BA ba, AttributeSet attrs) {
		this(ba, attrs, 0);
	}

	public FixedTabsView(BA ba, AttributeSet attrs, int defStyle) {
		super(ba.context, attrs);

		this.mContext = ba.context;
		this.mBa = ba;

		//final TypedArray a = ba.context.obtainStyledAttributes(attrs, R.styleable.ViewPagerExtensions, defStyle, 0);

		//mDividerColor = a.getColor(R.styleable.ViewPagerExtensions_dividerColor, mDividerColor);

		//mDividerMarginTop = a.getDimensionPixelSize(R.styleable.ViewPagerExtensions_dividerMarginTop, mDividerMarginTop);
		//mDividerMarginBottom = a.getDimensionPixelSize(R.styleable.ViewPagerExtensions_dividerMarginBottom,
		//    mDividerMarginBottom);

		//mDividerDrawable = a.getDrawable(R.styleable.ViewPagerExtensions_dividerDrawable);

		//a.recycle();

		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	/**
	 * Binds the {@link ViewPager} to this View
	 * 
	 */
	public void setViewPager(CustomViewPager pager, String eventName) {

		if (!(pager.getAdapter() instanceof ViewPagerTabProvider)) {
			throw new IllegalStateException("The pager's adapter has to implement ViewPagerTabProvider.");
		}
		
		this.mPager = pager;
		mPager.setOnPageChangeListener(this);
		mEventName = eventName.toLowerCase();

		if (mPager != null) notifyDatasetChanged();
	}

	/**
	 * Initialize and add all tabs to the layout
	 */
	public void notifyDatasetChanged() {

		removeAllViews();
		mTabs.clear();

		for (int i = 0; i < mPager.getAdapter().getCount(); i++) {

			final int index = i;

			ViewPagerTabButton tab = new ViewPagerTabButton(mContext);

			if (mUpperCaseTitle)
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i).toUpperCase());
			else				
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i));

			tab.setGravity(Gravity.CENTER);
			tab.setPadding(mTabPaddingLeft, mTabPaddingTop, mTabPaddingRight, mTabPaddingBottom);
			tab.setTextSize(mTextSize);
			tab.setTextColor(mTextColor);
			tab.setTypeface(mTextTypeface);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
			tab.setLayoutParams(params);
			this.addView(tab);

			mTabs.add(tab);

			if (i != mPager.getAdapter().getCount() - 1) {
				this.addView(getSeparator());
			}

			tab.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mPager.getPagingEnabled())
						mPager.setCurrentItem(index);
				}
			});

		}

		applyStyles();
		selectTab(mPager.getCurrentItem());
	}

	@Override
    public void onPageScrollStateChanged(int state) {
		if (mBa.subExists(mEventName + "_pagescrollstatechanged")) {
			mBa.raiseEventFromUI(
					mPager,
					mEventName + "_pagescrollstatechanged",
					new Object[] { state });
		}
    }

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (mBa.subExists(mEventName + "_pagescrolled")) {
			mBa.raiseEvent(
					mPager,
					mEventName + "_pagescrolled",
					new Object[] { position, positionOffset, positionOffsetPixels });
		}
	}

	@Override
	public void onPageSelected(int position) {
		selectTab(position);

		if (mBa.subExists(mEventName + "_pagechanged")) {
			mBa.raiseEventFromUI(mPager, mEventName + "_pagechanged", new Object[] {position});
		}
	}


	/**
	 * Creates and returns a new Separator View
	 * 
	 * @return
	 */
	private View getSeparator() {
		View v = new View(mContext);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT);
		params.setMargins(0, mDividerMarginTop, 0, mDividerMarginBottom);
		v.setLayoutParams(params);
		v.setTag("SEP");

		if (mDividerDrawable != null) v.setBackgroundDrawable(mDividerDrawable);
		else v.setBackgroundColor(mDividerColor);

		return v;
	}


	/**
	 * 
	 */
	private void applyStyles() {

		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			
			View v = getChildAt(i);
			if (v.getTag() != null && v.getTag() == "SEP") {
				if (mDividerDrawable != null) v.setBackgroundDrawable(mDividerDrawable);
				else v.setBackgroundColor(mDividerColor);
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
				params.topMargin = mDividerMarginTop;
				params.bottomMargin = mDividerMarginBottom;
				v.setLayoutParams(params);
			}
			else {
				ViewPagerTabButton tab = (ViewPagerTabButton) getChildAt(i);

				tab.setLineColor(mLineColor);
				tab.setLineColorSelected(mLineColorSelected);
				tab.setLineHeight(mLineHeight);
				tab.setLineHeightSelected(mLineHeightSelected);
				
				tab.setPadding(mTabPaddingLeft, mTabPaddingTop, mTabPaddingRight, mTabPaddingBottom);
				tab.setTextColor(mTextColor);
				tab.setTextSize(mTextSize);
				tab.setTypeface(mTextTypeface);
			}
		}

		this.requestLayout();
	}

	/**
	 * Call this method if the titles in the pager's adapter were changed
	 */
	public void refreshTitles() {
		final int count = getChildCount();

		int pos = 0;
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			if (v.getTag() == null) {
				ViewPagerTabButton tab = (ViewPagerTabButton) getChildAt(i);
				if (mUpperCaseTitle)
					tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(pos).toUpperCase());
				else				
					tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(pos));
				
				pos++;
			}
		}
		this.requestLayout();
	}

	/**
	 * Runs through all tabs and sets if they are currently selected.
	 * 
	 * @param position
	 *          The position of the currently selected tab.
	 */
	private void selectTab(int position) {

		for (int i = 0, pos = 0; i < getChildCount(); i++) {

			if (this.getChildAt(i) instanceof ViewPagerTabButton) {
				this.getChildAt(i).setSelected(pos == position);
				pos++;
			}

		}
	}

	
	public int getDividerColor() {
		return mDividerColor;
	}

	public void setDividerColor(int mDividerColor) {
		this.mDividerColor = mDividerColor;
		applyStyles();
	}

	public int getDividerMarginTop() {
		return mDividerMarginTop;
	}

	public void setDividerMarginTop(int mDividerMarginTop) {
		this.mDividerMarginTop = mDividerMarginTop;
		applyStyles();
	}

	public int getDividerMarginBottom() {
		return mDividerMarginBottom;
	}

	public void setDividerMarginBottom(int mDividerMarginBottom) {
		this.mDividerMarginBottom = mDividerMarginBottom;
		applyStyles();
	}

	public int getLineColor() {
		return mLineColor;
	}

	public void setLineColor(int mLineColor) {
		this.mLineColor = mLineColor;
		applyStyles();
	}

	public int getLineColorSelected() {
		return mLineColorSelected;
	}

	public void setLineColorSelected(int mLineColorSelected) {
		this.mLineColorSelected = mLineColorSelected;
		applyStyles();
	}

	public int getLineHeight() {
		return mLineHeight;
	}

	public void setLineHeight(int mLineHeight) {
		this.mLineHeight = mLineHeight;
		applyStyles();
	}

	public int getLineHeightSelected() {
		return mLineHeightSelected;
	}

	public void setLineHeightSelected(int mLineHeightSelected) {
		this.mLineHeightSelected = mLineHeightSelected;
		applyStyles();
	}

	public int getTabPaddingLeft() {
		return mTabPaddingLeft;
	}

	public void setTabPaddingLeft(int mTabPaddingLeft) {
		this.mTabPaddingLeft = mTabPaddingLeft;
		applyStyles();
	}

	public int getTabPaddingTop() {
		return mTabPaddingTop;
	}

	public void setTabPaddingTop(int mTabPaddingTop) {
		this.mTabPaddingTop = mTabPaddingTop;
		applyStyles();
	}

	public int getTabPaddingRight() {
		return mTabPaddingRight;
	}

	public void setTabPaddingRight(int mTabPaddingRight) {
		this.mTabPaddingRight = mTabPaddingRight;
		applyStyles();
	}

	public int getTabPaddingBottom() {
		return mTabPaddingBottom;
	}

	public void setTabPaddingBottom(int mTabPaddingBottom) {
		this.mTabPaddingBottom = mTabPaddingBottom;
		applyStyles();
	}

	public void setTabPadding(int left, int top, int right, int bottom) {
		this.mTabPaddingLeft = left;
		this.mTabPaddingTop = top;
		this.mTabPaddingRight = right;
		this.mTabPaddingBottom = bottom;
		applyStyles();
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
		applyStyles();
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTextSize(float mTextSize) {
		this.mTextSize = mTextSize;
		applyStyles();
	}

	public Typeface getTextTypeface() {
		return mTextTypeface;
	}

	public void setTextTypeface(Typeface mTextTypeface) {
		this.mTextTypeface = mTextTypeface;
		applyStyles();
	}

	public boolean getUpperCaseTitle() {
		return mUpperCaseTitle;
	}

	public void setUpperCaseTitle(boolean mUpperCaseTitle) {
		this.mUpperCaseTitle = mUpperCaseTitle;
		refreshTitles();
	}

}