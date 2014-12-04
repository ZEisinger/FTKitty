package Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class Utils {

	public static void loadImage(ImageView imageView, String url){
		Ion.with(imageView)
		.load(url);
	}

	public static boolean isEnd(String date, String time){
		String dateTime = date + " " + time;
		Log.d("dateTime", "DATE TIME: " + dateTime);
		Date expiredDate = stringToDate(dateTime, "MM-dd-yyyy hh:mm aaa");
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
