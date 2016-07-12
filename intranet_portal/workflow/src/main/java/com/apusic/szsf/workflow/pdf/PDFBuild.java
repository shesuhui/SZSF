package com.apusic.szsf.workflow.pdf;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * PDF生成
 */
public class PDFBuild {

	public static void buidPDF(String pdfFile, String imageFile, String waterMarkName, int permission) {
		try {
			File file = File.createTempFile("tempFile", ".pdf"); // 创建临时文件

			// 生成PDF
			if (createPDFFile(file)) {
				waterMark(file.getPath(), imageFile, pdfFile, waterMarkName, permission); // 添加水印
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addWaterMark(String inFile, String outFile, String imageFile, String waterMarkName,
			int permission) {
		if (inFile.toLowerCase().endsWith("pdf")) {

			waterMark(inFile, imageFile, outFile, waterMarkName, permission);

		}
	}

	/**
	 * 创建PDF文件
	 * 
	 * @param file
	 *            临时文件
	 * @return 成功/失败
	 */
	public static boolean createPDFFile(File file) {
		Rectangle rect = new Rectangle(PageSize.A4);
		Document doc = new Document(rect, 50.0F, 50.0F, 50.0F, 50.0F);
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(file));
			doc.open();

			BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体

			Font font = new Font(bf, 14.0F, 0);
			font.setStyle(37); // 设置样式
			font.setFamily("宋体"); // 设置字体

			Paragraph p = new Paragraph("付 款 通 知 书\r\n", font);
			p.setAlignment(1);
			doc.add(p);
			doc.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void generateWaterMarkText(PdfStamper pdfStamper, String waterMarkName,int hNumber,int vNumber) {
		PdfContentByte content = null;
		BaseFont base = null;
		Rectangle pageRect = null;
		PdfGState gs = new PdfGState();
		try {
			// 设置字体
			base =  BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (base == null || pdfStamper == null) {
				return;
			}
			// 设置透明度为0.4
			gs.setFillOpacity(0.4f);
			gs.setStrokeOpacity(0.4f);
			int toPage = pdfStamper.getReader().getNumberOfPages();
			for (int i = 1; i <= toPage; i++) {
				pageRect = pdfStamper.getReader().getPageSizeWithRotation(i);
				// 计算水印X,Y坐标
				float x = pageRect.getWidth() / hNumber;
				float y = pageRect.getHeight() / vNumber;
				// 获得PDF最顶层
				content = pdfStamper.getOverContent(i);
				content.saveState();
				// set Transparency
				content.setGState(gs);
				content.beginText();
				content.setColorFill(BaseColor.GRAY);
				content.setFontAndSize(base, 30);
				// 水印文字成45度角倾斜
				for (int j = 1; j <= hNumber; j++) {
					for (int j2 = 1; j2 <=vNumber; j2++) {
						content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, j*x, j2*y, 45);
					}
				}
				content.endText();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			content = null;
			base = null;
			pageRect = null;
		}

	}

	public static void waterMark(String inputFile, String imageFile, String outputFile, String waterMarkName,
			int permission) {
		try {
			PdfReader reader = new PdfReader(inputFile);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

			BaseFont base = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);// 使用系统字体

			int total = reader.getNumberOfPages() + 1;
			Image image = Image.getInstance(imageFile);

			// 图片位置
			image.setAbsolutePosition(400, 480);
		/*	PdfContentByte under;
			int j = waterMarkName.length();
			char c = 0;
			int rise = 0;
			for (int i = 1; i < total; i++) {
				rise = 400;
				under = stamper.getUnderContent(i);
				under.beginText();
				under.setFontAndSize(base, 30);

				if (j >= 15) {
					under.setTextMatrix(200, 120);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = waterMarkName.charAt(k);
						under.showText(c + "");
					}
				} else {
					under.setTextMatrix(240, 100);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = waterMarkName.charAt(k);
						under.showText(c + "");
						rise -= 18;

					}
				}

				// 添加水印文字
				under.endText();

				// 添加水印图片
				under.addImage(image);

				// 画个圈
				under.ellipse(250, 450, 350, 550);
				under.setLineWidth(1f);
				under.stroke();
			}*/
			generateWaterMarkText(stamper, waterMarkName,4,4);
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String imageFilePath = "D:/temp/watermark.png"; // 水印图片路径
		String outFilePath = "D:/temp/itext.pdf"; // 文件生成路径
		String inputFilePath = "d:/temp/inputpdf.pdf";
		// buidPDF(pdfFilePath, imageFilePath, "正版授权", 16);
		addWaterMark(inputFilePath, outFilePath, imageFilePath, "金蝶中间件", 16);
	}
}