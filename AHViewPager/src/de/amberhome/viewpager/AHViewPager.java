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
//    - Designer Support
//    - some minor internal changes
// History now on Github

package de.amberhome.viewpager;

import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.DependsOn;
import anywheresoftware.b4a.BA.Events;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.keywords.Common.DesignerCustomView;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.collections.Map;
import de.amberhome.viewpager.internal.CustomViewPager;

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
public class AHViewPager extends ViewWrapper<CustomViewPager> implements
DesignerCustomView {

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
	public void Initialize2(BA ba, AHPageContainer Container, String EventName) {
		super.Initialize(ba, EventName);
		Container.mEventName = EventName.toLowerCase();
		getObject().setAdapter(Container);
	}

	/**
	 * Initializes the object.
	 * 
	 * pages EventName - Sets the sub that will handle the event.
	 */
	public void Initialize(BA ba, String EventName) {
		super.Initialize(ba, EventName);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName,
			boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new CustomViewPager(ba.context));
		super.innerInitialize(ba, EventName, true);
		mEventName = EventName;

		getObject().addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

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
		getObject().setCurrentItem(Page, Smooth);
	}

	/**
	 * Get or Set the current page
	 */
	public void setCurrentPage(int Page) {
		getObject().setCurrentItem(Page);
	}

	public int getCurrentPage() {
		return getObject().getCurrentItem();
	}

	/**
	 * Set the number of pages that should be retained to either side of the
	 * current page in the view hierarchy in an idle state. Pages beyond this
	 * limit will be recreated from the adapter when needed.
	 *
	 * This is offered as an optimization. If you know in advance the number
	 * of pages you will need to support or have lazy-loading mechanisms in place
	 * on your pages, tweaking this setting can have benefits in perceived smoothness
	 * of paging animations and interaction. If you have a small number of pages (3-4)
	 * that you can keep active all at once, less time will be spent in layout for newly
	 * created view subtrees as the user pages back and forth.
	 * 
	 * You should keep this limit low, especially if your pages have complex layouts. This setting defaults to 1.
	 */
	public void setOffscreenPageLimit(int Limit) {
		getObject().setOffscreenPageLimit(Limit);
	}
	
	public int getOffscreenPageLimit() {
		return getObject().getOffscreenPageLimit();
	}
	
	/**
	 * Set the margin between pages.
	 */
	public void setPageMargin(int Margin) {
		getObject().setPageMargin(Margin);
	}
	
	public int getPageMargin() {
		return getObject().getPageMargin();
	}
	
	/**
	 * Enables or disables the paging of the ViewPager.
	 */
	public void setPagingEnabled(boolean Enabled) {
		getObject().setPagingEnabled(Enabled);
	}

	public boolean getPagingEnabled() {
		return getObject().getPagingEnabled();
	}

	public void setPageContainer(AHPageContainer Container) {
		Container.mEventName = mEventName;
		getObject().setAdapter(Container);
	}
	
	@Override
	@Hide
	public void _initialize(BA ba, Object activityClass, String EventName) {
		Initialize(ba, EventName);
	}
	
	/**
	 * This method is only for the B4A Designer. Don't call it directly
	 */
	@Override
	public void DesignerCreateView(PanelWrapper base, LabelWrapper label,
			Map props) {
		this.setBackground(base.getBackground());

		((ViewGroup) base.getObject().getParent()).addView(this.getObject(),  base.getObject().getLayoutParams());
		base.RemoveView();

		this.setTag(base.getTag());
		this.setVisible(base.getVisible());
		this.setEnabled(base.getEnabled());
	}

	/**
	 * Invalidate a rectangualar part of the object
	 */
	public void Invalidate2(Rect Rect) {
		super.Invalidate2(Rect);
	}

	/**
	 * Invalidate part of the object
	 */
	public void Invalidate3(int Left, int Top, int Right, int Bottom) {
		super.Invalidate3(Left, Top, Right, Bottom);
	}

	/**
	 * Animate the color to a new value
	 */
	public void SetColorAnimated(int Duration, int FromColor, int ToColor) {
		super.SetColorAnimated(Duration, FromColor, ToColor);
	}
	
	/**
	 * Fade the view in or out
	 */
	public void SetVisibleAnimated(int Duration, boolean Visible) {
		super.SetVisibleAnimated(Duration, Visible);
	}

	/**
	 * Similar to SetLayout. Animates the change. Note that the animation will only be applied when running on Android 3+ devices.
	 * 
	 * Duration - duration of the layout change
	 */
	@Override
	public void SetLayoutAnimated(int Duration, int Left, int Top, int Width,
			int Height) {
		super.SetLayoutAnimated(Duration, Left, Top, Width, Height);
	}

	/**
	 * Changes the View position and size.
	 */
	@Override
	public void SetLayout(int Left, int Top, int Width, int Height) {
		super.SetLayout(Left, Top, Width, Height);
	}

	/**
	 * Gets or sets the view's left position
	 */
	@Override
	public void setLeft(int Left) {
		super.setLeft(Left);
	}

	@Override
	public int getLeft() {
		return super.getLeft();
	}

	/**
	 * Gets or sets the view's top position
	 */
	@Override
	public void setTop(int Top) {
		super.setTop(Top);
	}

	@Override
	public int getTop() {
		return super.getTop();
	}

	/**
	 * Gets or sets the view's width
	 */
	@Override
	public void setWidth(int Width) {
		super.setWidth(Width);
	}

	@Override
	public int getWidth() {
		return super.getWidth();
	}

	/**
	 * Gets or sets the view's height
	 */
	@Override
	public void setHeight(int Height) {
		super.setHeight(Height);
	}

	@Override
	public int getHeight() {
		return super.getHeight();
	}
}
