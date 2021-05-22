package com.sharlock.parts;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class SharlockUtil {
    private static final boolean DEBUG = false;
    private static final String TAG = "SharlockUtil";
    private static SharlockUtil s_instance = null;

    public static SharlockUtil getInstance() {
        if( s_instance == null ) {
            s_instance = new SharlockUtil();
        }
        return s_instance;
    }

    public void viewSetActivated(View view, boolean activated) {
        view.setActivated(activated);
    }

    public boolean hasPermanentMenuKey(ViewConfiguration vcfg) {
        return vcfg.hasPermanentMenuKey();
    }

    public void jumpDrawablesToCurrentState(View v) {
        v.jumpDrawablesToCurrentState();
    }

    public Drawable getActivityLogo(Context context) {
        Drawable mLogo = null;
        ApplicationInfo appInfo = context.getApplicationInfo();
        PackageManager pm = context.getPackageManager();
        if (context instanceof Activity) {
            try {
                mLogo = pm.getActivityLogo(((Activity) context).getComponentName());
            } catch ( PackageManager.NameNotFoundException e) {
                Log.e("Utility", "Activity component name not found!", e);
            }
        }
        if (mLogo == null) {
            mLogo = appInfo.loadLogo(pm);
        }
        return mLogo;
    }


    /**
     * Attempt to programmatically load the logo from the manifest file of an
     * activity by using an XML pull parser. This should allow us to read the
     * logo attribute regardless of the platform it is being run on.
     *
     * @param activity Activity instance.
     * @return Logo resource ID.
     */
    /****
    private static int loadLogoFromManifest(Activity activity) {
        int logo = 0;
        try {
            final String thisPackage = activity.getClass().getName();
            if (DEBUG) Log.i(TAG, "Parsing AndroidManifest.xml for " + thisPackage);

            final String packageName = activity.getApplicationInfo().packageName;
            final AssetManager am = activity.createPackageContext(packageName, 0).getAssets();
            final XmlResourceParser xml = am.openXmlResourceParser("AndroidManifest.xml");

            int eventType = xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = xml.getName();

                    if ("application".equals(name)) {
                        //Check if the <application> has the attribute
                        if (DEBUG) Log.d(TAG, "Got <application>");

                        for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
                            if (DEBUG) Log.d(TAG, xml.getAttributeName(i) + ": " + xml.getAttributeValue(i));

                            if ("logo".equals(xml.getAttributeName(i))) {
                                logo = xml.getAttributeResourceValue(i, 0);
                                break; //out of for loop
                            }
                        }
                    } else if ("activity".equals(name)) {
                        //Check if the <activity> is us and has the attribute
                        if (DEBUG) Log.d(TAG, "Got <activity>");
                        Integer activityLogo = null;
                        String activityPackage = null;
                        boolean isOurActivity = false;

                        for (int i = xml.getAttributeCount() - 1; i >= 0; i--) {
                            if (DEBUG) Log.d(TAG, xml.getAttributeName(i) + ": " + xml.getAttributeValue(i));

                            //We need both uiOptions and name attributes
                            String attrName = xml.getAttributeName(i);
                            if ("logo".equals(attrName)) {
                                activityLogo = xml.getAttributeResourceValue(i, 0);
                            } else if ("name".equals(attrName)) {
                                activityPackage = ActionBarSherlockCompat.cleanActivityName(packageName, xml.getAttributeValue(i));
                                if (!thisPackage.equals(activityPackage)) {
                                    break; //on to the next
                                }
                                isOurActivity = true;
                            }

                            //Make sure we have both attributes before processing
                            if ((activityLogo != null) && (activityPackage != null)) {
                                //Our activity, logo specified, override with our value
                                logo = activityLogo.intValue();
                            }
                        }
                        if (isOurActivity) {
                            //If we matched our activity but it had no logo don't
                            //do any more processing of the manifest
                            break;
                        }
                    }
                }
                eventType = xml.nextToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (DEBUG) Log.i(TAG, "Returning " + Integer.toHexString(logo));
        return logo;
    }
     ****/

    public CharSequence stringToUpper(CharSequence text) {
        if(text != null) {
            return text.toString().toUpperCase(Locale.ROOT);
        }
        return null;
    }

    public PopupWindow buildPopupWindow(Context context, AttributeSet attrs, int defStyleAttr,
                                        int defStyleRes) {
        return new PopupWindow(context, attrs, defStyleAttr, defStyleRes);
    }

    public void jumpToCurrentState(Drawable indeterminateDrawable) {
        // Need to be implemented ?

    }

    public int resolveSizeAndState(int size, int measureSpec, int state) {
        return View.resolveSizeAndState(size, measureSpec, state);
    }

    public int getMeasuredState(View child) {
        return child.getMeasuredState();
    }

    public int combineMeasuredStates(int curState, int newState) {
        return View.combineMeasuredStates(curState, newState);
    }

    public boolean isLongPressEvent(KeyEvent evt) {
        return evt.isLongPress();
    }

    public void setBackgroundDrawable(View v, Drawable d) {
        v.setBackgroundDrawable(d);
    }

    public void setLinearLayoutDividerPadding(LinearLayout l, int padding) {
        l.setDividerPadding(padding);
    }

    public void setLinearLayoutDividerDrawable(LinearLayout l, Drawable drawable) {
        l.setDividerDrawable(drawable);
    }

    public static Method safelyGetSuperclassMethod(Class<?> cls, String methodName, Class<?>... parametersType) {
        Class<?> sCls = cls.getSuperclass();
        while(sCls != Object.class) {
            try {
                return sCls.getDeclaredMethod(methodName, parametersType);
            } catch (NoSuchMethodException e) {
                // Just super it again
            }
            sCls = sCls.getSuperclass();
        }
        throw new RuntimeException("Method not found " + methodName);
    }

    public static Object safelyInvokeMethod(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
            Log.e("Safe invoke fail", "Invalid args", e);
        } catch (IllegalAccessException e) {
            Log.e("Safe invoke fail", "Invalid access", e);
        } catch ( InvocationTargetException e) {
            Log.e("Safe invoke fail", "Invalid target", e);
        }

        return null;
    }

}
