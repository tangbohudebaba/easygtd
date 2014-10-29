package com.nationsky.backstage.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.nationsky.backstage.util.image.JavaImageUtil;


/**
 * 功能：图像处理的功能类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class ImageUtil {
	
	public static final String IMAGE_FILE_REGEX_PATTERN = "(.*)\\.(jpg|jpeg|png|gif|BMP)$";
	public static final String IMAGE_PNG_FILE_REGEX_PATTERN = "(.*)\\.png$";
	/**
	 * JPG图片格式保存
	 * @param src 上传文件必须是JPG图片
	 * @param distFilePrefix /web/pic/goods/1/1
	 */
	public static void transformJPGImage(File src,String distFilePrefix,List<Integer> sizeList){
		if(!src.canRead())return;
		String srcFilePath = StringUtil.concat(distFilePrefix,"_src.jpg");
		//生成目标目录
		FileUtil.mkdirs(FileUtil.getFileDirectory(srcFilePath));
		//保存原图片
		FileUtil.copyFile(src, new File(srcFilePath));
		for(Integer size:sizeList){
			ImageUtil.reduceImg(
					srcFilePath,
					StringUtil.concat(distFilePrefix,"_",size,".jpg"),
					size		
			);
		}
	}
	/**
	 * 最大区域为方形缩略图
	 * @param imgsrc
	 * @param imgdist
	 * @param size
	 */
	public static void reduceImg(String imgsrc, String imgdist,int size){
		JavaImageUtil.transformImage(imgsrc, imgdist, size);			
	}
	
	/**
	 * 生成指定大小的缩略图
	 * @param imgsrc
	 * @param imgdist
	 * @param widthdist
	 * @param heightdist
	 */
	public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist){
		JavaImageUtil.reduceImg(imgsrc, imgdist, widthdist, heightdist);			
	}
	
	/**
	 * 获得图片文件的宽高
	 * @param imageFile
	 * @return int[] 0:宽 1:高
	 * @throws IOException
	 */
	public static int[] getImageWidthHeight(File imageFile){
		int[] result = new int[2];
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(imageFile);
			BufferedImage bufferedImg = ImageIO.read(fis);
			result[0] = bufferedImg.getWidth();
			result[1] = bufferedImg.getHeight();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fis!=null)
				try{fis.close();}catch(Exception e){}
		}
		return result;
	}
	/**
	 * 切图
	 * @param imgsrc
	 * @param imgdist
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @throws MagickException
	 */
	public static void cutImg(File imgsrc, String imgdist, int width, int height, int x, int y){
		//JavaImageUtil.cutImg(imgsrc, imgdist, width, height, x, y);
		try {
			Rectangle rec = new Rectangle(x,y,width,height);
			BufferedImage src = ImageIO.read(imgsrc);
			JavaImageUtil.cut(src, rec, new File(imgdist));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获得最大的宽
	 * @param width
	 * @param height
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static int getMaxWidth(int width, int height, int maxWidth, int maxHeight){
		int newWidth = width;	
		int newHeight = height;
		if(width > maxWidth){		
			if(width > maxWidth){
				float temp =  height * maxWidth / width;
				newHeight = Math.round(temp);
				newWidth = maxWidth;
			}
			if(newHeight > maxHeight){
				float temp = width * maxHeight / height;
				newWidth = Math.round(temp);
				newHeight = maxHeight;
			}
		}
		return newWidth;
	}
	
	/**
	 * 获得最大的高度
	 * @param width
	 * @param height
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static int getMaxHeight(int width, int height, int maxWidth, int maxHeight){
		int newHeight = height;
		if(width > maxWidth){		
			if(width > maxWidth){
				float temp =  height * maxWidth / width;
				newHeight = Math.round(temp);
			}
			if(newHeight > maxHeight){
				newHeight = maxHeight;
			}
		}
		return newHeight;
	}
	
	/**
	 * 判断图片类型
	 * jpg jpeg : JPEG
	 * bmp:bmp
	 * png:png
	 * gif:gif
	 * @param imageFile
	 * @return
	 */
	public static String getImageFormat(File imageFile){
		return JavaImageUtil.getImageFormat(imageFile);
	}
	
	/**
	 * 给图片添加水印
	 * @param imgsrc
	 * @param imgdist
	 * @param watermarksrc
	 */
	public static boolean applyWatermark(File imgsrc, String imgdist, String watermarksrc){
		try {
			return JavaImageUtil.applyImageWaterMark(imgsrc,imgdist,watermarksrc,-1,-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 给图片添加文字水印
	 * @param imgsrc
	 * @param imgdist
	 * @param watermarksrc
	 */
	public static boolean applyTextWatermark(File imgsrc, String imgdist, String text){
		try {
			return JavaImageUtil.applyTextWaterMark(imgsrc,imgdist,text,null,-1,-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
