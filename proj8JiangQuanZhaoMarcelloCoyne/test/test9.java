// should be 3 non trivial 
/*{}}{
 * File: Main.java
 * F18 CS361 Project 7
 * Names: Liwei Jiang, Tracy Quan, Danqing Zhao, Chris Marcello, Michael Coyne
 * Date: 10/23/2018
 */

package proj7JiangQuanZhaoMarcelloCoyne;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * This class creates a stage, as specified in Main.fxml, that contains a
 * set of tabs, embedded in a tab pane, with each tab window containing a
 * code area; a menu bar containing File and Edit menu; and a toolbar of
 * buttons for compiling, running, and stopping code; and a program console
 * that takes in standard input, displays standard output and program message.
 *
 * @author Liwei Jiang
 * @author Tracy Quan
 * @author Chris Marcello
 */
public class Main extends Application {
    private static Parent parentRoot;
    /**
     * Creates a stage as specified in Main.fxml, that contains a set of tabs,
     * embedded in a tab pane, with each tab window containing a code area; a menu
     * bar containing File and Edit menu; and a toolbar of buttons for compiling,
     * running, and stopping code; and a program console that takes in standard
     * input, displays standard output and program message.
     *
     * @param stage The stage that contains the window content
     */
    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/proj7JiangQuanZhaoMarcelloCoyne/Main.fxml"));
        Parent root = loader.load();
        Main.parentRoot = root;

        // initialize a scene and add features specified in the css file to the scene
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/proj7JiangQuanZhaoMarcelloCoyne/Main.css").toExternalForm());
        // configure the stage
        stage.setTitle("proj7JiangQuanZhaoMarcelloCoyne's Project 7");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> ((proj7JiangQuanZhaoMarcelloCoyne.Controller)loader.getController()).handleExitAction(event));
        stage.show();
    }

    /**
     * main function of Main class
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
