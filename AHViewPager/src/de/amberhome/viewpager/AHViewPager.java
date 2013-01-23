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

@Version(2.00f)
@Author("Markus Stipp")

@ActivityObject
@ShortName("AHViewPager")
@Events(values={"PageChanged (Position As Int)", "PageCreated (Position As Int, Page as Object)", "PageDestroyed (Position As Int, Page as Object)"})
@DependsOn(values={"android-support-v4"})
/**
 * AHViewPager is the main Object that makes the sliding of the pages. 
 */
public class AHViewPager extends ViewWrapper<ViewPager> {

	/**
	 * This library provides objects to implement a ViewPager. 
	 */
	public static void LIBRARY_DOC() {

	}

	protected String mEventName;
	
	/**
	 * Initializes the object.
	 * 
	 * Layout - A fully initialized AHPagerLayout object with the content of the pages 
	 * EventName - Sets the sub that will handle the event.
	 */
	public void Initialize(BA ba, AHPageContainer Layout, String EventName)
	{
		super.Initialize(ba, EventName);
		Layout.mEventName = EventName.toLowerCase();
		((ViewPager)getObject()).setAdapter(Layout);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName, boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new ViewPager(ba.context));
		super.innerInitialize(ba, EventName, true);
		mEventName = EventName;
		if (ba.subExists(EventName + "_pagechanged")) {
			((ViewPager)getObject()).setOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageSelected(int arg0) {
					ba.raiseEvent(getObject(), EventName + "_pagechanged", new Object[] {((ViewPager)getObject()).getCurrentItem()});
				}
			});
		}

	}	

	public AHViewPager() {
		super();
	}

	/**
	 * Jump or Scroll to the new page
	 * Page - The new page to display
	 * Smooth - True to smoothly scroll to the new item, false to transition immediately
	 */
	public void GotoPage(int Page, boolean Smooth) {
		((ViewPager)getObject()).setCurrentItem(Page, Smooth);
	}
	
	/**
	 * Get or Set the current page
	 */
	public void setCurrentPage(int Page) {
		((ViewPager)getObject()).setCurrentItem(Page);
	}

	public int getCurrentPage() {
		return ((ViewPager)getObject()).getCurrentItem();
	}

}