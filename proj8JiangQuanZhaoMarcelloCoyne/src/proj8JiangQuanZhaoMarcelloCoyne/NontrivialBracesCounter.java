package proj8JiangQuanZhaoMarcelloCoyne;
import java.io.*;
import java.lang.InterruptedException;

public class NontrivialBracesCounter {

    /**
     * Helper method for remove comments. Removes all comments from a string,
     * where a comment is defined by a startIndicator and endIndicator
     * @param startIndicator the String the indicates the start of a comment
     * @param endIndicator the String that indicates the end of a comment
     * @param source the text to be examined for comments
     * @return the string with comments removed
     */
    private String removeCommentsAndStringsHelper(String startIndicator, String endIndicator, String source) {
        int start = source.indexOf(startIndicator);
        if(start == -1) {
            return source;
        }
        int end = findEnd(endIndicator, source, start + 1);

        String strToRemove = source.substring(start, end + endIndicator.length());
        String result = source.replace(strToRemove, "");

        return removeCommentsAndStringsHelper(startIndicator, endIndicator, result);
    }

    /**
     * This method removes all comments, strings, and chars from a given string 
     * the string represents the contents of a .java file
     * @param source the string that comments are to be removed from
     * @return the string with all comments removed
     */
    private String removeCommentsAndStrings(String source) {
        String firstPass = removeCommentsAndStringsHelper("//", "\n", source);
        String secondPass = removeCommentsAndStringsHelper("\"", "\"", firstPass);
        String thirdPass = removeCommentsAndStringsHelper("\'", "\'", secondPass);
        return removeCommentsAndStringsHelper("/*", "*/", thirdPass);
    }

    /**
     * finds the location of the given endIndicator in the source string from the given index
     * @param endIndicator the String that represents the end indicator
     * @param source the source string to be examined for the given end indicator
     * @param from the index to begin the search from
     * @return the index of the endIndicator in the given source String
     */
    private int findEnd(String endIndicator, String source, int from){
        int end = source.indexOf(endIndicator, from);
        if((endIndicator == "\"" | endIndicator == "\'") && isEscaped(end, source)) {
            return findEnd(endIndicator, source, end + 1);
        }
        return end;
    }

    /**
     * determines whether a given character has been escaped in the given source string
     * @param idxOfCharacter the index of the character in the given source string
     * @param source the source string
     * @return a boolean indicating whether or not the character has been escaped
     */
    private boolean isEscaped(int idxOfCharacter, String source) {
        if(source.charAt(idxOfCharacter - 1) == '\\') {
            return true;
        }
        return false;
    }

    /**
    * reads in the given file and returns it as a single string
    * @param fileName the name of the java file to be checked
    * @return the file as a string, or empty string if the file failed to load
    */
    private String getFileAsString(String fileName) {
        File file = new File(fileName);
        try {
            FileInputStream in=new FileInputStream(file);
            int size=in.available();
            byte[] buffer=new byte[size];

            in.read(buffer);

            in.close();

            return( new String(buffer,"GB2312") );
        } catch (IOException e) {
            return("");
        }
    }

    /**
    * counts the number of non-trivial left braces in a java file
    * contains no error checking for whether the java file is valid java code
    * @param fileName the name of the java file to be checked
    * @return the count of non-trivial left braces, or -1 if the file failed to load
    */
    public int getNumNontrivialLeftBraces(String fileName) {
        String fileString = getFileAsString(fileName);
        if (fileString.equals("")) {
            return(-1);
        }
        fileString = removeCommentsAndStrings(fileString);
        int braceCount = 0;
        for(int i = 0; i<fileString.length(); i++) {
            if(fileString.charAt(i) == '{') {
                braceCount ++;
            }
        }
        return(braceCount);
    } 

    /**
    * Test method for testing the class. Uses command line argument for file name.
    * Displays an example of how a user could handle errors
    */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main [<filename>]");
        }
        else {
            NontrivialBracesCounter bracesCounter = new NontrivialBracesCounter();
            int count = bracesCounter.getNumNontrivialLeftBraces(args[0]);
            if (count != -1) {
                System.out.println("Number of non-trivial left braces: " + count);
            }
            else {
                System.out.println("Error: File " + args[0] + " not loaded correctly. Check file name and current directory.");
            }
        }
    }
}
