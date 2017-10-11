package com.zhuo.test;
import java.awt.Image; 
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File; 
import javax.imageio.ImageIO;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage; 
import org.bytedeco.javacv.FFmpegFrameGrabber; 
import org.bytedeco.javacv.Frame;
public class Test {
	/**
	 * 获取指定视频的帧并保存为图片至指定目录
	 * @param videofile  源视频文件路径
	 * @param framefile  截取帧的图片存放路径
	 * @throws Exception
	 */
	public static void fetchFrame(String videofile, String framefile)
	        throws Exception {
	    long start = System.currentTimeMillis();
	    File targetFile = new File(framefile);
	    FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile); 
//	    ff.start();
	    ff.start();
	    int lenght = ff.getLengthInFrames();
	    int i = 0;
	    Frame f = null;
	    while (i < lenght) {
	        // 过滤前5帧，避免出现全黑的图片，依自己情况而定
	        f = ff.grabFrame();
	        if ((i > 5) && (f.image != null)) {
	            break;
	        }
	        i++;
	    }
	    IplImage img = f.image;
	    //OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	   // IplImage img = converter.convertToIplImage(f);
	    int owidth = img.width();
	    int oheight = img.height();
	    // 对截取的帧进行等比例缩放
	    int width = 800;
	    int height = (int) (((double) width / owidth) * oheight);
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    bi.getGraphics().drawImage(iplToBufImgData(img).getScaledInstance(width, height, Image.SCALE_SMOOTH),
	            0, 0, null);
/*	    bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
	            0, 0, null);*/
	    ImageIO.write(bi, "jpg", targetFile);
	    ff.stop();
	    System.out.println(System.currentTimeMillis() - start);
	}

	public static BufferedImage iplToBufImgData(IplImage mat) {  
	    if (mat.height() > 0 && mat.width() > 0) {  
	        BufferedImage image = new BufferedImage(mat.width(), mat.height(),  
	            BufferedImage.TYPE_3BYTE_BGR);  
	        WritableRaster raster = image.getRaster();  
	        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();  
	        byte[] data = dataBuffer.getData();  
	        BytePointer bytePointer =new BytePointer(data);  
	       mat.imageData(bytePointer);  
	        return image;  
	    }  
	    return null;  
	 }  

	public static void main(String[] args) {
	    try {
	        Test.fetchFrame("https://uzu-2017-test.oss-cn-hangzhou.aliyuncs.com/miuzu-huanxin-video/video/8f83c449-4a55-4716-8a2a-2b708ac4d377.mp4", "D:/4.jpg");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
