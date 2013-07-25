package com.pocket.patit.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pocket.patit.R;

import android.content.Context;

/**
 * {@link TutorialContent}
 * 
 * Clase que alamacena en memoria todos los elementos necesarios para cada página
 * del tutorial. Contiene los párrafos, imágenes y títulos de las clases.
 * 
 * @author Juan Manuel Jurado
 * @author <jmj23elviso@gmail.com>
 * @version 1.0
 */
public class TutorialContent {

    public static class TutorialItem {

        public String id;		//número de la lista
        public String content;	//título
        public String content2;	//primer párrafo
        public String content3;	//segundo párrafo
        public int image_type;	//imagen del título
        public int image_tuto;	//imagen del medio del párrafo
        
        /**
         * Constructor parametrizado
         * @param id
         * @param content_title
         * @param type
         * @param content2
         * @param content3
         * @param image_tuto
         */
        public TutorialItem(String id, String content_title,int type, String content2, String content3, int image_tuto) {
            this.id = id;
            this.content = content_title;
            this.content2 = content2;
            this.content3 = content3;
            this.image_type = type;
            this.image_tuto = image_tuto;
        }

        @Override
        public String toString() {
            return content;
        }
    }
    public static Context c;
    public static List<TutorialItem> ITEMS = new ArrayList<TutorialItem>();
    public static Map<String, TutorialItem> ITEM_MAP = new HashMap<String, TutorialItem>();

    static {
    	
    }

    private static void addItem(TutorialItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

	/**
	 * Método para definir los elementos de las páginas del tutorial
	 * @param c
	 */
	public static void setItems(Context c){
		addItem(new TutorialItem("1", c.getString(R.string.yourpocketshome) ,R.drawable.bluebordergrid,c.getString(R.string.yourtuto1),c.getString(R.string.yourtuto2),R.drawable.yoututo));
        addItem(new TutorialItem("2", c.getString(R.string.randompocketshome),R.drawable.redbordergrid,c.getString(R.string.randomtuto1),c.getString(R.string.randomtuto2),R.drawable.randomtuto));
        addItem(new TutorialItem("3", c.getString(R.string.featuredpocketshome),R.drawable.yellowbordergrid,c.getString(R.string.featuredtuto1),c.getString(R.string.featuredtuto2),R.drawable.featuredtuto));
        addItem(new TutorialItem("4", c.getString(R.string.sponsoredpocketshome),R.drawable.greenbordergrid,c.getString(R.string.sponsoredtuto1),c.getString(R.string.sponsoredtuto2),R.drawable.sponsoredtuto));
        addItem(new TutorialItem("5", c.getString(R.string.searchpocketshome),R.drawable.greybordergrid,c.getString(R.string.searchtuto1),c.getString(R.string.searchtuto2),R.drawable.searchtuto));
        addItem(new TutorialItem("6", c.getString(R.string.object),R.drawable.purplebordergrid,c.getString(R.string.objecttuto1),c.getString(R.string.objecttuto2),R.drawable.objecttuto));
	}
}
