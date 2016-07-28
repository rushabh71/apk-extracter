package rushabh.apk.extracter;


import android.graphics.drawable.Drawable;

public class AppInfo {

    private String app_name, package_name;
    Drawable app_image;

    public Drawable getApp_image() {
        return app_image;
    }

    public void setApp_image(Drawable app_image) {
        this.app_image = app_image;
    }

    public String getApp_name() {
        return app_name;

    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }
}