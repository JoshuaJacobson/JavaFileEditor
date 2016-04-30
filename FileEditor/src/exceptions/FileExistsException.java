package exceptions;

public class FileExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FileExistsException() { super(); }
	public FileExistsException(String message) { super(message); }
	public FileExistsException(String message, Throwable cause) { super(message, cause); }
	public FileExistsException(Throwable cause) { super(cause); }
}
