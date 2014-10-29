/**
 * 
 */
package com.nationsky.backstage.util.image;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nationsky.backstage.util.ExcptUtil;
import com.nationsky.backstage.util.FileUtil;
import com.nationsky.backstage.util.StringUtil;

/**
 * 功能：CMYK图片转换成RGB文件
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class CmykToRgb {

	/**
	 * CMYK转换成RGB
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	public static File cmyk2rgb(File imageFile) throws IOException{
		String format = JavaImageUtil.getImageFormat(imageFile); 
		File rgbImageFile = new File(StringUtil.concat(
				FileUtil.getFileDirectory(imageFile.getCanonicalPath()),
				File.separator,
				"rgb_",
				FileUtil.removeExtension(imageFile.getName()),".",format)
				);
        BufferedImage image = ImageIO.read(imageFile);
        if (image != null){
        	int colorSpaceType = image.getColorModel().getColorSpace().getType();
        	if (colorSpaceType == ColorSpace.TYPE_CMYK){
        		BufferedImage rgbImage = 
                    new BufferedImage( 
                        image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR); 
                ColorConvertOp op = new ColorConvertOp(null); 
                op.filter(image, rgbImage); 
                ImageIO.write(rgbImage, format, rgbImageFile); 
        	}
        }
        return rgbImageFile;
	}
	
	/**
	 * 判断图片是否是CMYK图片
	 * @param imageFile
	 * @return
	 */
	public static boolean isCMYK(File imageFile) { 
        boolean result = false; 
        BufferedImage img = null; 
        try { 
            img = ImageIO.read(imageFile); 
        } catch (IOException e) { 
            System.out.println(ExcptUtil.getStackTraceAsString(e)); 
        } 
        if (img != null) { 
            int colorSpaceType = img.getColorModel().getColorSpace().getType(); 
            result = colorSpaceType == ColorSpace.TYPE_CMYK; 
        } 
 
        return result; 
    } 
	
}
