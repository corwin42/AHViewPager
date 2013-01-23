package de.amberhome.viewpager;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;

/**
 * AHPagerLayout holds the layout of the pages
 */
@ActivityObject
@ShortName("AHPageContainer")	
/**
 * The AHPageContainer is a container for the pages.
 *
 */
public class AHPageContainer extends PagerAdapter implements ViewPagerTabProvider {
	
	protected transient Activity mContext;
	protected transient BA mBa;
	protected String mEventName;


	protected List pages = new List();
	protected List titles = new List();
	
	protected boolean isInitialized = false;
	
	@SuppressWarnings("static-access")
	@Hide
	public int POSITION_NONE = super.POSITION_NONE;
	
	@SuppressWarnings("static-access")
	@Hide
	public int POSITION_UNCHANGED = super.POSITION_UNCHANGED;
	
	public AHPageContainer() {
		isInitialized = false;
	}

	/**
	 * Initializes the object
	 */
	public void Initialize(BA ba) {
		mContext = ba.activity;
		mBa = ba;
		pages.Initialize();
		titles.Initialize();
		isInitialized = true;
	}
	
	public void Initialize2(BA ba, List Pages, List Titles) {		
		mContext = ba.activity;
		mBa = ba;
		pages = Pages;
		titles = Titles;
	}
	
	public boolean getIsInitialized() {
		return isInitialized;
	}
	
	/**
	 * Gets the number of pages in the layout.
	 */
	@Override
	public int getCount() {
		return pages.getSize();
	}

	@Hide
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = (View) pages.Get(position);
		LayoutParams lp =  new LayoutParams();
		lp.width = container.getLayoutParams().width;
		lp.height = container.getLayoutParams().height;
		((ViewPager) container).addView(v,0,lp);
		v.setLayoutParams(lp);
		
		if (mBa.subExists(mEventName + "_pagecreated")) {
			mBa.raiseEvent(container, mEventName + "_pagecreated", new Object[] {position, v});
		}
		
		return v;
	}

	@Hide
	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
		if (mBa.subExists(mEventName + "_pagedestroyed")) {
			mBa.raiseEvent(container, mEventName + "_pagedestroyed", new Object[] {position, view});
		}
	}


	@Hide
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}


	@Hide
	@Override
	public void finishUpdate(View container) {
	}

	@Hide
	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Hide
	@Override
	public Parcelable saveState() {
		return null;
	}

	@Hide
	@Override
	public void startUpdate(View container) {
	}

	/**
	 * Gets the title of the page.
	 * 
	 * Position - Position of the title to get.
	 */
	public String GetTitle(int Position) {
		return (String) titles.Get(Position);
	}

	/**
	 * Sets the title of the page.
	 * 
	 * Title - The new title text.
	 * Position - Position of the title to change.
	 */
	public void SetTitle(String Title, int Position) {
		titles.Set(Position, Title);
	}
	
	@Hide
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}
	
	/**
	 * Add a new page to the pager layout. Normally you will provide a Panel here.
	 * View - The view to be added as a new page
	 * Title - The title of the new page
	 */
	public void AddPage(View View, String Title)
	  {
		pages.Add(View);
		titles.Add(Title);
		
		notifyDataSetChanged();
	  }	
	
	/**
	 * Add a new page to the pager layout at the specified position. Normally you will provide a Panel here.
	 * View - The view to be added as a new page
	 * Title - The title of the new page
	 * Position - Position where the new page will be added
	 */
	public void AddPageAt(View View, String Title, int Position)
	  {
		pages.InsertAt(Position, View);
		titles.InsertAt(Position, Title);
		
		notifyDataSetChanged();
	  }	

	/**
	 * Deletes the specified page.
	 * Position - Page to be deleted
	 */
	public void DeletePage(int Position) {
		pages.RemoveAt(Position);
		titles.RemoveAt(Position);
		
		notifyDataSetChanged();
	}
	
	@Hide
	@Override
	public int getItemPosition(Object object) {
		//return super.getItemPosition(object);
		return POSITION_NONE;
	}
	
	@Hide
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	@Hide
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}
	
	/**
	 * Returns the View added as a page. Normally this is a Panel object.
	 * Position - Index of the object
	 */
	public Object GetPageObject(int Position) {
		return pages.Get(Position);
	}
	
	/**
	 * Sets the pages and titles for the container
	 * 
	 * Pages - A List or Array of Panel objects
	 * Titles - A List or Array of page titles
	 */
	public void SetContent(List Pages, List Titles) {
		pages = Pages;
		titles = Titles;
		
		notifyDataSetChanged();
	}
	
	/**
	 * The List or Array of the pages.
	 */
	public List getPages() {
		return pages;
	}
	
	/**
	 * The List or Array of the titles.
	 */
	public List getTitles() {
		return titles;
	}
	
}


