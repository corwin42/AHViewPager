package de.amberhome.viewpager;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.TypedValue;
import android.view.Gravity;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.DontInheritEvents;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.ViewWrapper;

@ActivityObject
@DontInheritEvents
@ShortName("AHViewPagerSupportTabs")
public class AHViewPagerSupportTabs extends ViewWrapper<PagerTabStrip> {

	/**
	 * Initializes the object.
	 * 
	 * Pager - A fully initialized AHViewPager object.
	 * Top - Flag if the indicator should be displayed on top or bottom of the ViewPager
	 * Height - The height of the indicator view. 
	 */
	public void Initialize(BA ba, AHViewPager Pager, boolean Top, int Height)
	{
		super.Initialize(ba, "");
		
		LayoutParams lp = new LayoutParams();
		lp.height = Height;
	    lp.width = LayoutParams.MATCH_PARENT;
	    if (Top)
	    	lp.gravity = Gravity.TOP;
	    else
	    	lp.gravity = Gravity.BOTTOM;

		Pager.getObject().addView(getObject(), lp);
	}

	@Hide
	public void innerInitialize(final BA ba, final String EventName, boolean keepOldObject) {
		if (!keepOldObject)
			setObject(new PagerTabStrip(ba.context));
		super.innerInitialize(ba, EventName, true);
	}	

	public AHViewPagerSupportTabs() {
		super();
	}
	
	/**
	 * Sets the text color
	 */
	public void setTextColor(int Color) {
		getObject().setTextColor(Color);
	}

	/**
	 * Sets or gets the tab indicator color
	 */
	public void setTabIndicatorColor(int Color) {
		getObject().setTabIndicatorColor(Color);
	}
	
	public int getTabIndicatorColor() {
		return getObject().getTabIndicatorColor();
	}
	
	/**
	 * Sets or gets the flag if the text is undelined completely
	 */
	public void setDrawFullUnderline(boolean Full) {
		getObject().setDrawFullUnderline(Full);
	}
	
	public boolean getDrawFullUnderline() {
		return getObject().getDrawFullUnderline();
	}
	
	/**
	 * Sets the text size
	 */
	public void setTextSize(float Size) {
		getObject().setTextSize(TypedValue.COMPLEX_UNIT_SP, Size);
	}
}
