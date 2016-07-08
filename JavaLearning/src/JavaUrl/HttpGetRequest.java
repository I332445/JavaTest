package JavaUrl;

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
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HttpGetRequest {
	private ArrayList<String> urlArray;

    /**
     * Main
     * @param args
     * @throws Exception 
     */
	//changed
    public static void main(String[] args) throws Exception {
    	Scanner urlId = new Scanner(System.in);
    	System.out.println("请输入ID：");
    	String id = urlId.nextLine();
    	//get json data
    	JSONArray ja = doGet(id);
    	String content = null;
    	//System.out.println(ja);
    	for (Iterator<JSONObject> iter = ja.listIterator(); iter.hasNext();){
    		 content = iter.next().getString("content");    		
    	}
    	//change str to html document
    	Document doc = Jsoup.parse(content);
//    	System.out.println(doc);
//    	System.out.println(content);
    	Elements es = doc.getElementsByTag("img");
    	String regex = "(?i)(src)[=\"\'\\s]+([^\"\']*)(?=[\"\'\\s>]+)";
    	Pattern pattern = Pattern.compile(regex);
    	
    	//array for pic url
    	ArrayList<String> picUrl = new ArrayList();
    	for(Element e:es){
    		String img = e.toString();
    		Matcher matcher = pattern.matcher(img);
        	while(matcher.find()){
        		String srcUrl = matcher.group().substring(5);
        		picUrl.add(srcUrl);
        	}
    	}
    	URL url = null;
    	int imageNumber = 0;
    	DataInputStream dataInputStream = null;
    	String imageName;
    	FileOutputStream fileOutputStream = null;
    	for(String urlString : picUrl){
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
            dataInputStream.close();  
            fileOutputStream.close();
	}
    	System.out.println("所有图片下载结束，共"+imageNumber+"张");
    }
    
    
    /**
     * Get Request
     * @return
     * @throws Exception
     */
    public static JSONArray doGet(String id) throws Exception {
        //URL localURL = new URL("http://www.baidu.com/");
    	//URL localURL = new URL("http://note.youdao.com/share/?id=541d355d59b9b1419459841176471e3a&type=note#/");
    	/*URL localURL = 
    			new URL("http://note.youdao.com/yws/public/note/541d355d59b9b1419459841176471e3a?keyfrom=public");*/
    	String url = "http://note.youdao.com/yws/public/note/" + id + "?keyfrom=public";
    	URL localURL = new URL(url);    			
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        //StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        JSONArray ct = new JSONArray();
        
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
                      
            
            while ((tempLine = reader.readLine()) != null) {
                //resultBuffer.append(tempLine);//
            	System.out.println(tempLine);
                ct.add(tempLine);
            }
         
            System.out.println(ct);
            //JSONArray ct = new JSONArray(resultBuffer.toString());
            
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }
        
        return ct;
        
    }
    
}