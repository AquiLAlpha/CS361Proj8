/*
 * File: NontrivialBracesCounter.java
 * F18 CS361 Project 8
 * Names: Liwei Jiang, Tracy Quan, Danqing Zhao, Chris Marcello, Michael Coyne
 * Date: 11/06/2018
 * This file contains the NontrivialBracesCounter class,
 * analyzing a Java source file and finding all non-trivial left braces '{'.
 */

package proj8JiangQuanZhaoMarcelloCoyne;
import java.io.*;
import java.lang.*;

/**
 * NontrivialBracesCounter class, counting the number of non-trivial left braces in a legal Java program.
 */
public class NontrivialBracesCounter {
    private boolean ifStringSingle = false;
    private boolean ifStringDouble = false;
    private boolean ifComment = false;
    private boolean ifBlockComment = false;


    private int countNumLeftBracesInString(String source) {
        source = " " + source + " ";
        int count = 0;

        this.checkStartIndicator(source, 0);
        for (int i = 1; i < source.length() - 1; i++) {
            if (!this.ifStringSingle && !this.ifStringDouble && !this.ifComment && !this.ifBlockComment) {
                if (source.charAt(i) == '{') count++;
                this.checkStartIndicator(source, i);
            } else {
                this.checkEndIndicator(source, i);
            }
        }

        return count;
    }


    private void checkStartIndicator(String source, int index) {
        if (source.charAt(index) == '\"') this.ifStringDouble = true;
        else if (source.charAt(index) == '\'') this.ifStringSingle = true;
        else if (source.charAt(index) == '/' && source.charAt(index + 1) == '/') this.ifComment = true;
        else if (source.charAt(index) == '/' && source.charAt(index + 1) == '*') this.ifBlockComment = true;
    }


    private void checkEndIndicator(String source, int index) {
        if (source.charAt(index) == '\"' && this.ifStringDouble) this.ifStringDouble = false;
        else if (source.charAt(index) == '\'' && this.ifStringSingle) this.ifStringSingle = false;
        else if (source.charAt(index) == '\n' && this.ifComment) this.ifComment = false;
        else if (source.charAt(index) == '*' && source.charAt(index + 1) == '/' && this.ifBlockComment) this.ifBlockComment = false;
    }


    /**
     * Helper method to get the content of the specified file.
     *
     * @param fileName the name of the File to get the content from
     * @return a String of the file content; null if an error occurs when reading the specified file
     */
    private String getFileContent(String fileName) {
        try {
            FileInputStream fileInput = new FileInputStream(new File(fileName));
            byte[] buffer = new byte[fileInput.available()];
            fileInput.read(buffer); // read the file into a buffer
            fileInput.close();
            return new String(buffer,"GB2312");
        }
        catch (FileNotFoundException e) {
            System.out.println("ERROR: " + fileName + " not found!");
            return null;
        }
        catch (IOException e) {
            System.out.println("ERROR: " + fileName + " cannot be opened!");
            return null;
        }
    }

    /**
     * Counts the number of non-trivial left braces in the specified Java file.
     *
     * @param fileName the java file to be examined
     * @return the number of non-trivial left braces in the specified Java file;
     *         -1 if exception occurs when opening the file
     */
    public int getNumNontrivialLeftBraces(String fileName) {
            String fileContent = this.getFileContent(fileName);
            // if the file opens successfully
            if (fileContent != null) {
                return(this.countNumLeftBracesInString(fileContent));
            }
            return(-1);
    }

    /**
     * Main method to of NontrivialBracesCounter class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        NontrivialBracesCounter counterObj = new NontrivialBracesCounter();
        System.out.println("------- test1.java -------");
        System.out.println("actual: 3");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test1.java"));
        System.out.println("------- test2.java -------");
        System.out.println("actual: 48");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test2.java"));
        System.out.println("------- test3.java -------");
        System.out.println("actual: 10");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test3.java"));
    }
}
