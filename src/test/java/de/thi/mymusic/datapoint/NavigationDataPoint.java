package de.thi.mymusic.datapoint;

/**
 * Created by Michael on 31.12.2015.
 */
public class NavigationDataPoint {

    private String viewName;
    private String navigationName;

    public NavigationDataPoint(String viewName, String navigationName) {
        this.viewName = viewName;
        this.navigationName = navigationName;
    }

    public String getNavigationName() {
        return navigationName;
    }

    public String getViewName() {
        return viewName;
    }
}
