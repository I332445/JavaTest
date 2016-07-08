package URLTest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownPicToLocalByUrl {
	//id in a share url
	private static String id;
	
	//content return
	//private static String content;
	private static JSONObject jo = new JSONObject();
	
	//list for pic physical url
	private static ArrayList<String> picUrlList = new ArrayList();
	
	//the prefix and suffix of share url of youdaoNote
	private final static String urlPrefix = "http://note.youdao.com/yws/public/note/";
	private final static String urlSuffix = "?keyfrom=public";
	
	//set scanner.in id
	public static void setId(String urlid){
		id  = urlid;
	}
	
	//return the content of real url js httprequest
	public static JSONObject doGet(String id){
		//js request url , to return content
		String urlStr = urlPrefix + id + urlSuffix;
		try {
			URL localURL = new URL(urlStr);
			try {
				URLConnection connection = (HttpURLConnection)localURL.openConnection();
				InputStream inputStream = null;
		        InputStreamReader inputStreamReader = null;
		        BufferedReader reader = null;
		        String tempLine = null;
		        inputStream = connection.getInputStream();
	            inputStreamReader = new InputStreamReader(inputStream);
	            reader = new BufferedReader(inputStreamReader);
	            while ((tempLine = reader.readLine()) != null) {
	            	//the reason to use json save data
	            	//the img url return here like src=\"
	            	//if I just return a String, whne convert to a document, we can't clear the '\'
	            	//the String we then get will be hard to operate
	            	jo = JSONObject.fromObject(tempLine);
	            }
	            
	            if (reader != null) {
	                reader.close();
	            }
	            
	            if (inputStreamReader != null) {
	                inputStreamReader.close();
	            }
	            
	            if (inputStream != null) {
	                inputStream.close();
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
	//get pic's url into list
	public static void getImgUrls(){
		//change str to html document
		String content = jo.getString("content");
    	Document doc = Jsoup.parse(content);
    	Elements es = doc.getElementsByTag("img");
    	String regex = "(?i)(src)[=\"\'\\s]+([^\"\']*)(?=[\"\'\\s>]+)";
    	Pattern pattern = Pattern.compile(regex);
    	for(Element e: es){
    		String img = e.toString();
    		Matcher matcher = pattern.matcher(img);
        	while(matcher.find()){        		
        		String srcUrl = matcher.group().substring(5);
        		picUrlList.add(srcUrl);
        	}
    	}
	}
	//down pic to local from pic's url
	public static void downImg(ArrayList<String> list){
		URL url = null;
    	int imageNumber = 0;
    	DataInputStream dataInputStream = null;
    	String imageName;
    	FileOutputStream fileOutputStream = null;
    	for(String urlString : list){
			try {  
                url = new URL(urlString);  
                dataInputStream = new DataInputStream(url.openStream());  
                imageName = "C:\\imagesFrom\\" + imageNumber + ".jpg";  
                fileOutputStream = new FileOutputStream(new File(imageName));  
  
                byte[] buffer = new byte[1024];  
                int length;  
  
                while ((length = dataInputStream.read(buffer)) > 0) {  
                    fileOutputStream.write(buffer, 0, length);  
                }  
                System.out.println("图片"+imageNumber+"已下载");
                
                imageNumber++;  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
            try {
            	if(dataInputStream != null){
            		dataInputStream.close();
            	}
				if(fileOutputStream != null){
					fileOutputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("input ID:");
		Scanner urlId = new Scanner(System.in);
		setId(urlId.nextLine());
		doGet(id);
		getImgUrls();
		downImg(picUrlList);
		System.out.println("图片下载结束，请到C:\\imagesFrom查看");
	}
}
