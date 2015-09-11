// Version History
// v1.0: initial version
// v1.01
//    - Set LayoutParams for Panels of Container
// v1.02
//    - Line below tabs is always on the bottom of the tabs view
// v1.03
//    - Fix line redraw problems on Android4.0+
//    - Fix UppercaseTitle
// v2.00
//    - Complete new code for Swipeytabs
// v2.10
//    - Use raiseEventfromUI() in most events
//    - New SupportTabs object
//    - New SupportTitles object
// v2.20
//    - Use addOnPageChangedListener
// History now on Github

package de.amberhome.viewpager;

import android.support.v4.view.ViewPager;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.DependsOn;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.objects.ViewWrapper;

@Version(2.20f)
@Author("Markus Stipp")
@ActivityObject
@ShortName("AHViewPager")
@Events(values = { "PageChanged (Position As Int)",
		"PageScrollStateChanged (State as Int)",
		"PageScrolled (Position As Int, PositionOffset as Float, PositionOffsetPixels as Int)",
		"PageCreated (Position As Int, Page as Object)",
		"PageDestroyed (Position As Int, Page as Object)" })
@DependsOn(values = { "android-support-v4" })
/**
 * AHViewPager is the main Object that makes the sliding of the pages. 
 */
public class AHViewPager extends ViewWrapper<CustomViewPager> {

	/**
	 * This library provides objects to implement a CustomViewPager.
	 */
	public static void LIBRARY_DOC() {

	}

	protected String mEventName;
	
	public static int SCROLLSTATE_IDLE = ViewPager.SCROLL_STATE_IDLE;
	public static int SCROLLSTATE_DRAGGING = ViewPager.SCROLL_STATE_DRAGGING;
	public static int SCROLLSTATE_SETTLING = ViewPager.SCROLL_STATE_SETTLING;

	/**
	 * Initializes the object.
	 * 
	 * Layout - A fully initialized AHPagerLayout object with the content of the
	 * pages EventName - Sets the sub that will handle the event.
	 */
	public void Initialize(BA ba, AHPageContainer Layout, String EventName) {
		super.Initialize(ba, EventName);
		Layout.mEventName = EventName.toLowerCase();
		((CustomViewPager) getObject()).setAdapter(Layout);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName,
			boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new CustomViewPager(ba.context));
		super.innerInitialize(ba, EventName, true);
		mEventName = EventName;

		((CustomViewPager) getObject())
				.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

					@Override
					public void onPageScrollStateChanged(int state) {
						if (ba.subExists(EventName + "_pagescrollstatechanged")) {
							ba.raiseEventFromUI(
									getObject(),
									EventName + "_pagescrollstatechanged",
									new Object[] { state });
						}
					}

					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
						if (ba.subExists(EventName + "_pagescrolled")) {
							ba.raiseEvent(
									getObject(),
									EventName + "_pagescrolled",
									new Object[] { position, positionOffset, positionOffsetPixels });
						}
					}

					@Override
					public void onPageSelected(int position) {
						if (ba.subExists(EventName + "_pagechanged")) {
							ba.raiseEventFromUI(
									getObject(),
									EventName + "_pagechanged",
									new Object[] { position });
						}
					}
				});

	}

	public AHViewPager() {
		super();
	}

	/**
	 * Jump or Scroll to the new page Page - The new page to display Smooth -
	 * True to smoothly scroll to the new item, false to transition immediately
	 */
	public void GotoPage(int Page, boolean Smooth) {
		((CustomViewPager) getObject()).setCurrentItem(Page, Smooth);
	}

	/**
	 * Get or Set the current page
	 */
	public void setCurrentPage(int Page) {
		((CustomViewPager) getObject()).setCurrentItem(Page);
	}

	public int getCurrentPage() {
		return ((CustomViewPager) getObject()).getCurrentItem();
	}

	/**
	 * Enables or disables the paging of the ViewPager.
	 */
	public void setPagingEnabled(boolean Enabled) {
		((CustomViewPager) getObject()).setPagingEnabled(Enabled);
	}

	public boolean getPagingEnabled() {
		return ((CustomViewPager) getObject()).getPagingEnabled();
	}

}
