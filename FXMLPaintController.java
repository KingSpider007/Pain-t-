/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import static java.awt.SystemColor.text;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.awt.image.RenderedImage;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.transform.Scale;
import javafx.stage.WindowEvent;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javax.management.timer.Timer;

/**
 *
 * @author mspiv
 */
public class FXMLPaintController implements Initializable {

    ImageView picture = new ImageView();
    ColorPicker colorPicker = new ColorPicker();
    File file;
    Slider slider = new Slider();
    Stage primaryStage;
    Scale scale = new Scale();
    Label label;
    Double startingPosX;
    Double startingPosY;
    Double endingPosX;
    Double endingPosY;
    Double centerStartX;
    Double centerStartY;
    Double centerEndX;
    Double centerEndY;
    Double selRect;
    double[] xCoord, yCoord;    // Arrays containing the points of 
    //   the polygon.  Up to 500 points 
    //   are allowed.

    int pointCt;  // The number of points that have been input.

    boolean complete;   // Set to true when the polygon is complete.
    // When this is false, only a series of lines are drawn.
    // When it is true, a filled polygon is drawn.

    @FXML
    private MenuItem openBut;
    @FXML
    private MenuItem svBut;
    @FXML
    private MenuItem saveBut;
    @FXML
    private MenuItem exitBut;
    @FXML
    private MenuItem undoBut;
    @FXML
    private MenuItem redoBut;
    @FXML
    private MenuItem inBut;
    @FXML
    private MenuItem outBut;
    @FXML
    private MenuItem aboutBut;
    @FXML
    private MenuItem infoBut;
    @FXML
    private MenuItem hotBut;
    @FXML
    private Circle testCircle;
    @FXML
    private ColorPicker colorPick;
    @FXML
    private Slider control;
    @FXML
    private ToggleButton draw;
    @FXML
    private ToggleButton line;
    @FXML
    private ToggleButton elipse;
    @FXML
    private ToggleButton square;
    @FXML
    private ToggleButton rec;
    @FXML
    private ToggleButton circle;
    @FXML
    private ToggleButton triangle;
    @FXML
    private ToggleButton colorGrabber;
    @FXML
    private ToggleGroup tools;
    @FXML
    private ToggleButton fill;
    @FXML
    private ToggleButton eraser;
    @FXML
    private ToggleButton placer;
    @FXML
    private ToggleButton polygon;

    @FXML
    private ToggleButton textBubble;
    @FXML
    private ToggleButton clearCanvas;
    @FXML
    private ToggleGroup tools1;
    @FXML
    Canvas canvas;
    @FXML
    private Canvas canvas1;
    @FXML
    private Canvas canvas2;
    @FXML
    private Canvas canvas3;
    @FXML
    private Canvas canvas4;
    @FXML
    private Canvas canvas5;
    @FXML
    private Canvas canvas6;
    @FXML
    private Label ToolSelected;

    /**
     * A section of the code where the code you where you can see the hot keys
     * This is where the you can see the canvas and where you can see overall
     * impacts.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openBut.setAccelerator(new KeyCodeCombination(KeyCode.O,
                KeyCombination.CONTROL_DOWN));
        svBut.setAccelerator(new KeyCodeCombination(KeyCode.S,
                KeyCombination.CONTROL_DOWN));
        saveBut.setAccelerator(new KeyCodeCombination(KeyCode.S,
                KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        infoBut.setAccelerator(new KeyCodeCombination(KeyCode.I,
                KeyCombination.CONTROL_DOWN));
        aboutBut.setAccelerator(new KeyCodeCombination(KeyCode.A,
                KeyCombination.CONTROL_DOWN));
        exitBut.setAccelerator(new KeyCodeCombination(KeyCode.F,
                KeyCombination.CONTROL_DOWN));

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        xCoord = new double[500];  // create arrays to hold the polygon's points
        yCoord = new double[500];
        pointCt = 0;

        draw.setToggleGroup(tools);
        line.setToggleGroup(tools);
        square.setToggleGroup(tools);
        rec.setToggleGroup(tools);
        elipse.setToggleGroup(tools);
        triangle.setToggleGroup(tools);
        colorGrabber.setToggleGroup(tools);
        fill.setToggleGroup(tools);
        circle.setToggleGroup(tools);
        eraser.setToggleGroup(tools);
        polygon.setToggleGroup(tools);
        placer.setToggleGroup(tools);
        textBubble.setToggleGroup(tools);
        clearCanvas.setToggleGroup(tools);

        draw.setTooltip(new Tooltip("Draw Freeform lines"));
        line.setTooltip(new Tooltip("Creates a line from 2 points"));
        square.setTooltip(new Tooltip("Draws a Square"));
        rec.setTooltip(new Tooltip("Draws a Rectangle"));
        elipse.setTooltip(new Tooltip("Draws a Oval"));
        triangle.setTooltip(new Tooltip("Draws a Triangle"));
        colorGrabber.setTooltip(new Tooltip("Grabs the Color"));
        fill.setTooltip(new Tooltip("Fills the Line color"));
        circle.setTooltip(new Tooltip("Draws a Circle"));
        eraser.setTooltip(new Tooltip("Eraser"));
        polygon.setTooltip(new Tooltip("Draws a 6 sided polygon"));
        placer.setTooltip(new Tooltip("Place points to create new shapes"));
        textBubble.setTooltip(new Tooltip("Places my default text"));
        clearCanvas.setTooltip(new Tooltip("Clears the Canvas"));

    }

    /*
    @FXML
    private void TimerTask(ActionEvent event){
        Timer time = new TimerTask() {
            @Override
            public void run() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            };
    
                public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
            })  
        Timer timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(time, 0, 1000);
            }
        }
    }
     */
    /**
     * Registers your input to Open the Canvas. If hot key is being used or you
     * hit the open button. The program should allow you to open images and add
     * them to the canvas.
     *
     * @param openBut open an image onto the canvas.
     */
    @FXML
    private void openBut(ActionEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("jpeg", ".*jpeg"),
                new FileChooser.ExtensionFilter("png", "*.png"));
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Image pic = new Image(file.toURI().toURL().toString());
                if (pic.getWidth() > canvas.getWidth()) {
                    canvas.setWidth(pic.getWidth());
                }
                if (pic.getHeight() > canvas.getHeight()) {
                    canvas.setHeight(pic.getHeight());
                }
                gc.drawImage(pic, 0, 0, pic.getWidth(), pic.getHeight());
            } catch (Exception ex) {
                System.out.println("File not found");
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }

    /**
     * Registers your input to save the Canvas. If hot key is being used or you
     * hit the save button. The program should allow you to save the canvas and
     * add a name to the save image if the canvas is already saved with a name.
     *
     * @param svBut saves the canvas as a image.
     */
    @FXML
    private void svBut(ActionEvent event) {
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(
                        writableImage, null);
                ImageIO.write(renderedImage, "png", file);
                ImageIO.write(renderedImage, "jpg", file);
                ImageIO.write(renderedImage, "jpeg", file);
            } catch (IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }

    /**
     * Registers your input to Save as the Canvas. If hot key is being used or
     * you hit the save button. The program should allow you to save the canvas
     * and add a name to the save image if the canvas is already saved with a
     * name. You are allowed to change the name which allows it to be saved as a
     * different image.
     *
     * @param saveBut saves the canvas as a image.
     */
    @FXML
    private void saveBut(ActionEvent event) {
        FileChooser fc = new FileChooser();
        //Save As Button Which lets you pick the same your saving 
        fc.setTitle("Save File");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "."),
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter(",.jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".jpeg", "*.jpeg"),
                new FileChooser.ExtensionFilter(".png", "*.png"));
        File save = fc.showSaveDialog(primaryStage);
        if (save != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(
                        writableImage, null);
                ImageIO.write(renderedImage, "png", save);
                ImageIO.write(renderedImage, "jpg", save);
                ImageIO.write(renderedImage, "jpeg", save);

                file = save;

            } catch (IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }
    }

    /**
     * Registers your input to get to the exit GUI.If hot key is being used or
     * you hit the exit button the problem should have this pop-up and asked if
     * you will like to save the program and this it will close the program.
     *
     * @param exitBut the program.
     */
    @FXML
    private void exitBut(ActionEvent event) {
        //This only works with the hotkey make sure to get it on the exit button
        //primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) exitBut);
        Alert exit = new Alert(Alert.AlertType.WARNING);
        exit.setTitle("Warning");
        exit.setHeaderText("Hold On");
        String s = "Do you want to save your files before you leave?";
        exit.setContentText(s);

        ButtonType buttonSave = new ButtonType("Save");
        ButtonType buttonDont = new ButtonType("Don't Save");
        ButtonType buttonCancel = new ButtonType("Cancel",
                ButtonData.CANCEL_CLOSE);

        exit.getButtonTypes().setAll(buttonSave, buttonDont, buttonCancel);

        Optional<ButtonType> result = exit.showAndWait();
        //
        if (result.get() == buttonSave) {
            //if you want to save and leave save the program and close it
            if (file != null) {
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);
                try {
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(
                            writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                    ImageIO.write(renderedImage, "jpg", file);
                    ImageIO.write(renderedImage, "jpeg", file);
                } catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
                Platform.exit();
                System.exit(0);
                primaryStage.close();
            }

        } else if (result.get() == buttonDont) {
            //If you dont save just close the program
            Platform.exit();
            System.exit(0);
            primaryStage.close();

        } else {
            //Cancel Button it doesnt do anything just returns you back
            //to the program.

        }
    }

    /**
     * This is added to the the toolBar. This to to undo happy mistakes that you
     * may or may not like. It can help you when you change your mind and hit
     * the redo button too many times.
     *
     * @param "redoBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void undoBut(ActionEvent event) {
    }

    /**
     * This is added to the the toolBar. This to to redo happy mistakes that you
     * may or may not like. It can help you when you change your mind and hit
     * the undo button too many times.
     *
     * @param "redoBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void redoBut(ActionEvent event) {

    }

    /**
     * This is added to the the toolBar. This to zoom in of the canvas to get a
     * much better view of the canvas sense your only able to see so much of the
     * canvas at once on the screen.
     *
     * @param "inBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void inBut(ActionEvent event) {
        // Link this to the slider to zoom in
        double zoomIn = 1.1;
        canvas.setScaleX(1 * zoomIn);
        canvas.setScaleY(1 * zoomIn);
    }

    /**
     * This is added to the the toolBar. This to zoom out of the canvas to get a
     * much wider view of the canvas sense your only able to see so much of the
     * canvas at once on the screen.
     *
     * @param "outBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void outBut(ActionEvent event) {
        // Link this to the slider to zoom out
        double zoomOut = 1.1;
        canvas.setScaleX(1 / zoomOut);
        canvas.setScaleY(1 / zoomOut);
    }

    /**
     * This is added to the the artBar. This will help the user see what is
     * happening inside of the program. When selecting colors they will be able
     * to see and understand what colors are being selected with the TestCircle.
     * This can also give the user an idea on what is happening
     *
     * @param "infoBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void colorPicker(ActionEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(colorPick.getValue());
        testCircle.setFill(colorPick.getValue());
        gc.setStroke(colorPick.getValue());
        testCircle.setStroke(colorPick.getValue());
    }

    /**
     * This is added to the the toolBar. This will help the user understand what
     * is going on with the code and when is the next update is coming. This can
     * be used to tell when was the last time I updated said code.
     *
     * @param "aboutBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void aboutBut(ActionEvent event) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("About");
        info.setHeaderText("Overall");
        info.setContentText("Weekly Changes are being added each week.\n"
                + "M.Spivey is going to make sure that all of the problems\n"
                + "are going to be patched or fixed.");
        info.showAndWait();
    }

    /**
     * This is added to the the toolBar. This will help the user understand what
     * is going on with the code and when is the next update is coming. This can
     * be used to tell when was the last time I updated said code.
     *
     * @param "infoBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void infoBut(ActionEvent event) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Information");
        info.setHeaderText("Information");
        info.setContentText("Please Contact M.Spivey \n"
                + "Paint Ver: 2.3.5\nUpdated Weekly");
        info.showAndWait();

    }

    /**
     * This is connected to the toolBar. This will help the user understand some
     * of the hot keys that are created on the Paint project.
     *
     * @param "hotBut" ToggleButton to be added to the toolBar
     */
    @FXML
    private void hotBut(ActionEvent event) {
        Alert shortCuts = new Alert(Alert.AlertType.INFORMATION);
        shortCuts.setTitle("Hotkeys");
        shortCuts.setHeaderText("Information");
        shortCuts.setContentText("Open file = ctrl + O \n"
                + "Save As = ctrl + alt + S\n" + "Save = ctrl + S\n"
                + "Exit = ctrl + F\n ");
        shortCuts.showAndWait();
    }

    /**
     * Slider is created as well as the corresponding with the TestCircle. The
     * testCircle is created to give you an idea about how big the lines are
     * going to get before drawing them on the canvas.
     *
     * @param "slider" ToggleButton to be added to the toolBar
     */
    @FXML
    private void slider(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        control.getValue();
        testCircle.setRadius(control.getValue() / 2);
        gc.setLineWidth(control.getValue());
    }

    @FXML
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (complete) { // draw a polygon
            gc.setFill(colorPick.getValue());
            gc.fillPolygon(xCoord, yCoord, pointCt);
            gc.strokePolygon(xCoord, yCoord, pointCt);
        } else { // show the lines the user has drawn so far
            gc.setFill(Color.BLACK);
            gc.fillRect(xCoord[0] - 2, yCoord[0] - 2, 4, 4);  // small square marks first point
            for (int i = 0; i < pointCt - 1; i++) {
                gc.strokeLine(xCoord[i], yCoord[i], xCoord[i + 1], yCoord[i + 1]);
            }
        }
    }

    @FXML
    private void colorGrabber(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage snapshot = canvas.snapshot(parameters, writableImage);
        PixelReader pixel = snapshot.getPixelReader();
        colorPick.setValue(pixel.getColor((int) event.getX(), (int) event.getY()));
    }

    /**
     * ToggleButton is created as well as the corresponding EventHandler. The
     * EventHandler handles the on the canvas. This is to help have all of the
     * tools be in a group to be able to test the actions and see what is going
     * on with certain tools.
     *
     * @param "mousePresser" ToggleButton to be added to the toolBar
     */
    @FXML
    private void mousePresser(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        String toolstring = tools.getSelectedToggle().toString();

        if (toolstring.contains("draw")) {
            System.out.println("Drawing");
            gc.moveTo(event.getX(), event.getY());
            gc.beginPath();
            gc.stroke();
        }
        if (toolstring.contains("line")) {
            System.out.println("Drawing Line");
            startingPosX = event.getX();
            startingPosY = event.getY();
            gc.lineTo(event.getX(), event.getY());
            gc.beginPath();
        }
        if (toolstring.contains("eraser")) {
            System.out.println("Erase");
            gc.setLineWidth(15);
            gc.moveTo(event.getX(), event.getY());
            gc.setStroke(Color.TRANSPARENT);
            //gc.beginPath();
        }
        if (toolstring.contains("fill")) {
            System.out.println("Fill Area");
            gc.moveTo(event.getX(), event.getY());
        }
        if (toolstring.contains("circle")) {
            System.out.println("Drawing Circle");
            centerStartX = event.getX();
            centerStartY = event.getY();
            centerEndX = event.getX();
            centerEndY = event.getY();
        }
        if (toolstring.contains("elipse")) {
            System.out.println("Drawing Elipse");
            centerStartX = event.getX();
            centerStartY = event.getY();
        }
        if (toolstring.contains("rec")) {
            System.out.println("Drawing Rec");
            startingPosX = event.getX();
            startingPosY = event.getY();
        }
        if (toolstring.contains("square")) {
            System.out.println("Drawing Square");
            startingPosX = event.getX();
            startingPosY = event.getY();
            endingPosX = event.getX();
            endingPosY = event.getY();
        }
        if (toolstring.contains("placer")) {
            System.out.println("Drawing Placer");
            if (complete) {
                // Start a new polygon at the point that was clicked.
                complete = false;
                xCoord[0] = event.getX();
                yCoord[0] = event.getY();
                pointCt = 1;
            } else if (pointCt > 0 && pointCt > 0 && (Math.abs(xCoord[0] - event.getX()) <= 3)
                    && (Math.abs(yCoord[0] - event.getY()) <= 3)) {
                // User has clicked near the starting point.
                // The polygon is complete.
                complete = true;
            } else if (event.getButton() == MouseButton.SECONDARY || pointCt == 500) {
                // The polygon is complete.
                complete = true;
            } else {
                // Add the point where the user clicked to the list of
                // points in the polygon, and draw a line between the
                // previous point and the current point.  A line can
                // only be drawn if there are at least two points.
                xCoord[pointCt] = event.getX();
                yCoord[pointCt] = event.getY();
                pointCt++;
            }
            draw();
        }
        if (toolstring.contains("polygon")) {
            System.out.println("Drawing Polygon");
            startingPosX = event.getX();
            startingPosY = event.getY();
            endingPosX = event.getX();
            endingPosY = event.getY();
        }
        if (toolstring.contains("textBubble")) {
            System.out.println("Drawing Text");
            startingPosX = event.getX();
            startingPosY = event.getY();
            gc.setLineWidth(1);
            //gc.setFont(Font.font(slider.getValue()));
            gc.setStroke(Color.BLACK);
            gc.setFill(colorPick.getValue());
            //gc.fillText(text.toString().getText(), event.getX(), event.getY());
            //gc.strokeText(text.getText(), event.getX(), event.getY());
        }
        if (toolstring.contains("clearCanvas")) {
            System.out.println("Clearing Canvas");
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }
        if (toolstring.contains("copy")) {
            System.out.println("Copy");
            //selRect.setX(event.getX());
            //selRect.setY(event.getY());
        }
        if (toolstring.contains("triangle")) {
            System.out.println("triangle");

        }
        System.out.println(event.getX() + "," + event.getY());
    }

    /**
     * ToggleButton is created as well as the corresponding EventHandler. The
     * EventHandler handles the on the canvas. These tools are used to control
     * or impact the canvas.
     *
     * @param "mouseDragger" ToggleButton to be added to the toolBar
     */
    @FXML
    private void mouseDragger(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        String toolstring = tools.getSelectedToggle().toString();

        if (toolstring.contains("draw")) {
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        }
        if (toolstring.contains("line")) {
            //gc.lineTo(event.getX(), event.getY());
            gc.strokeLine(startingPosX, startingPosY, event.getX(), event.getY());
        }
        if (toolstring.contains("eraser")) {
            //gc.lineTo(event.getX(), event.getY());
            gc.stroke();
            gc.clearRect(startingPosX, startingPosY, event.getX(), event.getY());
        }
        if (toolstring.contains("fill")) {
            gc.lineTo(event.getX(), event.getY());
            gc.getFill();
        }
        if (toolstring.contains("circle")) {
            double dia;
            if ((centerEndX != event.getX()) || (centerEndY != event.getY())) {
                centerEndX = event.getX();
                centerEndY = event.getY();

                dia = Math.abs(Math.abs(centerEndX) - Math.abs(centerStartX));

                if (centerEndX < centerStartX && centerEndY < centerStartY) {
                    gc.fillOval(centerStartX - dia, centerStartY - dia, dia,
                            dia);
                    gc.strokeOval(centerStartX - dia, centerStartY - dia, dia,
                            dia);
                } else if (centerEndY < centerStartY) {
                    gc.fillOval(centerStartX, centerStartY - dia, dia, dia);
                    gc.strokeOval(centerStartX, centerStartY - dia, dia, dia);
                } else if (centerEndX < centerStartX) {
                    gc.fillOval(centerStartX - dia, centerStartY, dia, dia);
                    gc.strokeOval(centerStartX - dia, centerStartY, dia, dia);
                } else {
                    gc.fillOval(centerStartX, centerStartY, dia, dia);
                    gc.strokeOval(centerStartX, centerStartY, dia, dia);
                }
            }

        }
        if (toolstring.contains("elipse")) {
            double diaX, diaY;
            if ((centerEndX != event.getX()) || (centerEndY != event.getY())) {
                centerEndX = event.getX();
                centerEndY = event.getY();

                diaX = Math.abs(Math.abs(centerEndX) - Math.abs(centerStartX));
                diaY = Math.abs(Math.abs(centerEndY) - Math.abs(centerStartY));

                if (centerEndX < centerStartX && centerEndY < centerStartY) {
                    gc.fillOval(centerStartX - diaX, centerStartY - diaY,
                            diaX, diaY);
                    gc.strokeOval(centerStartX - diaX, centerStartY - diaY,
                            diaX, diaY);
                } else if (centerEndY < centerStartY) {
                    gc.fillOval(centerStartX, centerStartY - diaY, diaX,
                            diaY);
                    gc.strokeOval(centerStartX, centerStartY - diaY, diaX,
                            diaY);
                } else if (centerEndX < centerStartX) {
                    gc.fillOval(centerStartX - diaX, centerStartY, diaX,
                            diaY);
                    gc.strokeOval(centerStartX - diaX, centerStartY, diaX,
                            diaY);
                } else {
                    gc.fillOval(centerStartX, centerStartY, diaX, diaY);
                    gc.strokeOval(centerStartX, centerStartY, diaX, diaY);
                }
            }
        }
        if (toolstring.contains("rec")) {
            double width, height;
            if ((endingPosX != event.getX()) || (endingPosY != event.getY())) {
                endingPosX = event.getX();
                endingPosY = event.getY();

                width = Math.abs(Math.abs(endingPosX) - Math.abs(startingPosX));
                height = Math.abs(Math.abs(endingPosY) - Math.abs(startingPosY));

                if (endingPosX < startingPosX && endingPosY < startingPosY) {
                    gc.fillRect(startingPosX - width, startingPosY - height,
                            width, height);
                    gc.strokeRect(startingPosX - width, startingPosY - height,
                            width, height);

                } else if (endingPosY < startingPosY) {
                    gc.fillRect(startingPosX, startingPosY - height, width,
                            height);
                    gc.strokeRect(startingPosX, startingPosY - height, width,
                            height);

                } else if (endingPosX < startingPosX) {
                    gc.fillRect(startingPosX - width, startingPosY, width,
                            height);
                    gc.strokeRect(startingPosX - width, startingPosY, width,
                            height);
                } else {
                    gc.fillRect(startingPosX, startingPosY, width, height);
                    gc.strokeRect(startingPosX, startingPosY, width, height);
                }
            }
        }
        if (toolstring.contains("square")) {
            double width;
            if ((endingPosX != event.getX()) || (endingPosY != event.getY())) {
                endingPosX = event.getX();
                endingPosY = event.getY();

                width = Math.abs(endingPosX - startingPosX);

                if (endingPosX < startingPosX && endingPosY < startingPosY) {
                    gc.fillRect(startingPosX - width, startingPosY - width,
                            width, width);
                    gc.strokeRect(startingPosX - width, startingPosY - width,
                            width, width);

                } else if (endingPosY < startingPosY) {
                    gc.fillRect(startingPosX, startingPosY - width, width,
                            width);
                    gc.strokeRect(startingPosX, startingPosY - width, width,
                            width);

                } else if (endingPosX < startingPosX) {
                    gc.fillRect(startingPosX - width, startingPosY, width,
                            width);
                    gc.strokeRect(startingPosX - width, startingPosY, width,
                            width);
                } else {
                    gc.fillRect(startingPosX, startingPosY, width, width);
                    gc.strokeRect(startingPosX, startingPosY, width, width);
                }
            }
        }
        if (toolstring.contains("placer")) {
            System.out.println(event.getX() + "," + event.getY());

        }
        if (toolstring.contains("polygon")) {

        }
        if (toolstring.contains("textBubble")) {

        }
        if (toolstring.contains("clearCanvas")) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }

        System.out.println(event.getX() + "," + event.getY());
    }

    /**
     * ToggleButton is created as well as the corresponding EventHandler. The
     * EventHandler handles the on the canvas. These will close the shapes and
     * tools after I release the tools.
     *
     * @param "mouseReleaser" ToggleButton to be added to the toolBar
     */
    @FXML
    private void mouseReleaser(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.closePath();
        System.out.println(event.getX() + "," + event.getY());
    }

}
