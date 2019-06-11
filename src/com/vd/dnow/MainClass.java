package com.vd.dnow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vd.dnow.dto.PlaceHolderDto;

public class MainClass {

	public static void main(String[] args) throws Exception {
		String htmlFilePath = "C://Users/Sakhi/Desktop/dNowTask/cinch-letter-with-statement.html";
		String htmlString = htmlToString(htmlFilePath);
//		htmlString = replacePlaceholderinHtmlString(htmlString);
//		replaceHtmlFile(htmlFilePath, htmlString);
//		htmlFileToPdfConverter(htmlFilePath);
		StringToPdf(htmlString);
	}

	private static void replaceHtmlFile(String htmlFilePath, String htmlString) throws Exception {
		File old_file = new File(htmlFilePath);
		old_file.delete();
		File new_file = new File(htmlFilePath);
		try (FileWriter fw = new FileWriter(new_file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(htmlString);
		}
	}

	private static String replacePlaceholderInHtmlString(String htmlString) throws Exception {
		String regex = "(\\$\\{.*?\\})";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(htmlString);
		while (matcher.find()) {
			String placeHolder = matcher.group().trim();
			System.out.println(placeHolder);
			String placeHolderKey = placeHolder.substring(2, placeHolder.length() - 1);
			System.out.println(placeHolderKey);

			Class<?> cls = Class.forName("com.vd.dnow.dto.PlaceHolderDto");
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				String classField = field.getName();
				if (placeHolderKey.equals(classField)) {
					Field fieldsss = cls.getDeclaredField(classField);
					PlaceHolderDto dto = new PlaceHolderDto();
					Object value = fieldsss.get(dto);
					System.out.println(value.toString());
					htmlString = htmlString.replace(placeHolder, value.toString());
				}
			}
		}
		System.out.println(htmlString);
		return htmlString;
	}

	private static String htmlToString(String htmlFilePath) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(htmlFilePath));
			String data = "";
			while ((data = reader.readLine()) != null)
				stringBuilder.append(data);
			reader.close();
		} catch (Exception e) {
		}
		String htmlData = stringBuilder.toString();
		System.out.println(htmlData);
		return htmlData;
	}

	private static void StringToPdf(String htmlString) throws Exception {
		try (OutputStream os = new FileOutputStream("C:\\Users\\Sakhi\\Desktop\\dNowTask\\HtmlToPDF1111.pdf")) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(htmlString, null);
			builder.toStream(os);
			builder.run();
		}
		System.out.println("PDF Created!");
	}

}
