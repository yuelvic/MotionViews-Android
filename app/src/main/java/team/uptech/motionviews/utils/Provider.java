package team.uptech.motionviews.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xeleb.motionviews.viewmodel.Mention;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by emman on 10/5/17.
 */

public class Provider {

    private static final String PROFILES = "[\n" +
            "    {\n" +
            "      \"id\": 19,\n" +
            "      \"nickname\": \"ella\",\n" +
            "      \"mobile\": \"09178450831\",\n" +
            "      \"email\": \"ella@xurpas.com\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 415,\n" +
            "      \"gifts\": 837,\n" +
            "      \"followers\": 9,\n" +
            "      \"followees\": 23,\n" +
            "      \"follow\": 0,\n" +
            "      \"coins\": 99910089,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \";)\",\n" +
            "      \"token\": \"6e59fbe7fc79e37c3290681ea4555169\",\n" +
            "      \"fcm\": \"35272973-4355-4036-8f05-8d982f6986d2\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": null,\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": true,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": \"Ella Malapitan\",\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/19/pics/ella_mid.jpg\",\n" +
            "      \"cover_pic_url\": null,\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 663,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 15,\n" +
            "      \"nickname\": \"jay\",\n" +
            "      \"mobile\": \"0915173542512131\",\n" +
            "      \"email\": \"jaymark.macaranas@xurpas.com1\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 0,\n" +
            "      \"gifts\": 0,\n" +
            "      \"followers\": 6,\n" +
            "      \"followees\": 17,\n" +
            "      \"follow\": 808,\n" +
            "      \"coins\": 71439,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"Cool\",\n" +
            "      \"token\": \"14e5c2281a0098d7a3f7cd7f626d71ba\",\n" +
            "      \"fcm\": \"d98abfbf-0d08-4afc-8eb5-b633fc445891\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": \"12345\",\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": true,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": null,\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/15/pics/jay20170811152958_mid.jpg\",\n" +
            "      \"cover_pic_url\": \"http://play.xeleblive.com/15/pics/jay_c.jpg\",\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 0,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 96,\n" +
            "      \"nickname\": \"andyg830\",\n" +
            "      \"mobile\": \"09178306692\",\n" +
            "      \"email\": \"\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 360,\n" +
            "      \"gifts\": 2653,\n" +
            "      \"followers\": 15,\n" +
            "      \"followees\": 27,\n" +
            "      \"follow\": 812,\n" +
            "      \"coins\": 19953869,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"\",\n" +
            "      \"token\": \"982f6810c7d3ec31f429db8acfc5ab68\",\n" +
            "      \"fcm\": \"5d1b77b3-03fa-4138-a7ab-3792521b40bd\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": null,\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": false,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": null,\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/96/pics/andyg83020170815181918_mid.jpg\",\n" +
            "      \"cover_pic_url\": null,\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 346,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": false\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 21,\n" +
            "      \"nickname\": \"larsenheim\",\n" +
            "      \"mobile\": \"09055263986\",\n" +
            "      \"email\": \"qaxurpas@gmail.come\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 75,\n" +
            "      \"gifts\": 865,\n" +
            "      \"followers\": 10,\n" +
            "      \"followees\": 17,\n" +
            "      \"follow\": 813,\n" +
            "      \"coins\": 0,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"wew\",\n" +
            "      \"token\": \"9aa862e9dfbea83a82a9923e78bd5300\",\n" +
            "      \"fcm\": \"3f827e8b-7ee9-433d-9077-3750a84eb342\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": \"1392896744135384\",\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": false,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": \"larse\",\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/21/pics/larsenheim20170907162350_mid.jpg\",\n" +
            "      \"cover_pic_url\": \"http://play.xeleblive.com/21/pics/larsenheim_c.jpg\",\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 392,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 215,\n" +
            "      \"nickname\": \"chammytoralba\",\n" +
            "      \"mobile\": \"?9999999999\",\n" +
            "      \"email\": \"charmaine.toralba@xurpas.com\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 890005,\n" +
            "      \"gifts\": 3555,\n" +
            "      \"followers\": 13,\n" +
            "      \"followees\": 8,\n" +
            "      \"follow\": 740,\n" +
            "      \"coins\": 93617,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"@chammytoralba\\n02 3427910\\n09150723680\\ncharmaine.toralba@gmail.com\",\n" +
            "      \"token\": \"23e63914111326b7c22a6b38e1dc15a5\",\n" +
            "      \"fcm\": \"8cf2454f-4a37-4baf-8c48-f98eff8e938e\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": \"\",\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": false,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 1,\n" +
            "      \"fullName\": \"Chammy Toralba\",\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/215/pics/3cfe8d1aeb172c5ed448a6459a68a31a20170918174531_mid.jpg\",\n" +
            "      \"cover_pic_url\": \"http://play.xeleblive.com/215/pics/chammytoralba20170921192402_c.jpg\",\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"\\n10-05 13:41:12.235 7063-3194/com.xeleb.xeleblive D/XL: is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 3789,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"nickname\": \"mcytg\",\n" +
            "      \"mobile\": \"666\",\n" +
            "      \"email\": \"ddr@konami.jp\",\n" +
            "      \"verified\": true,\n" +
            "      \"likes\": 353,\n" +
            "      \"gifts\": 433,\n" +
            "      \"followers\": 9,\n" +
            "      \"followees\": 39,\n" +
            "      \"follow\": 0,\n" +
            "      \"coins\": 978148,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"DDR\",\n" +
            "      \"token\": \"e6cc224584bef622ecf2a80099e093c7\",\n" +
            "      \"fcm\": \"05800702-eb8f-41a3-a0f7-28bfca028ea5\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": \"\",\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"globe\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": false,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": \"yongers\",\n" +
            "      \"pic_url\": \"http://play.xeleblive.com/2/pics/mcytg20170811154335_mid.jpg\",\n" +
            "      \"cover_pic_url\": \"\",\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 296,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": true\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 12,\n" +
            "      \"nickname\": \"doods\",\n" +
            "      \"mobile\": \"09178219802\",\n" +
            "      \"email\": \"\",\n" +
            "      \"verified\": false,\n" +
            "      \"likes\": 88,\n" +
            "      \"gifts\": 0,\n" +
            "      \"followers\": 7,\n" +
            "      \"followees\": 31,\n" +
            "      \"follow\": 0,\n" +
            "      \"coins\": 16891,\n" +
            "      \"earnings\": 0,\n" +
            "      \"bio\": \"bioman\",\n" +
            "      \"token\": null,\n" +
            "      \"fcm\": \"\",\n" +
            "      \"restrictions\": null,\n" +
            "      \"payment\": 0,\n" +
            "      \"fbId\": null,\n" +
            "      \"account_settings\": null,\n" +
            "      \"notif_settings\": null,\n" +
            "      \"contact_shop_info\": null,\n" +
            "      \"telco\": \"chinese\",\n" +
            "      \"recent_searches\": null,\n" +
            "      \"direct_message\": false,\n" +
            "      \"is_tester\": false,\n" +
            "      \"video_count\": 0,\n" +
            "      \"fullName\": null,\n" +
            "      \"pic_url\": \"\",\n" +
            "      \"cover_pic_url\": null,\n" +
            "      \"is_subscribed\": null,\n" +
            "      \"is_shareholder\": false,\n" +
            "      \"recent_cash_out\": 0,\n" +
            "      \"total_views\": 28,\n" +
            "      \"is_active\": null,\n" +
            "      \"is_following\": null,\n" +
            "      \"complete_profile\": false\n" +
            "    }\n" +
            "  ]";

    public static List<Mention> getProfiles() {
        Type type = new TypeToken<List<Mention>>(){}.getType();
        return new Gson().fromJson(PROFILES, type);
    }

}
