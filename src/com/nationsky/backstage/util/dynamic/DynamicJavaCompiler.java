/**
 * 
 */
package com.nationsky.backstage.util.dynamic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.nationsky.backstage.Configuration;

/**
 * 功能：动态代码编译器
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class DynamicJavaCompiler {
	
	
	/**
	 * 编译
	 * @param source
	 * @param diagnosticListener
	 * @param locale
	 * @param charset
	 * @return
	 */
	public static boolean compiler(
			String javaName,
			String source, 
			DiagnosticListener<? super JavaFileObject> diagnosticListener,
			Locale locale,
			Charset charset){
		StandardJavaFileManager fileManager = null;
		boolean ok = false;
		try{
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			fileManager = compiler.getStandardFileManager(diagnosticListener, locale, charset);
			StringSourceJavaObject sourceObject = new StringSourceJavaObject(javaName,source);
			Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(sourceObject);
			// 获取编译类根路径，不然会报找不到类的错误
			String path = DynamicJavaCompiler.class.getResource("/").getPath();
			//Configuration._CURRENT_PATH
			Iterable< String> options = Arrays.asList("-d", path ,"-classpath",Configuration.CLASSPATH);
			
			CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
			ok = task.call();
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			try {
				if(fileManager!=null)
					fileManager.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	/**
	 * 生成对象
	 * @return
	 */
	public static Object getDynamicInstance(File clazzFile){
		Object obj  = null;
		try{
			DynamicClassLoader loader = new DynamicClassLoader(DynamicClassLoader.class.getClassLoader());
			Class<?> clazz = loader.loadClass(clazzFile, new File(Configuration.CURRENT_PATH));
			obj = clazz.newInstance();
		}catch(Throwable t){
			t.printStackTrace();
		}
		return obj;
	}
	
}

class StringSourceJavaObject extends SimpleJavaFileObject {

	private String content = null;

	public StringSourceJavaObject(String name, String content) throws URISyntaxException {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
						Kind.SOURCE);
		this.content = content;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return content;
	}
}
