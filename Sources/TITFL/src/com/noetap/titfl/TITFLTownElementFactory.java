package com.noetap.titfl;

import android.app.Activity;

public class TITFLTownElementFactory 
{

    public static TITFLLayout createLayout(Activity activity, TITFLTownElement element)
    {
        TITFLLayout layout = null;
        int layoutId = 0;

        if (element.isHome())
        {
            layoutId = R.layout.townelement_home;
            layout = new TITFLTownElementHomeLayout(activity, element);
        }
        else if (element.isSchool())
        {
            layoutId = R.layout.townelement_school;
            layout = new TITFLTownElementSchoolLayout(activity, element);
        }
        else
        {
            layoutId = R.layout.townelement;
            layout = new TITFLTownElementLayout(activity, element);
        }
        
        activity.setContentView(layoutId);
        return layout;
    }
}
