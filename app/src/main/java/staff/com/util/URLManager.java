package staff.com.util;

/**
 * Created by Caesar on 4/21/2017.
 */

public class URLManager {
    public static String getDownloadStockInfoURL() {
        return "http://glideadmin.co.uk/json/stocktake-download.php";
    }

    public static String getCheltenhamDownURL() {
        return "http://glideadmin.co.uk/json/stocktake-download-chel.php";
    }

    public static String getWessexDownURL() {
        return "http://glideadmin.co.uk/json/stocktake-download-wess.php";
    }

    public static String getUploadStockInfoURL() {
//        return "http://glideadmin.co.uk/json/deliverySend.php";
        return "http://glideadmin.co.uk/json/stocktake-upload.php";
    }

    public static String getUploadModifiedStockURL() {
//        return "http://glideadmin.co.uk/json/deliverySend.php";
        return "http://glideadmin.co.uk/json/stocktake-upload.php";
    }

    public static String getLoginURL() {
        return "http://glideadmin.co.uk/json/login.php";
    }
}