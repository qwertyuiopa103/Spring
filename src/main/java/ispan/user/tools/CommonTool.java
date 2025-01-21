package ispan.user.tools;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CommonTool {
	/**
	 * 將圖片的 byte[] 轉換成 base64 格式的圖片
	 */
	public static String convertByteArrayToBase64String(byte[] data) {

		String mimeType;
		try {
			mimeType = guessMimeType(data);
		} catch (IOException e) {
			mimeType = "image/png";
			e.printStackTrace();
		}

		String base64String = Base64.getEncoder().encodeToString(data);

		return "data:%s;base64,%s".formatted(mimeType, base64String);
	}

	/**
	 * 從圖片的 byte[] 中猜測其 MimeType
	 */
	public static String guessMimeType(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);

		String mimeType = URLConnection.guessContentTypeFromStream(bais);

		bais.close();
		return mimeType;
	}

	
	/**
	 * 回傳某物件中，所有為 null 值的屬性名稱。
	 *
	 * @return String[] nullProperties
	 */
	public static String[] getNullPropertyNames(Object source) {

		final BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();

		List<String> emptyNames = new ArrayList<>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}

		return emptyNames.toArray(new String[0]);
	}
}
