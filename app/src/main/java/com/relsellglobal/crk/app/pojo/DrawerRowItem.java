package com.relsellglobal.crk.app.pojo;

/**
 * Created by anil on 12/7/15.
 */
public class DrawerRowItem {
    private String mTitle;
    private int mIcon;
    private boolean mActive;
    private boolean mSectionHeader;

    public boolean ismSectionHeader() {
        return mSectionHeader;
    }

    public void setmSectionHeader(boolean mSectionHeader) {
        this.mSectionHeader = mSectionHeader;
    }

    public boolean ismActive() {
        return mActive;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }
}
