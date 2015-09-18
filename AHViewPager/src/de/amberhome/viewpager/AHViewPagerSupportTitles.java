package de.amberhome.viewpager;

import android.support.v4.view.PagerTitleStrip;
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
@ShortName("AHViewPagerSupportTitles")
public class AHViewPagerSupportTitles extends ViewWrapper<PagerTitleStrip> {

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
			setObject(new PagerTitleStrip(ba.context));
		super.innerInitialize(ba, EventName, true);
	}	

	public AHViewPagerSupportTitles() {
		super();
	}
	
	/**
	 * Sets the text color
	 */
	public void setTextColor(int Color) {
		getObject().setTextColor(Color);
	}

	/**
	 * Sets the text size
	 */
	public void setTextSize(float Size) {
		getObject().setTextSize(TypedValue.COMPLEX_UNIT_SP, Size);
	}
}
