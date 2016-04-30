/**
 * 
 */
package editors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import exceptions.FileExistsException;

/**
 * Provides better support to java.io's base file editing objects.
 * Combines them all together to create an easy to use file object.
 * 
 * @author Josh
 *
 */
public class FileEditor {

	/*
	 * Created on 4-27-16
	 * 
	 * Last modified 4-30-16
	 * 
	 * This code is open source. Please modify or redistribute at your own disgression, please Just leave credit in the code to
	 * 
	 * Joshua Jacobson (reddit.com/u/aagjj)
	 */
	
	private File file;
	private FileReader reader;
	private BufferedReader bReader;
	private PrintWriter pWriter;
	private Scanner scan;
	private FileInputStream stream;
	private BufferedWriter bWriter;
	private FileWriter writer;
	
	/**
	 * Adds a line with the given text at the specified location
	 * @param location The line to add the text after
	 * @param contents The text to add
	 * @throws IOException If an I/O Exception occurs
	 */
	public void addLine(int location, String contents) throws IOException { 
		File localFile = new File(file.getPath() + ".jbfe.temp");
		int total = this.getTotalLines();
		pWriter = new PrintWriter(new FileOutputStream(localFile));
		for (int i = 0; i <= location; i++) {
			pWriter.append(this.getLine(i) + System.getProperty("line.separator"));
		}
		pWriter.append(contents + System.getProperty("line.separator"));
		for (int i = location+1; i <= total; i++) {
			pWriter.append(this.getLine(i) + System.getProperty("line.separator"));
		}
		
		pWriter.flush();
		pWriter.close();
		file.delete();
		localFile.renameTo(file);
	}
	
	/**
	 * Adds a line with the given text at the end of the file
	 * @param contents The text to add
	 * @throws IOException If an I/O Exception occurs
	 */
	public void addLine(String contents) throws IOException {
		this.addLine(this.getTotalLines(), contents);
	}
	
	/**
	 * Adds several lines at the specified location
	 * @param location The line to add the text after.
	 * @param contents The text to add
	 * @throws IOException If 
	 */
	public void addLines(int location, String[] contents) throws IOException {
		for (int i = 0; i<contents.length; i++) {
			this.addLine(location+i, contents[i]);
		}
	}
	
	/**
	 * Adds several lines at the end of the file
	 * @param contents The text to add.
	 * @throws IOException If an I/O Exception occurs.
	 */
	public void addLines(String[] contents) throws IOException {
		for (int i = 0; i<contents.length; i++) {
			this.addLine(this.getTotalLines(), contents[i]);
		}
	}
	
	/**
	 * Creates the specified file.
	 * @throws IOException If an I/O Exception occurs
	 * @throws FileExistsException If he file allready exists
	 */
	public void createFile() throws IOException, FileExistsException {
		if(!file.exists()) {
			file.createNewFile();
		}
		else {
			throw new FileExistsException();
		}
	}
	
	/**
	 * Creates a new File Editor from the file given. 
	 * 
	 * Note that all values for lines gotten or set are in terms of "0" as the first line
	 * @param fileToUse A filename in the directory 
	 */
	public FileEditor(String fileToUse) {
		this.file = new File(fileToUse);
	}
	
	/**
	 * Creates a new File Editor from the file given
	 * @param fileToUse A filename in the directory 
	 */
	public FileEditor(File fileToUse) {
		this.file = fileToUse;
	}
	
	/**
	 * Finds the first line that contains the given key
	 * @param key The text to find
	 * @return The first line that it was found on.
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("null")
	public int findKey(String key) throws FileNotFoundException {
		scan = new Scanner(file);
		int line = 0;
		
		while(scan.hasNextLine()) {
			line++;
			if (scan.nextLine().contains(key)) {
				return line;
			}
		}
		
		scan.close();
		return (Integer) null;
	}
	
	/**
	 * Gets the text of a single line
	 * 
	 * @param lineNumber The line to get data from
	 * @return The text of the line
	 * @throws IOException If an I/O Exception occurs
	 */
	public String getLine(int lineNumber) throws IOException {
		try {
			reader = new FileReader(file);
			bReader = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			try {
				this.createFile();
			} catch (FileExistsException e1) {
				//This will never happen.
			}
			reader = new FileReader(file);
			bReader = new BufferedReader(reader);
		}
		
		for (int i = 0; i<lineNumber; i++) {
			bReader.readLine();
		}
		
		String returnStuff =  bReader.readLine();
		bReader.close();
		reader.close();
		
		return returnStuff;
		
	}

	/**
	 * Gets the text of the specified lines
	 * 
	 * @param startline The first line to start getting data from
	 * @param finalLine The last line to get data from
	 * @return The text of each line in an array
	 * @throws IOException If an I/O Exception occurs
	 */
	public String[] getLines(int startLine, int finalLine) throws IOException {
		return this.getLinesByNumber(startLine, (finalLine - (startLine - 1)));
	}
	
	/**
	 * Similar to getLines(int, int). Uses a starting point and an array size instead of a starting and ending line
	 * 
	 * @param startLine The line to start reading from
	 * @param lines The size of the array
	 * @return The text of each line as an array
	 * @throws IOException If an I/O Exception occurs.
	 */
	public String[] getLinesByNumber(int startLine, int lines) throws IOException {
		String[] returnStuff = new String[lines];
		try {
			reader = new FileReader(file);
			bReader = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			try {
				this.createFile();
			} catch (FileExistsException e1) {
				//This will never happen
			}
			reader = new FileReader(file);
			bReader = new BufferedReader(reader);
		}
		
		for (int i = 0; i<startLine; i++) {
			bReader.readLine();
		}
		
		for (int i = 0; i<lines; i++) {
			returnStuff[i] = bReader.readLine();
		}
		
		return returnStuff;
	}

	/**
	 * Gets the total number of lines in the file.
	 * @return The total number of lines
	 * @throws IOException If an I/O Exception occurs
	 */
	@SuppressWarnings("null")
	public int getTotalLines() throws IOException {
		stream = new FileInputStream(file);
		scan = new Scanner(stream);
		int total = -1;
		while(scan.hasNextLine()) {
			scan.nextLine();
			total++;
		}
		scan.close();
		stream.close();
		if (total == -1) {
			return (Integer) null;
		}
		return total;
	}

	/**
	 * Removes a line if it matches the sting given
	 * 
	 * @param text The text of the line to remove.
	 * @throws IOException If an I/O error occurs
	 */
	public void removeLine(String text) throws IOException {
		File tempFile = new File(file + ".jbfe.temp");

		reader = new FileReader(file);
		bReader = new BufferedReader(reader);
		writer = new FileWriter(tempFile);
		bWriter = new BufferedWriter(writer);

		String lineToRemove = text;
		String currentLine;

		while((currentLine = bReader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.equals(lineToRemove)) continue;
		    bWriter.write(currentLine + System.getProperty("line.separator"));
		}
		bWriter.close(); 
		bReader.close(); 
		reader.close();
		writer.close();
		tempFile.renameTo(file);
	}
	
	/**
	 * Removes a line based on its number. Note that the first line is line "0"
	 * @param line The line to remove
	 * @throws IOException If an I/O Exception occurs.
	 */
	public void removeLine(int line) throws IOException {
		File tempFile = new File(file + ".jbfe.temp");

		reader = new FileReader(file);
		bReader = new BufferedReader(reader);
		writer = new FileWriter(tempFile);
		bWriter = new BufferedWriter(writer);

		String currentLine;
		int lineNum = 0;

		while((currentLine = bReader.readLine()) != null) {
		    if (lineNum != line) {
			    bWriter.write(currentLine + System.getProperty("line.separator"));
		    }
		    lineNum++;
		}
		bWriter.close(); 
		bReader.close(); 
		reader.close();
		writer.close();
		file.delete();
		tempFile.renameTo(file);
	}
	
	/**
	 * Removes several lines based on their line number
	 * @param startLine The first line to remove
	 * @param lastLine The last line to remove
	 * @throws IOException If an I/O Exception occurs.
	 */
	public void removeLines(int startLine, int lastLine) throws IOException {
		for (int i = startLine; i <= lastLine; i++) {
			this.removeLine(startLine);
		}
	}
	
}
