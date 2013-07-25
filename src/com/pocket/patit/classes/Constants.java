package com.pocket.patit.classes;

/**
 * {@link Constants}
 * 
 * Clase con las constantes del sistema. Se puede acceder de forma estática a todos los 
 * elementos de la clase. 
 * Hay un elemento importante con la URL de la dirección a la que se conecta el servidor.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class Constants {
	
	
	
	public static int LOGIN_ACTIVITY_OK = 1;
	public static int REGISTER_ACTIVITY_OK = 2;
	public static int HOME_ACTIVITY_OK = 3;
	public static int MAIN_ACTIVITY_OK = 4;
	public static int YOUR_POCKET_ACTIVITY_OK = 5;
	public static final int NEW_POCKET_ACTIVITY_OK = 6;
	public static final int SELECT_NEW_ELE_ACTIVITY_OK = 7;
	public static final int RANDOM_POCKET_ACTIVITY_OK = 7;
	public static final int FEATURED_POCKET_ACTIVITY_OK = 8;
	public static final int SPONSORED_POCKET_ACTIVITY_OK = 9;
	public static final int DESCRIPTION_POCKET_OK = 10;
	public static final int INFOUSER_ACTIVITY_OK = 11;
	public static final int UPDATE_POCKET_OK = 12;
	public static final int VIEW_OBJECT_ACTIVITY_OK = 13;
	public static final int DESCRIPTION_OBJECT_OK = 14;
	public static final int EDIT_POCKET_ACTIVITY_OK = 15;
	public static final int SEARCH_POCKET_ACTIVITY_OK = 16;
	public static final int RESULT_OK = 1;
	public static final int RESULT_FAIL = 0;
	
	public static final int PREFERENCES_ACTIVITY_OK = 10;
	public static final int GALLERYITEM_RESULT = 11;
	
	
	public static int YOUR_POCKETS_LIST_POSITION = 0;
	public static int RANDOM_POCKETS_LIST_POSITION = 1;
	public static int FEATURED_POCKETS_LIST_POSITION = 2;
	public static int SPONSORED_POCKETS_LIST_POSITION = 3;
	public static final int SEARCH_POCKETS_LIST_POSITION = 4;
	
	public static final String API_RESULT = "result";
	public static final String API_RESULT_OK = "OK";
	public static final String API_RESULT_KO = "KO";
	public static final String API_RESULT_OK_CHANGED_VOTE = "OK_CHANGED_VOTED";
	public static final String API_KO_VOTE_PREVIOUSLY = "KO_VOTE_PREVIOUSLY";
	
	public static String SHAREDPREFERENCES_LOGIN = "login";
	public static String SHAREDPREFERENCES_DATASTORAGE = "data";
	public static String SHAREDPREFERENCES_PREFERENCES = "pref";
	
	public static String FIRST_USAGE = "first_usage";
	public static String DIR_CREATED = "dir_created";
	
	public static String DIR_IN_MEM_YOURPOCKETS = "yourpocket.txt";
	public static String DIR_IN_MEM_RANDOMPOCKETS = "randompocket.txt";
	public static String DIR_IN_MEM_FEATUREDPOCKETS = "featuredpockets.txt";
	public static String DIR_IN_MEM_SPONSOREDPOCKETS = "sponsoredpockets.txt";
	
	public static String HOME_TO_MAIN_FRAGMENT = "fragmentToShow";
	public static String MAIN_YOUR_TO_SINGLE_YOUR = "fragmentToYour";
	public static String MAIN_RANDOM_TO_SINGLE_RANDOM = "fragmentToRandom";
	public static String MAIN_FEATURED_TO_SINGLE_FEATURED = "fragmentToFeatured";
	public static String MAIN_SPONSORED_TO_SINGLE_SPONSORED = "fragmentToSponsored";
	public static final String SINGLE_TO_VIEW_OBJECT = "singleToView";
	
	public static final String NEW_POCKET_TO_SINGLE_YOUR = "newToYourPocket";
	public static final String PREFERENCES_LOGIN = "preferences_login";
	public static final String PREFERENCES_LOGIN_NICK = "pref_nick";
	public static final String PREFERENCES_LOGIN_EMAIL = "pref_email";
	public static final String PREFERENCES_LOGIN_PASS = "pref_pass";
	public static final String PREFERENCES_LOGIN_APIKEY = "pref_apikey";
	public static final String PREFERENCES_LOGIN_ID = "pref_id";
	public static final String PREFERENCES_ID_USER = "pref_id";
	public static final String PREFERENCES_FIRST_USE = "first_use";
	public static final String PREFERENCES_LOGIN_REGDATE = "reg_date";
	
	public static final String OBJECT = "object";
	public static String NICK = "nick";
	public static String PASS = "password";
	public static String EMAIL = "email";
	public static String API_KEY = "api_key";
	public static String NAME_POCKET = "name";
	public static String DESC_POCKET = "description";
	public static String THEME_POCKET = "type";
	public static String POCKETS = "pockets";
	public static final String POCKET = "pocket";
	public static String COMMENTS = "comments";
	public static final String COMMENT = "comment";
	public static final String ITEM = "ITEM";
	public static String POCKET_CREATE_DATE = "create_date";
	public static String POCKET_DESCRIPTION = "description";
	public static final String POCKET_TO_UPDATE = "pocket_to_update";
	public static String POCKET_LAST_MOD = "last_mod";
	public static String POCKET_NAME = "name";
	public static String POCKET_TYPE = "type";
	public static String POCKET_USER = "user";
	public static final String POCKET_NICK_USER = "nick_user";
	public static final String POCKET_ID = "id";
	public static String USER_ID = "id";
	public static String USER_NICK = "nick";
	public static String USER_OD_USER = "od_user";
	public static String COMMENT_CREATE_DATE ="create_date";
	public static String COMMENT_ID = "id";
	public static String COMMENT_USER_ID = "user_id";
	public static String COMMENT_POCKET = "pocket";
	public static String COMMENT_TEXT = "text";
	public static final String COMMENT_USER_NICK = "user_nick";
	public static final String FEATURED = "featured";
	public static final String FEATURED_POCKET = "pocket";
	public static final String FEATURED_ID = "id";
	public static final String FEATURED_ID_POCKET = "id_pocket";
	public static final String OBJECTS = "objects";
	public static final String USER = "user";

	public static final String VOTE = "vote";
	
	public static String POCKET_THEME_MISC = "miscelaneus";
	public static String POCKET_THEME_SPORT = "sport";
	public static String POCKET_THEME_MUSIC = "music";
	public static String POCKET_THEME_CINEMA = "cinema";
	
	//Object class type
	public static String OBJECT_TYPE_OTHER = "OTHER";
	public static String OBJECT_TYPE_IMG = "IMAGE";
	public static String OBJECT_TYPE_MSC = "MUSIC";
	public static String OBJECT_TYPE_TXT = "TEXT";
	public static String OBJECT_TYPE_URL = "URL"; 
	
	public static String POCKET_TO_DESCRIPTION = "pocket_esc";
	
	
	public static String API_URL = "192.168.0.195";
	public static final String N_RANDOM = "20";
	public static String API_REGISTER = "http://"+ API_URL +"/api/v1/register/?format=json";

	public static final String API_WEB_POCKET = "http://"+API_URL+"/web/index.html#pocket/";
	public static final String API_WEB_OBJECT = "http://"+API_URL+"/web/index.html#object/";
	public static final String API_LOGIN = "http://"+ API_URL +"/api/v1/login/?format=json";
	public static final String API_NEWPOCKET = "http://"+ API_URL +"/api/v1/newpocket/?format=json";
	public static final String API_DELETEPOCKET = "http://"+ API_URL +"/api/v1/deletepocket/";
	public static final String API_DELETEOBJECT = "http://"+ API_URL +"/api/v1/deleteobject/";
	public static final String API_GET_POCKET_USER = "http://"+API_URL+"/api/v1/user/";
	public static final String API_NEWFEATURED = "http://"+ API_URL +"/api/v1/newfeatured/";
	public static final String API_DELETEFEATURED = "http://"+ API_URL +"/api/v1/deletefeatured/";
	public static final String API_NEWCOMMENT = "http://"+ API_URL +"/api/v1/newcomment/?format=json";
	public static final String API_GET_SPONSORED_POCKET = "http://"+ API_URL +"/api/v1/sponsored/?format=json";
	public static final String API_GET_FEATURED_POCKET = "http://"+ API_URL +"/api/v1/pocket/set/";
	public static final String API_GET_SPONSORED_SET_POCKET = "http://"+ API_URL +"/api/v1/pocket/set/";
	public static final String API_GET_POCKET = "http://"+ API_URL +"/api/v1/pocket/";
	public static final String API_PATH_POCKET = "http://"+ API_URL +"/api/v1/pocket/";
	public static final String API_PUT_POCKET = "http://"+ API_URL +"/api/v1/updatepocket/";
	public static final String API_NEWVOTE = "http://"+ API_URL +"/api/v1/vote/?format=json";
	public static final String API_GET_RANDOM_POCKET = "http://"+ API_URL +"/api/v1/pocket/?format=json&ordering='?'&limit="+N_RANDOM;	
	public static final String API_UPLOAD_FILE = "http://"+ API_URL +"/api/upload/";
	public static final String API_NEWOBJECT = "http://"+ API_URL +"/api/v1/newobject/";
	public static final String API_UPDATEOBJECT = "http://"+ API_URL +"/api/v1/updateobject/";
	public static final String API_CHECKUSER = "http://"+ API_URL +"/api/v1/user/";
	public static final String API_GET_SEARCH_POCKET = "http://"+ API_URL +"/api/v1/pocket/?name=";
	public static final String API_GET_SEARCH_USER = "http://"+ API_URL +"/api/v1/user/?nick=";

	public static final String API_GET_POCKET_SET = "http://"+ API_URL +"/api/v1/pocket/set/";
	
	public static final String API_FORMAT_JSON = "/?format=json";
	public static final String DATAJSON = "datajson";
	
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String DELETE = "DELETE";
	public static final String PUT = "PUT";
	public static final String POSTFILE = "POSTFILE";
	public static final String ID = "id";
	public static final String ID_USER = "id_user";
	public static final String ID_POCKET = "id_pocket";
	public static final String PASSWORD = "password";
	public static final String POS_VOTES = "pos_votes";
	public static final String NEG_VOTES = "neg_votes";
	public static final String DATEFORMAT = "yyyy-MM-dd'T'hh:mm:ss";
	public static final String RANDOM = "RAMDOM";
	public static final String REG_DATE = "reg_date";
	
	

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String MESSENGER = "MESSENGER";
	public static final String FILE = "FILE";
	public static final String RESULT = "result";
	public static final String OK = "OK";
	
	public static final String NAME_OBJECT = "name";
	public static final String TYPE_OBJECT = "type";
	public static final String TYPE_DESCRIPTION_OBJECT = "description";
	public static final String POCKET_ID_OBJECT = "id_pocket";
	
	public static final String ID_OBJECT = "id";
	public static final String N_MOD_OBJECT = "id";
	public static final String DESCRIPTION_OBJECT = "description";
	public static final String URL_OBJECT = "url";
	public static final String ROUTE_OBJECT = "route";
	public static final String CREATION_DATE_OBJECT = "create_date";
	public static final String LAST_MOD_OBJECT = "last_mod";
	
	public static final int MSG_OK = 1;
	public static final int MSG_KO = 0;
	
	public static final String IMAGE = "IMAGE";
	public static final String MUSIC = "MUSIC";
	public static final String TEXT = "TXT";
	public static final String URL = "URL";
	
	public static final String TYPE_IMAGE = "image/jpeg";
	public static final String TYPE_MUSIC = "audio/mpeg";
	public static final String TYPE_TEXT = "text/html";
	public static final String TYPE_URL = "text/html";
	
	public static final String PREF_KEY_VIBRATE = "key_preference_vibrate";
	public static final String PREF_KEY_SOUND = "key_preference_sound";
	public static final String PREF_KEY_STORE = "key_preference_store";
	public static final String PREF_KEY_AUTO = "key_preference_auto";
	public static final String PREF_KEY_FILE = "key_preference_file_route";
	public static final String PREF_KEY_EXT_NAV = "key_preference_ext_navigator"; 
	public static final String PREFERENCES_SOUND_STATE = "sound_state";
	public static final String PREFERENCES_VIBRATE_STATE = "vibrate_state";
	public static final String DESTINATION = "destination";
	public static final String DEFAULT_LOCATION = "/mnt/sdcard/";
	public static final String SDCARD = "/mnt/sdcard/";
	public static final String HTTP = "http://";
	public static final String SINGLE_TO_VIEW_OBJECT_PARENT = "parent";
	public static final String SINGLE_TO_VIEW_OBJECT_N_PARENT = "n_pocket";
	
	public static final int SINGLE_YOUR = 0;
	public static final int SINGLE_RANDOM = 1;
	public static final int SINGLE_FEATURED = 2;
	public static final int SINGLE_SPONSORED = 3;
	public static final int SINGLE_SEARCH = 4;
	public static final String SINGLE_TO_NEW_ITEM_N_POCKET = "n_pocket";
	public static final String ACTIVITY_TO_SINGLE = "acttosingle";
	public static final String OBJECT_TO_DESCRIPTION = "object";
	public static final String OBJECT_ID_OBJECT = "id_object";
	public static final String OBJECT_ID_POCKET = "id_pocket";
	public static final String OBJECT_NAME = "name";
	public static final String OBJECT_DESCRIPTION = "description";
	public static final String OBJECT_TO_EDIT = "edit";
	public static final String REGISTER = "register";
	public static final String CHECK_USER = "check_user";
	public static final String MAIN_SEARCH_TO_SINGLE_SEARCH = "searchToSechar";
	public static final String SEARCH = "SEARCH";
	public static final String SEARCH_TYPE_POCKET = "SEARCH_POCKET";
	public static final String SEARCH_TYPE_USER = "SEARCH_USER";
	public static final String SEARCH_TYPE = "SEARCH_TYPE";
	public static final String SEARCH_VALUE = "SEARCH_VALUE";
	public static final String HEADER_USER_ID = "USER_ID";
	public static final String HEADER_API_KEY = "API_KEY";



















	
	
	
	
	
}
