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

        int end = source.indexOf(endIndicator, start + 1);
        String strToRemove = source.substring(start, end + endIndicator.length());
        String result = source.replace(strToRemove, "");

        return removeCommentsAndStringsHelper(startIndicator, endIndicator, result);
    }

    /**
     * This method removes all comments from a given string
     * @param source the string that comments are to be removed from
     * @return the string with all comments removed
     */
    public  String removeCommentsAndStrings(String source) {
        String firstPass = removeCommentsAndStringsHelper("//", "\n", source);
        String secondPass = removeCommentsAndStringsHelper("\"", "\"", firstPass);
        String thirdPass = removeCommentsAndStringsHelper("\'", "\'", secondPass);
        return removeCommentsAndStringsHelper("/*", "*/", thirdPass);
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
        int count = main.getNumNontrivialLeftBraces("C:\\Users\\Danqing Zhao\\Desktop\\CS361\\HelloWorld.java");
        System.out.println(count);
    }
}
