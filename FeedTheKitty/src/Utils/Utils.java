package Utils;

import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class Utils {

	public static void loadImage(ImageView imageView, String url){
		Ion.with(imageView)
		.load(url);
	}
}
