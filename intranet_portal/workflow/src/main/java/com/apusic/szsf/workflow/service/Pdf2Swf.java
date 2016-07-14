package com.apusic.szsf.workflow.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

public class Pdf2Swf {

	private String toolPath;

	public void setToolPath(String toolPath) {
		this.toolPath = toolPath;
	}

	public String getToolPath() {
		return this.toolPath;
	}

	public int doConvert(String pdfPath, String destPath, String fileName, String password) throws IOException {

		// 目标路径不存在则建立目标路径
		File dest = new File(destPath);
		if (!dest.exists())
			dest.mkdirs();

		// 源文件不存在则返回
		File source = new File(pdfPath);
		if (!source.exists())
			return 0;

		// 调用pdf2swf命令进行转换
		String command = this.getToolPath() + " -o " + destPath + "/" + fileName + " -s flashversion=9 -B \"d:/SWFTools/swfs/rfxview.swf\" " + pdfPath ;

		System.out.println(command);

		Process pro = Runtime.getRuntime().exec(command);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		String s;
		while ((s = bufferedReader.readLine()) != null) {
			System.out.println(s);
		}

		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return pro.exitValue();

	}

	public static void main(String[] args) {
		// String toolpath = "cmd /c C:/Program\" \"Files\"
		// \"(x86)/SWFTools/pdf2swf.exe";

		String toolpath = "D:/SWFTools/pdf2swf.exe  ";

		Pdf2Swf ps = new Pdf2Swf();
		if (StringUtils.isBlank(ps.getToolPath())) {
			ps.setToolPath(toolpath);
		}
		try {
			ps.doConvert("d:/temp/itext.pdf", "d:/temp", "tt.swf", "123456");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
