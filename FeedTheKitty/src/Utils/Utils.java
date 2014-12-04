package Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class Utils {

	public static void loadImage(ImageView imageView, String url){
		Ion.with(imageView)
		.load(url);
	}

	public static boolean isEnd(String date, String time){
		Date expiredDate = stringToDate(date, "MM-dd-yyyy");
		Date now = new Date();
		if(now.after(expiredDate)){
			return true;
		}

		return false;
	}

	private static Date stringToDate(String aDate,String aFormat) {

		if(aDate==null) return null;
		ParsePosition pos = new ParsePosition(0);
		SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
		Date stringDate = simpledateformat.parse(aDate, pos);
		return stringDate;            

	}
}
