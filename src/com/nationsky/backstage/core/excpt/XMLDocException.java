/*
 * Created on 2006-3-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nationsky.backstage.core.excpt;

/**
 * 功能：用于处理XML错误
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class XMLDocException extends Exception {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Exception exception;

	  /**
	   * Creates a new XMLDocumentException wrapping another exception, and with a detail message.
	   * @param message the detail message.
	   * @param exception the wrapped exception.
	   */
	  public XMLDocException(String message, Exception exception) {
	    super(message);
	    this.exception = exception;
	  }

	  /**
	   * Creates a XMLDocumentException with the specified detail message.
	   * @param message the detail message.
	   */
	  public XMLDocException(String message) {
	    this(message, null);
	  }

	  /**
	   * Creates a new XMLDocumentException wrapping another exception, and with no detail message.
	   * @param exception the wrapped exception.
	   */
	  public XMLDocException(Exception exception) {
	    this(null, exception);
	  }

	  /**
	   * Gets the wrapped exception.
	   *
	   * @return the wrapped exception.
	   */
	  public Exception getException() {
	    return exception;
	  }

	  /**
	   * Retrieves (recursively) the root cause exception.
	   *
	   * @return the root cause exception.
	   */
	  public Exception getRootCause() {
	    if (exception instanceof XMLDocException) {
	      return ((XMLDocException) exception).getRootCause();
	    }
	    return exception == null ? this : exception;
	  }

	  public String toString() {
	    if (exception instanceof XMLDocException) {
	      return ((XMLDocException) exception).toString();
	    }
	    return exception == null ? super.toString() : exception.toString();
	  }
}
