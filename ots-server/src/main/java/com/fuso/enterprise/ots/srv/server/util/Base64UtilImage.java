package com.fuso.enterprise.ots.srv.server.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.fuso.enterprise.ots.srv.api.model.domain.Base64ByteImage;
import com.fuso.enterprise.ots.srv.api.model.domain.ZipBase64Images;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Base64UtilImage {
	
	public static  String convertBase64toImage(String base64, int name) throws IOException {  
        // TODO Auto-generated method stub  
		System.out.println("5");
		String path = "/home/etaarana_support/image/e-tarana/"+name+".png" ;
        byte[] base64Val=convertToImg(base64);  

        writeByteToImageFile(base64Val, path); 
        System.out.println("6");
        System.out.println("Saved the base64 as image in current directory with name image.png"); 
        path = "http://etaaranaservices.ortusolis.in:8081/ots/rotary/"+name+".png";
        return path;
   }  

	 public static void writeByteToImageFile(byte[] imgBytes, String imgFileName) throws IOException  
     {  
		  System.out.println("5-next");
		  System.out.println("image filename"+imgBytes);
		  System.out.println("image filename"+imgFileName);
          File imgFile = new File(imgFileName);  
          BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));  
          ImageIO.write(img, "png", imgFile);  
     }  

	 public static byte[] convertToImg(String base64) throws IOException  
     {  
          return Base64.decodeBase64(base64);  
     }  
	 
}