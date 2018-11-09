package proj8JiangQuanZhaoMarcelloCoyne;
import java.io.*;
import java.lang.InterruptedException;

public class Main {

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

    public int getNumNontrivialLeftBraces(String fileName){
        File file = new File(fileName);
        try {
            FileInputStream in=new FileInputStream(file);
            int size=in.available();
            byte[] buffer=new byte[size];

            in.read(buffer);

            in.close();

            String str = new String(buffer,"GB2312");
            str = removeCommentsAndStrings(str);
            System.out.println(str);
            int braceCount = 0;
            for(int i = 0; i<str.length(); i++){
                if(str.charAt(i) == '{'){
                    braceCount ++;
                }
            }

            return(braceCount);

        } catch (IOException e) {
            e.printStackTrace();
            return(-1);
        }

    }

    public static void main(String[] args) {
        Main main = new Main();
        int count = main.getNumNontrivialLeftBraces("/Users/michaelcoyne/Downloads/Test.java");
        System.out.println(count);
    }
}
