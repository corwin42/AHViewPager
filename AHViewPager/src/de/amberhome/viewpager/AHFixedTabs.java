package de.amberhome.viewpager;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.DontInheritEvents;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.ViewWrapper;

@ActivityObject
@DontInheritEvents
@ShortName("AHViewPagerFixedTabs")
/**
 * The AHFixedTabsView object adds a page indicator and tab selector to a AHViewPager object.
 *
 */
public class AHFixedTabs extends ViewWrapper<FixedTabsView> {

	/**
	 * Initializes the object.
	 * 
	 * Pager - A fully initialized AHViewPager object. 
	 */
	public void Initialize(BA ba, AHViewPager Pager)
	{
		super.Initialize(ba, "");

		((FixedTabsView)getObject()).setViewPager((CustomViewPager)Pager.getObject(), Pager.mEventName);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName, boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new FixedTabsView(ba));
		super.innerInitialize(ba, EventName, true);
	}	

	public AHFixedTabs() {
		super();
	}
	
	/**
	 * Gets the current position in the tabs.
	 */
//	public void setTabPaddingLeft(int padding) {
//		((FixedTabsView)getObject()).setTabPaddingLeft(padding);
//	}
//
//	public int getTabPaddingLeft() {
//		return ((FixedTabsView)getObject()).getTabPaddingLeft();
//	}
//	
//	public void setTabPaddingTop(int padding) {
//		((FixedTabsView)getObject()).setTabPaddingTop(padding);
//	}
//
//	public int getTabPaddingTop() {
//		return ((FixedTabsView)getObject()).getTabPaddingTop();
//	}
//
//	public void setTabPaddingRight(int padding) {
//		((FixedTabsView)getObject()).setTabPaddingRight(padding);
//	}
//
//	public int getTabPaddingRight() {
//		return ((FixedTabsView)getObject()).getTabPaddingRight();
//	}
//
//	public void setTabPaddingBottom(int padding) {
//		((FixedTabsView)getObject()).setTabPaddingBottom(padding);
//	}
//
//	public int getTabPaddingBottom() {
//		return ((FixedTabsView)getObject()).getTabPaddingBottom();
//	}
//
//	public void SetTabPadding(int Left, int Top, int Right, int Bottom) {
//		((FixedTabsView)getObject()).setTabPadding(Left, Top, Right, Bottom);
//	}
//	
	public void setTextSize(float size) {
		((FixedTabsView)getObject()).setTextSize(size);
	}

	public float getTextSize() {
		return ((FixedTabsView)getObject()).getTextSize();
	}

	public void setTextColor(int color) {
		((FixedTabsView)getObject()).setTextColor(color);
	}

	public int getTextColor() {
		return ((FixedTabsView)getObject()).getTextColor();
	}

	public void setLineColor(int color) {
		((FixedTabsView)getObject()).setLineColor(color);
	}

	public int getLineColor() {
		return ((FixedTabsView)getObject()).getLineColor();
	}

	public void setLineColorSelected(int color) {
		((FixedTabsView)getObject()).setLineColorSelected(color);
	}

	public int getLineColorSelected() {
		return ((FixedTabsView)getObject()).getLineColorSelected();
	}

	public void setLineHeight(int height) {
		((FixedTabsView)getObject()).setLineHeight(height);
	}

	public int getLineHeight() {
		return ((FixedTabsView)getObject()).getLineHeight();
	}

	public void setLineHeightSelected(int height) {
		((FixedTabsView)getObject()).setLineHeightSelected(height);
	}

	public int getLineHeightSelected() {
		return ((FixedTabsView)getObject()).getLineHeightSelected();
	}

	public void setUpperCaseTitle(boolean Flag) {
		((FixedTabsView)getObject()).setUpperCaseTitle(Flag);
	}
	
	/**
	 * Rebuilds the complete tabs data structure. You MUST call this after you add or remove a page from the AHViewPager.
	 */
	public void NotifyDataChange() {
		((FixedTabsView)getObject()).notifyDatasetChanged();
	}
	
	/**
	 * Refresh the titles of the tabs. Use this after changing a title in AHPageContainer.
	 * 
	 */
	public void RefreshTitles() {
		((FixedTabsView)getObject()).refreshTitles();
	}
	
}
