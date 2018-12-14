package com.example.sketchxu.mall;

public class Contants {

    public  static final String DES_KEY="Cniao5_123456";
    public static final String TOKEN = "token";
    public static final String USER_JSON = "user_json";
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE_PAYMENT = 1;

    public static class API {
        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";
        public static final String BANNER = BASE_URL + "banner/query?type=1";
        public static final String HOMECAMPAIGN = BASE_URL + "campaign/recommend";
        public static final String HOT_WARES = BASE_URL + "wares/hot";
        public static final String CATEGORY_LIST = BASE_URL + "category/list";
        public static final String WARES_LIST = BASE_URL + "wares/list";
        public static final String WARES_CAMPAIGN_LIST = BASE_URL + "wares/campaign/list";
        public static final String WARES_DETAIL = BASE_URL + "wares/detail.html";
        public static final String LOGIN = BASE_URL + "auth/login";
        public static final String USER_DETAIL = BASE_URL + "user/get?id=282392";
        public static final String REG = "https://www.cniao5.com/auth/reg.html";

        public static final String ORDER_CREATE=BASE_URL +"/order/create";
        public static final String ORDER_COMPLEPE=BASE_URL +"/order/complete";

        public static final String ADDRESS_LIST=BASE_URL +"addr/list";
        public static final String ADDRESS_CREATE=BASE_URL +"addr/create";
        public static final String ADDRESS_UPDATE=BASE_URL +"addr/update";
        public static final String ADDRESS_DELETE = BASE_URL + "addr/del";
        public static final String FAVORITE_LIST=BASE_URL +"favorite/list";
        public static final String FAVORITE_CREATE=BASE_URL +"favorite/create";

    }

    public static final String CAMPAIGN_ID = "campaign_id";
    public static final String WARE = "ware";
}
