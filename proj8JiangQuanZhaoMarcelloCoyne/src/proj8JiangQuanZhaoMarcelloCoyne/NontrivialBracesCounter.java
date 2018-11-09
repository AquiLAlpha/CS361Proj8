/*
 * File: NontrivialBracesCounter.java
 * F18 CS361 Project 8
 * Names: Liwei Jiang, Tracy Quan, Danqing Zhao, Chris Marcello, Michael Coyne
 * Date: 11/06/2018
 * Contains the NontrivialBracesCounter class, analyzing a Java source file and finding all non-trivial left braces '{'.
 */

package proj8JiangQuanZhaoMarcelloCoyne;
import java.io.*;
import java.lang.*;

/**
 * NontrivialBracesCounter class, counting the number of non-trivial left braces in a legal Java program.
 *
 * @author Liwei Jiang
 * @author Tracy Quan
 * @author Danqing Zhao
 * @author Chris Marcello
 * @author Michael Coyne
 */
public class NontrivialBracesCounter {
    /**
     * Boolean flags indicating whether scanning within a single quote string,
     * a double quote string, a comment, or a block comment.
     */
    private boolean isStringDouble = false;
    private boolean isStringSingle = false;
    private boolean isComment = false;
    private boolean isBlockComment = false;

    /**
     * Helper method to count the number of non-trivial left braces in a string.
     *
     * @param source a string to be examined for the number of non-trivial left braces
     * @return the count of the non-trivial left braces in the given string
     */
    private int countNumNontrivialLeftBracesInString(String source) {
        // extend the beginning and the end of the string to avoid out of bound error
        source = " " + source + " ";
        int count = 0;

        for (int i = 1; i < source.length() - 1; i++) {
            // if the current character is in a non-trivial block
            // check if the current character is a left brace
            // or otherwise check whether the current character begins a trivial block (quote or comment)
            if (!this.isStringDouble && !this.isStringSingle && !this.isComment && !this.isBlockComment) {
                if (source.charAt(i) == '{')
                    count++;
                else
                    this.checkTrivialBeginIndicator(source, i);
            }
            // if the current character is in a trivial block (quote or comment)
            // then check whether the current character ends the trivial block
            else {
                this.checkTrivialEndIndicator(source, i);
            }
        }
        return count;
    }

    /**
     * Helper method to check whether a specified character is a begin indicator of a trivial block.
     * The begin indicators include ", ', //, /* for single quote, double quote, comment, and block comment.
     *
     * @param source a source string
     * @param index the index of the specified character
     */
    private void checkTrivialBeginIndicator(String source, int index) {
        if (source.charAt(index) == '\"')
            this.isStringDouble = true;
        else if (source.charAt(index) == '\'')
            this.isStringSingle = true;
        else if (source.charAt(index) == '/' && source.charAt(index + 1) == '/')
            this.isComment = true;
        else if (source.charAt(index) == '/' && source.charAt(index + 1) == '*')
            this.isBlockComment = true;
    }

    /**
     * Helper method to check whether a specified character is an end indicator of a trivial block.
     * The end indicators include non-escaped ", ', \n, * / for single quote, double quote, comment, and block comment.
     *
     * @param source the source string
     * @param index the index of the specified character
     */
    private void checkTrivialEndIndicator(String source, int index) {
        if (this.isStringDouble && source.charAt(index) == '\"' && !this.isEscaped(source, index))
            this.isStringDouble = false;
        else if (this.isStringSingle && source.charAt(index) == '\'' && !this.isEscaped(source, index))
            this.isStringSingle = false;
        else if (this.isComment && source.charAt(index) == '\n')
            this.isComment = false;
        else if (this.isBlockComment && source.charAt(index) == '*' && source.charAt(index + 1) == '/')
            this.isBlockComment = false;
    }

    /**
     * Helper method to determine whether a given character has been escaped in the given source string
     *
     * @param index the index of the character in the given source string
     * @param source the source string
     * @return a boolean indicating whether or not the character has been escaped
     */
    private boolean isEscaped(String source, int index) {
        int countBackslash = 0;
        int tmpIndex = index - 1;
        // count the number of consecutive backslashes before the given character
        while (tmpIndex >= 0 && source.charAt(tmpIndex) == '\\') {
            countBackslash++;
            tmpIndex--;
        }
        // if the number of backslashes is odd, then the character is escaped
        if (countBackslash%2 == 1) return true;
        return false;
    }

    /**
     * Helper method to get the content of a specified file.
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
            return(this.countNumNontrivialLeftBracesInString(fileContent));
        }
        return(-1);
    }

    /**
     * Main method of NontrivialBracesCounter class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        NontrivialBracesCounter counterObj = new NontrivialBracesCounter();
        System.out.println("------- test1.java -------");
        System.out.println("actual: 0");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test1.java"));
        System.out.println("------- test2.java -------");
        System.out.println("actual: 48");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test2.java"));
        System.out.println("------- test3.java -------");
        System.out.println("actual: 10");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test3.java"));
        System.out.println("------- test4.java -------");
        System.out.println("actual: 36");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test4.java"));
        System.out.println("------- test5.java -------");
        System.out.println("actual: 1");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test5.java"));
        System.out.println("------- test6.java -------");
        System.out.println("actual: 14");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test6.java"));
        System.out.println("------- test7.java -------");
        System.out.println("actual: 29");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test7.java"));
        System.out.println("------- test8.java -------");
        System.out.println("actual: 4038");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test8.java"));
        System.out.println("------- test9.java -------");
        System.out.println("actual: 3");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test9.java"));
        System.out.println("------- test10.java -------");
        System.out.println("actual: 21");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test10.java"));
        System.out.println("------- test11.java -------");
        System.out.println("result: " + counterObj.getNumNontrivialLeftBraces("test/test11.java"));
    }
}