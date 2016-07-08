package JavaUrl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlTest {
	private URL url;
	
	//constructor
	public UrlTest(String url){
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public URL getUrl(){
		return url;
	}
	
	public void saveFile(URLConnection conn, String fullPath, int length){
		try {
			if(conn == null){
				throw new Exception("Can't get URLConnection.");
			}
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(fullPath);
			byte[] b = new byte[length];
			int len = 0;
			while(len != -1){
				fos.write(b,0,len); 
		        len = is.read(b);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UrlTest ut = new UrlTest("http://www.baidu.com/");
		try {
			URL url = ut.getUrl();
			url.getContent();
			String path = new String("C:\\Files");
			URLConnection uConection = ut.getUrl().openConnection();
			//ut.saveFile(uConection, path, 1000000);
			
			//uConection.connect();
			//System.out.println(content);
			//BufferedReader br = new BufferedReader(new InputStreamReader(ut.getUrl().openStream()));
			/*
			while(br.readLine()!=null){
				System.out.println();
				System.out.println(br.readLine());
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
