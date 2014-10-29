package com.nationsky.backstage.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.nationsky.backstage.util.ExcptUtil;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.ImageUtil;
import com.nationsky.backstage.util.StringUtil;
import com.nationsky.backstage.util.ValidateUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 功能：处理图片的java实现,本包中可能有其他的方法实现处理图片
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
//@SuppressWarnings("restriction")
@SuppressWarnings("restriction")
public class JavaImageUtil{
  
  	/**
	 * 最大区域为方形缩略图
	 * @param imgsrc 原图片路径
	 * @param imgdist 图片保存路径
	 * @param size 方形最大值
	 */
	public static void transformImage(String imgsrc, String imgdist,int size) {
		if(size==0){
			return;
		}
	   try {
		   BufferedImage src = ImageIO.read(new File(imgsrc)); // 读入文件
		   reduceImg(imgsrc,imgdist,ImageUtil.getMaxWidth(Math.round(src.getWidth()), Math.round(src.getHeight()), size, size),ImageUtil.getMaxHeight(Math.round(src.getWidth()), Math.round(src.getHeight()), size, size));
	   }catch(Exception e){
		   e.printStackTrace();
		  ExcptUtil.unchecked(e);
	   }
	}
	/**
	 * 生成指定大小的缩略图
	 * @param imgsrc 原图路径
	 * @param imgdist 生成图路径
	 * @param widthdist 生成图宽度
	 * @param heightdist 生成图高度
	 */
	
	public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist) {
		FileOutputStream out = null;
	    try {
	        File srcfile = new File(imgsrc);
	        if (!srcfile.exists()) {
	            return;
	        }
	        Image src = javax.imageio.ImageIO.read(srcfile);

	        BufferedImage tag = new BufferedImage(widthdist, heightdist,BufferedImage.TYPE_INT_RGB);

	        tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,Image.SCALE_SMOOTH), 0, 0,  null);

	        out = new FileOutputStream(imgdist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	        encoder.encode(tag);

	    } catch (IOException ex) {
	    	ExcptUtil.unchecked(ex);
	    }finally{
	    	FileUtil.close(out);
	    }
	}
	
	/**
	 * 裁剪图片
	 * 
	 * @param image
	 *            原图
	 * @param subImageBounds
	 *            裁剪矩形框
	 * @param subImageFile
	 *            保存路径
	 * @throws IOException
	 */
	public static void cut(BufferedImage image,
			Rectangle subImageBounds, File subImageFile) throws IOException {
		String formatName = getImageFormat(subImageFile);
		BufferedImage subImage = new BufferedImage(subImageBounds.width,
				subImageBounds.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = subImage.getGraphics();
		if (subImageBounds.width > image.getWidth()
				|| subImageBounds.height > image.getHeight()) {
			int left = subImageBounds.x;
			int top = subImageBounds.y;
			if (image.getWidth() < subImageBounds.width)
				left = (int) ((subImageBounds.width - image.getWidth()) / 2);
			if (image.getHeight() < subImageBounds.height)
				top = (int) ((subImageBounds.height - image.getHeight()) / 2);
			g.setColor(Color.white);
			g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
			g.drawImage(image, left, top, null);
			ImageIO.write(image, formatName, subImageFile);
		} else {
			g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
					subImageBounds.width, subImageBounds.height), 0, 0, null);
		}
		g.dispose();
		ImageIO.write(subImage, formatName, subImageFile);
	}
	
	
	/**
	 * 判断图片类型
	 * @param imageFile
	 * @return
	 */
	public static String getImageFormat(File imageFile){
		if(imageFile==null || !imageFile.canRead())
			return "";
		ImageInputStream iis = null;
		try{
			iis = ImageIO.createImageInputStream(imageFile);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			ImageReader reader = iter.next();
			return reader.getFormatName();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(iis!=null)
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return StringUtil.EMPTY_STRING;
	}
	
	/**
	 * 给图片加文本水印
	 * @param srcImage 要加水印的图片
	 * @param distImage 加完水印的图片
	 * @param waterText 水印文字
	 * @param font 字体可以为null
	 * @param x 坐标X 小于零时为右下角
	 * @param y 坐标Y 小于零时为右下角
	 * @return
	 * @throws Exception
	 */
	public static boolean applyTextWaterMark(File srcImage,String distImage,String waterText,Font font,int x,int y) throws Exception{
		String imageType = getImageFormat(srcImage);
		if(ValidateUtil.isNotNull(imageType)){
			BufferedImage originalImage = ImageIO.read(srcImage);
			int originalWidth = originalImage.getWidth(null);
			int originalHeight = originalImage.getHeight(null);
			if (originalWidth < 50 || originalHeight < 50){ 
				return false;
			}
			if(waterText==null || waterText.trim().equals("")){
				return false;
			}
			font = font==null?new Font("宋体", Font.PLAIN, 13):font;
			BufferedImage newImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, null);
			g.setColor(Color.RED);
			g.setFont(font);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
			int len = waterText.length();
			if(x>-1 && y>-1){
				g.drawString(waterText, x, y);
			}else{
				//右下角
				g.drawString(waterText, originalWidth-len*font.getSize2D()/2-6,originalHeight-6);
			}
			g.dispose();
			FileOutputStream fos = new FileOutputStream(distImage);
			ImageIO.write(newImage, imageType, fos);
			fos.close();
			return true;
		}
		return false;
	}
	
	/**
	 * 图片加水印
	 * @param srcImage 要加水印的图片
	 * @param distImage 加完水印后的图片
	 * @param waterMarkFile 水印图片
	 * @param x 加水印位置横坐标 小于零时为右下角
	 * @param y 加水印纵坐标  小于零时为右下角
	 * @return
	 * @throws Exception
	 */
	public static boolean applyImageWaterMark(File srcImage, String distImage,String waterMarkFile,int x,int y) throws Exception{
		String imageType = getImageFormat(srcImage);
		if(ValidateUtil.isNotNull(imageType)){
			File waterMarkImage = new File(waterMarkFile);
			if (!waterMarkImage.exists()) {
				return false;
			}
			BufferedImage originalImage = ImageIO.read(srcImage);
			BufferedImage watermarkImage = ImageIO.read(waterMarkImage);
			int originalWidth = originalImage.getWidth(null);
			int originalHeight = originalImage.getHeight(null);
			int watermarkWidth = watermarkImage.getWidth(null);
			int watermarkHeight = watermarkImage.getHeight(null);
			if (originalWidth <= watermarkWidth || originalHeight <= watermarkHeight || originalWidth < 50 || originalHeight < 50) {
				return false;
			}
			BufferedImage newImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
			if(x>-1 && y>-1){
				g.drawImage(watermarkImage, x, y, watermarkWidth, watermarkHeight, null);
			}else{
				//右下角
				g.drawImage(watermarkImage, originalWidth-watermarkWidth-10, originalHeight-watermarkHeight-10, watermarkWidth, watermarkHeight, null);
			}
			g.dispose();
			FileOutputStream fos = new FileOutputStream(distImage);
			ImageIO.write(newImage, imageType, fos);
			fos.close();
			
			return true;
		}
		return false;
	}
	
	/**
	 * 图片格式转换
	 * @param source 原图
	 * @param result 新图
	 */
	public static void convert(String source, String result) {
		try{
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, JavaImageUtil.getImageFormat(f), new File(result));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}