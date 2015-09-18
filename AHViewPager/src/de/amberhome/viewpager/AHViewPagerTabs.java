package de.amberhome.viewpager;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.DontInheritEvents;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.ViewWrapper;
import de.amberhome.viewpager.internal.ViewPagerTabs;


@ActivityObject
@DontInheritEvents
@ShortName("AHViewPagerTabs")
/**
 * The AHViewPagerTabs object adds a page indicator and tab selector to a AHViewPager object.
 *
 */
public class AHViewPagerTabs extends ViewWrapper<ViewPagerTabs> {

	/**
	 * Initializes the object.
	 * 
	 * Pager - A fully initialized AHViewPager object. 
	 */
	public void Initialize(BA ba, AHViewPager Pager)
	{
		super.Initialize(ba, "");

		((ViewPagerTabs)getObject()).setViewPager(Pager, Pager.mEventName);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName, boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new ViewPagerTabs(ba));
		super.innerInitialize(ba, EventName, true);
	}	

	public AHViewPagerTabs() {
		super();
	}
	
	/**
	 * Gets the current position in the tabs.
	 */
	public int getPosition() {
		return ((ViewPagerTabs)getObject()).getPosition();
	}
	
	public void setTabPaddingLeft(int padding) {
		((ViewPagerTabs)getObject()).setTabPaddingLeft(padding);
	}

	public int getTabPaddingLeft() {
		return ((ViewPagerTabs)getObject()).getTabPaddingLeft();
	}
	
	public void setTabPaddingTop(int padding) {
		((ViewPagerTabs)getObject()).setTabPaddingTop(padding);
	}

	public int getTabPaddingTop() {
		return ((ViewPagerTabs)getObject()).getTabPaddingTop();
	}

	public void setTabPaddingRight(int padding) {
		((ViewPagerTabs)getObject()).setTabPaddingRight(padding);
	}

	public int getTabPaddingRight() {
		return ((ViewPagerTabs)getObject()).getTabPaddingRight();
	}

	public void setTabPaddingBottom(int padding) {
		((ViewPagerTabs)getObject()).setTabPaddingBottom(padding);
	}

	public int getTabPaddingBottom() {
		return ((ViewPagerTabs)getObject()).getTabPaddingBottom();
	}

	public void SetTabPadding(int Left, int Top, int Right, int Bottom) {
		((ViewPagerTabs)getObject()).setTabPadding(Left, Top, Right, Bottom);
	}
	
	public void setBackgroundColorPressed(int color) {
		((ViewPagerTabs)getObject()).setBackgroundColorPressed(color);
	}

	public int getBackgroundColorPressed() {
		return ((ViewPagerTabs)getObject()).getBackgroundColorPressed();
	}
	
	public void setTextSize(float size) {
		((ViewPagerTabs)getObject()).setTextSize(size);
	}

	public float getTextSize() {
		return ((ViewPagerTabs)getObject()).getTextSize();
	}

	public void setTextColor(int color) {
		((ViewPagerTabs)getObject()).setTextColor(color);
	}

	public int getTextColor() {
		return ((ViewPagerTabs)getObject()).getTextColor();
	}

	public void setTextColorCenter(int color) {
		((ViewPagerTabs)getObject()).setTextColorCenter(color);
	}

	public int getTextColorCenter() {
		return ((ViewPagerTabs)getObject()).getTextColorCenter();
	}

	public void setLineColorCenter(int color) {
		((ViewPagerTabs)getObject()).setLineColorCenter(color);
	}

	public int getLineColorCenter() {
		return ((ViewPagerTabs)getObject()).getLineColorCenter();
	}

	public void setLineHeight(int height) {
		((ViewPagerTabs)getObject()).setLineHeight(height);
	}

	public int getLineHeight() {
		return ((ViewPagerTabs)getObject()).getLineHeight();
	}

	public void setOutsideOffset(int offset) {
		((ViewPagerTabs)getObject()).setOutsideOffset(offset);
	}
	
	public int getOutsideOffset() {
		return ((ViewPagerTabs)getObject()).getOutsideOffset();
	}
	
	public void setUpperCaseTitle(boolean Flag) {
		((ViewPagerTabs)getObject()).setUpperCaseTitle(Flag);
	}
	
	/**
	 * Rebuilds the complete tabs data structure. You MUST call this after you add or remove a page from the AHViewPager.
	 */
	public void NotifyDataChange() {
		((ViewPagerTabs)getObject()).notifyDatasetChanged();
	}
	
	/**
	 * Refresh the titles of the tabs. Use this after changing a title in AHPageContainer.
	 * 
	 */
	public void RefreshTitles() {
		((ViewPagerTabs)getObject()).refreshTitles();
	}
	
}
