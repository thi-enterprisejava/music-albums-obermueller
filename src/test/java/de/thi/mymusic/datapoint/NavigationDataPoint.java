package de.thi.mymusic.datapoint;

/**
 * Help Entity for DataPoints to test navigation css styling
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
