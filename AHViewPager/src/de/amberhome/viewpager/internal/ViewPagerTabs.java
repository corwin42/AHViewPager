package de.amberhome.viewpager.internal;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import anywheresoftware.b4a.BA;
import de.amberhome.viewpager.AHViewPager;

public class ViewPagerTabs extends ViewGroup implements
OnPageChangeListener, OnTouchListener {

    // Scrolling direction
    private enum Direction {
        Left, None, Right;

        public int getPrev(TabPosition position) {
            return this == Direction.Left ? position.leftPos : position.oldPos;
        }

        public int getNext(TabPosition position) {
            return this == Direction.Right ? position.leftPos : position.oldPos;
        }

        public float getX(float positionOffset) {
            return this == Direction.Left ? 1 - positionOffset : positionOffset;
        }

    }
    
    private int mPosition;
    
    // This ArrayList stores the positions for each tab.
    private ArrayList<TabPosition> mPositions = new ArrayList<TabPosition>();
    
    // Length of the horizontal fading edges
    private static final int SHADOW_WIDTH = 20;
    
    private CustomViewPager mPager;
    
    private int mTabsCount = 0;
    
    private int mWidth = 0;
    
    private int mCenter = 0;
    
    private int mHighlightOffset = 0;
    
    // store measure specs for manual re-measuring after
    // data has changed
    private int mWidthMeasureSpec = 0;
    private int mHeightMeasureSpec = 0;
    
    // The offset at which tabs are going to
    // be moved, if they are outside the screen
    private int mOutsideOffset = -1;
    
    private float mDragX = 0.0f;

    /** Last offset of the view pager. */
    private int mLastOffsetX;

    /** Original direction. */
    private Direction mOrigDirection;

    /** Selected index. */
    private int mSelectedIndex;
    
	// Context.
	private Context mContext;
	protected BA mBa;
	private String mEventName;


	// These values will be passed to every child ({@link ViewPagerTab})

	private int mBackgroundColorPressed = 0x9943797F;

	private int mTextColor = 0xFF999999;
	private int mTextColorCenter = 0xFF91A438;

	private int mLineColor = 0x00000000;
	private int mLineColorCenter = 0xFF91A438;
	private int mLineHeight = 3;


	// These values are needed here
	private int mTabPaddingLeft = 25;
	private int mTabPaddingTop = 5;
	private int mTabPaddingRight = 25;
	private int mTabPaddingBottom = 10;

	private float mTextSize = 14;

	private boolean mUpperCaseTitle = false;

	public ViewPagerTabs(BA ba) {
		this(ba, null);
	}

	public ViewPagerTabs(BA ba, AttributeSet attrs) {
		this(ba, attrs, 0);
	}

	public ViewPagerTabs(BA ba, AttributeSet attrs, int defStyle) {
		super(ba.context, attrs, defStyle);

		mBa = ba;
		mContext = ba.context;

		setHorizontalFadingEdgeEnabled(true);
		setFadingEdgeLength((int) (getResources().getDisplayMetrics().density * SHADOW_WIDTH));
		setWillNotDraw(false);
		
		setOnTouchListener(this);
	}
	
	@Override
	protected float getLeftFadingEdgeStrength() {
		return 1.0f;
	}

	@Override
	protected float getRightFadingEdgeStrength() {
		return 1.0f;
	}

    /**
     * Notify the view that new data is available.
     */
    public void notifyDatasetChanged() {
        if (mPager != null) {
            initTabs();
            measure(mWidthMeasureSpec, mHeightMeasureSpec);
            calculateNewPositions(true);
        }
    }
    

	/**
	 * Binds the {@link ViewPager} to this instance
	 * 
	 * @param pager
	 *          An instance of {@link ViewPager}
	 */
	public void setViewPager(AHViewPager pager, String eventName) {

		if (!(pager.getObject().getAdapter() instanceof ViewPagerTabProvider)) {
			throw new IllegalStateException("The pager's adapter has to implement ViewPagerTabProvider.");
		}

		this.mPager = pager.getObject();
		mPager.setCurrentItem(this.mPosition);
		mPager.addOnPageChangeListener(this);
		mEventName = eventName.toLowerCase();

		mLastOffsetX = mPager.getScrollX();

		initTabs();
	}

	/**
	 * Binds the {@link ViewPager} to this instance and sets the current position
	 * 
	 * @param pager
	 *          An instance of {@link ViewPager}
	 * @param position
	 *          Initial position of the {@link ViewPager}
	 */
	public void setViewPager(AHViewPager pager, String eventName, int position) {
		this.mPosition = position;
		setViewPager(pager, eventName);
	}

    /**
     * Initialize and add all tabs to the Layout
     */
    private void initTabs() {
        
        // Remove all old child views
        removeAllViews();
        
        mPositions.clear();
        
        for (int i = 0; i < mPager.getAdapter().getCount(); i++) {
    		ViewPagerTab tab = new ViewPagerTab(mContext);
			if (mUpperCaseTitle)
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i).toUpperCase());
			else				
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i));
    		tab.setIndex(i);
            addTab(tab, i);
            mPositions.add(new TabPosition());
        }
        
        mTabsCount = getChildCount();
        mPosition = mPager.getCurrentItem();
		applyStyles();
        mSelectedIndex = mPosition;
        if (getChildAt(mSelectedIndex) != null)
        	getChildAt(mSelectedIndex).setSelected(true);
        
    }
    	
	/**
	 * Returns the current position
	 */
	public int getPosition() {
		return this.mPosition;
	}

    /**
     * Adds a new {@link SwipeyTabButton} to the layout
     * 
     * @param index
     *            The index from the Pagers adapter
     * @param title
     *            The title which should be used
     */
    public void addTab(View tab, final int index) {
        if (tab == null) return;
        
        addView(tab);
        
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (mPager.getPagingEnabled())
					mPager.setCurrentItem(((ViewPagerTab) v).getIndex());
            }
        });
        
        tab.setOnTouchListener(this);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        
        // Set a default outsideOffset
        if (mOutsideOffset < 0) mOutsideOffset = w;
        
        mWidth = w;
        mCenter = w / 2;
        mHighlightOffset = w / 5;
        
        if (mPager != null) mPosition = mPager.getCurrentItem();
        calculateNewPositions(true);
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int maxTabHeight = 0;

        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);

        for (int i = 0; i < mTabsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) continue;

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            mPositions.get(i).width = child.getMeasuredWidth();
            mPositions.get(i).height = child.getMeasuredHeight();

            maxTabHeight = Math.max(maxTabHeight, mPositions.get(i).height);
        }

        setMeasuredDimension(
                resolveSize(0, widthMeasureSpec),
                resolveSize(
                        maxTabHeight + getPaddingTop() + getPaddingBottom(),
                        heightMeasureSpec));

    }

    private void higlightTab(View tab, int position) {
        if (tab instanceof ViewPagerTab) {

            int tabCenter = mPositions.get(position).currentPos
                    + tab.getWidth() / 2;
            int diff = Math.abs(mCenter - tabCenter);
            int p = 100 * diff / mHighlightOffset;

            ((ViewPagerTab) tab)
                    .setHighlightPercentage(diff <= mHighlightOffset ? 100 - p
                            : 0);

        }
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int paddingTop = getPaddingTop();

        for (int i = 0; i < mTabsCount; i++) {

            View tab = getChildAt(i);
            TabPosition pos = mPositions.get(i);

            tab.layout(pos.currentPos, paddingTop, pos.currentPos + pos.width,
                    paddingTop + pos.height);

            higlightTab(tab, i);

        }

    }

    /**
     * This method calculates the previous, current and next position for each
     * tab
     * 
     * -5 -4 -3 /-2 |-1 0 +1| +2\ +3 +4 +5
     * 
     * There are the following cases:
     * 
     * [1] -5 to -3 are outside the screen 
     * [2] -2 is outside the screen, may come into the screen when swiping right 
     * [3] -1 is inside the screen, aligned at the left 
     * [4] 0 is inside the screen, aligned at the center 
     * [5] +1 is inside the screen, aligned at the right 
     * [6] +2 is outside the screen, may come into the screen when swiping left 
     * [7] +3 to +5 are outside the screen
     * 
     * @param layout
     *            If true, all tabs will be aligned at their initial position
     */
    private void calculateNewPositions(boolean layout) {

        if (mTabsCount == 0) return;

        final int currentItem = mPosition;

        for (int i = 0; i < mTabsCount; i++) {

            mOrigDirection = Direction.None;

            if (i < currentItem - 2) alignLeftOutside(i, false);
            else if (i == currentItem - 2) alignLeftOutside(i, true);
            else if (i == currentItem - 1) alignLeft(i);
            else if (i == currentItem) alignCenter(i);
            else if (i == currentItem + 1) alignRight(i);
            else if (i == currentItem + 2) alignRightOutside(i, true);
            else if (i > currentItem + 2) alignRightOutside(i, false);

        }

        preventFromOverlapping();

        if (layout) {
            for (TabPosition p : mPositions) {
                p.currentPos = p.oldPos;
            }
            requestLayout();
        }

    }

    private int leftOutside(int position) {
        View tab = getChildAt(position);
        final int width = tab.getMeasuredWidth();
        return width * (-1) - mOutsideOffset;
    }

    private int left(int position) {
        View tab = getChildAt(position);
        return 0 - tab.getPaddingLeft();
    }

    private int center(int position) {
        View tab = getChildAt(position);
        final int width = tab.getMeasuredWidth();
        return mWidth / 2 - width / 2;
    }

    private int right(int position) {
        View tab = getChildAt(position);
        final int width = tab.getMeasuredWidth();
        return mWidth - width + tab.getPaddingRight();
    }

    private int rightOutside(int position) {
        return mWidth + mOutsideOffset;
    }

    private void alignLeftOutside(int position,
            boolean canComeToLeft) {
        TabPosition pos = mPositions.get(position);

        pos.oldPos = leftOutside(position);
        pos.leftPos = pos.oldPos;
        pos.rightPos = canComeToLeft ? left(position) : pos.oldPos;
    }

    private void alignLeft(int position) {
        TabPosition pos = mPositions.get(position);

        pos.leftPos = leftOutside(position);
        pos.oldPos = left(position);
        pos.rightPos = center(position);
    }

    private void alignCenter(int position) {
        TabPosition pos = mPositions.get(position);

        pos.leftPos = left(position);
        pos.oldPos = center(position);
        pos.rightPos = right(position);
    }

    private void alignRight(int position) {
        TabPosition pos = mPositions.get(position);

        pos.leftPos = center(position);
        pos.oldPos = right(position);
        pos.rightPos = rightOutside(position);
    }

    private void alignRightOutside(int position, boolean canComeToRight) {
        TabPosition pos = mPositions.get(position);

        pos.oldPos = rightOutside(position);
        pos.rightPos = pos.oldPos;
        pos.leftPos = canComeToRight ? right(position) : pos.oldPos;
    }

    /**
    *
    */
    private void preventFromOverlapping() {

        final int currentItem = mPosition;

        TabPosition leftOutside = currentItem > 1 ? mPositions
                .get(currentItem - 2) : null;
        TabPosition left = currentItem > 0 ? mPositions
                .get(currentItem - 1) : null;
        TabPosition center = mPositions.get(currentItem);
        TabPosition right = currentItem < mTabsCount - 1 ? mPositions
                .get(currentItem + 1) : null;
        TabPosition rightOutside = currentItem < mTabsCount - 2 ? mPositions
                .get(currentItem + 2) : null;

        if (leftOutside != null) {
            if (leftOutside.rightPos + leftOutside.width >= left.rightPos) {
                leftOutside.rightPos = left.rightPos - leftOutside.width;
            }
        }

        if (left != null) {
            if (left.oldPos + left.width >= center.oldPos) {
                left.oldPos = center.oldPos - left.width;
            }
            if (center.rightPos <= left.rightPos + left.width) {
                center.rightPos = left.rightPos + left.width;
            }
        }

        if (right != null) {
            if (right.oldPos <= center.oldPos + center.width) {
                right.oldPos = center.oldPos + center.width;
            }
            if (center.leftPos + center.width >= right.leftPos) {
                center.leftPos = right.leftPos - center.width;
            }
        }

        if (rightOutside != null) {
            if (rightOutside.leftPos <= right.leftPos + right.width) {
                rightOutside.leftPos = right.leftPos + right.width;
            }
        }
    }

    private void updateDir(Direction dir) {
        if (mOrigDirection == Direction.None) {
            mOrigDirection = dir;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onPageScrollStateChanged(int state) {
		if (mBa.subExists(mEventName + "_pagescrollstatechanged")) {
			mBa.raiseEvent(
					mPager,
					mEventName + "_pagescrollstatechanged",
					new Object[] { state });
		}
    }
    
    /**
     * At this point the scrolling direction is determined and every child is
     * interpolated to its previous or next position
     * 
     * {@inheritDoc}
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {

        if (position != mPosition) {
            mPosition = position;
            calculateNewPositions(false);
        }

        // Check if the user is swiping to the left or to the right
        Direction dir;
        int pageScroll = mPager.getScrollX();
        if (pageScroll < mLastOffsetX) {
            dir = Direction.Left;
        } else if (pageScroll > mLastOffsetX) {
            dir = Direction.Right;
        } else {
            dir = Direction.None;
        }
        mLastOffsetX = pageScroll;

        updateDir(dir);
        float x = mOrigDirection.getX(positionOffset);

        // Iterate over all tabs and set their current positions
        for (int i = 0; i < mTabsCount; i++) {
            TabPosition pos = mPositions.get(i);

            float y0 = mOrigDirection.getPrev(pos);
            float y1 = mOrigDirection.getNext(pos);

            float dest = y0 + (y1 - y0) * x;
            pos.currentPos = (int) (dest);
        }

        offsetChildren();

		if (mBa.subExists(mEventName + "_pagescrolled")) {
			mBa.raiseEvent(
					mPager,
					mEventName + "_pagescrolled",
					new Object[] { position, positionOffset, positionOffsetPixels });
		}
    
    }

    private void offsetChildren() {
        int count = mTabsCount;
        ArrayList<TabPosition> positions = mPositions;
        for (int i = 0; i < count; i++) {
            View tab = getChildAt(i);
            TabPosition pos = positions.get(i);

            tab.offsetLeftAndRight(pos.currentPos - tab.getLeft());

            higlightTab(tab, i);
        }
        invalidate();
    }

    
    
	@Override
	public void onPageSelected(int position) {
        View selected = getChildAt(position);
        View unselected = getChildAt(mSelectedIndex);
        mSelectedIndex = position;
        selected.setSelected(true);
        unselected.setSelected(false);
        
		if (mBa.subExists(mEventName + "_pagechanged")) {
			mBa.raiseEvent(mPager, mEventName + "_pagechanged", new Object[] {position});
		}
	}


	/*
	 * Public property access
	 */

	public void setTabPaddingLeft(int padding) {
		this.mTabPaddingLeft = padding;
		applyStyles();
	}

	public void setTabPaddingTop(int padding) {
		this.mTabPaddingTop = padding;
		applyStyles();
	}

	public void setTabPaddingRight(int padding) {
		this.mTabPaddingRight = padding;
		applyStyles();
	}

	public void setTabPaddingBottom(int padding) {
		this.mTabPaddingBottom = padding;
		applyStyles();
	}

	public void setTabPadding(int left, int top, int right, int bottom) {
		this.mTabPaddingLeft = left;
		this.mTabPaddingTop = top;
		this.mTabPaddingRight = right;
		this.mTabPaddingBottom = bottom;
		applyStyles();
	}

	public void setBackgroundColorPressed(int color) {
		this.mBackgroundColorPressed = color;
		applyStyles();
	}

	public void setTextSize(float size) {
		this.mTextSize = size;
		applyStyles();
	}

	public void setTextColor(int color) {
		this.mTextColor = color;
		applyStyles();
	}

	public void setTextColorCenter(int color) {
		this.mTextColorCenter = color;
		applyStyles();
	}

	public void setLineColorCenter(int color) {
		this.mLineColorCenter = color;
		applyStyles();
	}

	public void setLineHeight(int height) {
		this.mLineHeight = height;
		applyStyles();
	}

	public void setOutsideOffset(int offset) {
		this.mOutsideOffset = offset;
		applyStyles();
	}
	
	public void setUpperCaseTitle(boolean flag) {
		this.mUpperCaseTitle = flag;
		refreshTitles();
	}
	
	public int getOutsideOffset() {
		return mOutsideOffset;
	}

	public int getBackgroundColorPressed() {
		return mBackgroundColorPressed;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public int getTextColorCenter() {
		return mTextColorCenter;
	}

	public int getLineColor() {
		return mLineColor;
	}

	public int getLineColorCenter() {
		return mLineColorCenter;
	}

	public int getLineHeight() {
		return mLineHeight;
	}

	public int getTabPaddingLeft() {
		return mTabPaddingLeft;
	}

	public int getTabPaddingTop() {
		return mTabPaddingTop;
	}

	public int getTabPaddingRight() {
		return mTabPaddingRight;
	}

	public int getTabPaddingBottom() {
		return mTabPaddingBottom;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public boolean getUpperCaseTitle() {
		return mUpperCaseTitle;
	}

	/**
	 * 
	 */
	private void applyStyles() {

		final int count = getChildCount();

		for (int i = 0; i < count; i++) {

			ViewPagerTab tab = (ViewPagerTab) getChildAt(i);

			tab.setPadding(mTabPaddingLeft, mTabPaddingTop, mTabPaddingRight, mLineHeight + mTabPaddingBottom - 4);
			tab.setTextColors(mTextColor, mTextColorCenter);
			tab.setLineColors(mLineColor, mLineColorCenter);
			tab.setLineHeight(mLineHeight);
			tab.setBackgroundColorPressed(mBackgroundColorPressed);
			tab.setTextSize(mTextSize);

		}

		calculateNewPositions(true);
		this.requestLayout();
	}


	/**
	 * Call this method if the titles in the pager's adapter were changed
	 */
	public void refreshTitles() {
		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			ViewPagerTab tab = (ViewPagerTab) getChildAt(i);
			if (mUpperCaseTitle)
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i).toUpperCase());
			else				
				tab.setText(((ViewPagerTabProvider) mPager.getAdapter()).GetTitle(i));
		}

		calculateNewPositions(true);
		this.requestLayout();
	}



	/*
	 * state handling
	 */

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mPosition = savedState.position;
		mBackgroundColorPressed = savedState.backgroundColorPressed;
		mTextColor = savedState.textColor;
		mTextColorCenter = savedState.textColorCenter;
		mLineColorCenter = savedState.lineColorCenter;
		mLineHeight = savedState.lineHeight;
		mTabPaddingLeft = savedState.tabPaddingLeft;
		mTabPaddingTop = savedState.tabPaddingTop;
		mTabPaddingRight = savedState.tabPaddingRight;
		mTabPaddingBottom = savedState.tabPaddingBottom;
		mTextSize = savedState.textSize;
		applyStyles();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.position = mPosition;
		savedState.backgroundColorPressed = mBackgroundColorPressed;
		savedState.textColor = mTextColor;
		savedState.textColorCenter = mTextColorCenter;
		savedState.lineColorCenter = mLineColorCenter;
		savedState.lineHeight = mLineHeight;
		savedState.tabPaddingLeft = mTabPaddingLeft;
		savedState.tabPaddingTop = mTabPaddingTop;
		savedState.tabPaddingRight = mTabPaddingRight;
		savedState.tabPaddingBottom = mTabPaddingBottom;
		savedState.textSize = mTextSize;
		return savedState;
	}


	/**
	 * This holds our state
	 * 
	 */
	static class SavedState extends BaseSavedState {

		int position;
		int backgroundColorPressed;
		int textColor;
		int textColorCenter;
		int lineColorCenter;
		int lineHeight;
		int tabPaddingLeft;
		int tabPaddingTop;
		int tabPaddingRight;
		int tabPaddingBottom;
		float textSize;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			position = in.readInt();
			backgroundColorPressed = in.readInt();
			textColor = in.readInt();
			textColorCenter = in.readInt();
			lineColorCenter = in.readInt();
			lineHeight = in.readInt();
			tabPaddingLeft = in.readInt();
			tabPaddingTop = in.readInt();
			tabPaddingRight = in.readInt();
			tabPaddingBottom = in.readInt();
			textSize = in.readFloat();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(position);
			dest.writeInt(backgroundColorPressed);
			dest.writeInt(textColor);
			dest.writeInt(textColorCenter);
			dest.writeInt(lineColorCenter);
			dest.writeInt(lineHeight);
			dest.writeInt(tabPaddingLeft);
			dest.writeInt(tabPaddingTop);
			dest.writeInt(tabPaddingRight);
			dest.writeInt(tabPaddingBottom);
			dest.writeFloat(textSize);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
    /**
     * Helper class which holds different positions (and the width) for a tab
     * 
     */
    private class TabPosition {
        
        public int oldPos;
        
        public int leftPos;
        public int rightPos;
        
        public int currentPos;
        
        public int width;
        public int height;
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append("oldPos: ").append(oldPos).append(", ");
            sb.append("leftPos: ").append(leftPos).append(", ");
            sb.append("rightPos: ").append(rightPos).append(", ");
            sb.append("currentPos: ").append(currentPos);
            
            return sb.toString();
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getRawX();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDragX = x;
                if (mPager.getPagingEnabled())
                	mPager.beginFakeDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mPager.isFakeDragging()) break;
                mPager.fakeDragBy((mDragX - x) * (-1));
                mDragX = x;
                break;
            case MotionEvent.ACTION_UP:
                if (!mPager.isFakeDragging()) break;
                mPager.endFakeDrag();
                break;
        }
        
        return v.equals(this) ? true : super.onTouchEvent(event);
    }
    
}