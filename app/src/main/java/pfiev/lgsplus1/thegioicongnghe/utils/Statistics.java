package pfiev.lgsplus1.thegioicongnghe.utils;

public class Statistics {
    public static final String BASE_URL = "https://yshuynh.pythonanywhere.com/api/";

    public static final int TAB_WAITING_CONFIRM_INDEX = 0;
    public static final int TAB_CONFIRMED_INDEX = 1;
    public static final int TAB_SHIPPING_INDEX = 2;
    public static final int TAB_SUCCESS_INDEX = 3;
    public static final int TAB_CANCEL_INDEX = 4;

    public static final String WAITING_CONFIRM = "waiting_confirm";
    public static final String CONFIRMED_ORDER = "confirmed";
    public static final String SHIPPING_ORDER = "shipping";
    public static final String SUCCESS_ORDER = "success";
    public static final String CANCEL_ORDER = "cancel";

    public static String getStatusText(String status){
        String sttText = "";
        switch (status){
            case WAITING_CONFIRM:
                sttText = "Chờ xác nhận";
                break;
            case CONFIRMED_ORDER:
                sttText = "Đã xác nhận";
                break;
            case SHIPPING_ORDER:
                sttText = "Đang vận chuyển";
                break;
            case SUCCESS_ORDER:
                sttText = "Đã thành công";
                break;
            case CANCEL_ORDER:
                sttText = "Đã huỷ";
                break;
            default: break;
        }
        return sttText;
    }
}
