package UI.refactoredUI.gameMap;

import BLL.ACQ.IPlanet;
import UI.refactoredUI.components.*;
import UI.refactoredUI.planet.Globe;
import UI.refactoredUI.spaceship.Spaceship;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.*;

public class GameMap extends Component implements IGameMap {

    private List<IEventListener<AbstractMap.SimpleImmutableEntry<Double, Double>>> onMovementSubscribers;

    ISpaceship spaceship;
    private Camera camera;
    private Group root;
    private Pane map;
    private int numberOfStars = 200;

    @FXML
    private AnchorPane wrapper;

    @FXML
    private AnchorPane upperRightCorner;

    @FXML
    private SubScene subscene;


    public GameMap(){
        super("gamemap_view.fxml");
        camera = new ParallelCamera();
        map = new Pane();
        root = new Group();
        spaceship = new Spaceship();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ComponentLoader.loadComponent(map, spaceship.getView(), mapWidth/2, mapHeight/2);

        /** Listen to spaceship velocity and pass this to subscribers. */
        spaceship.onMovement(data -> onMovementSubscribers.forEach(listener -> listener.onAction(data)));

        map.setMinWidth(wrapper.widthProperty().getValue());
        map.setMinHeight(wrapper.heightProperty().getValue());
        map.setBackground(new Background(new BackgroundImage(new Image("./UI/resources/img/grid.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        map.setStyle("-fx-border-color: #333333");
        map.setMinWidth(mapWidth);
        map.setMinHeight(mapHeight);
        subscene.widthProperty().bind(wrapper.widthProperty());
        subscene.heightProperty().bind(wrapper.heightProperty());
        root.getChildren().add(map);
        subscene.setRoot(root);
        subscene.setCamera(camera);
        renderStars();
        renderCenterLabel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMovement(IEventListener<AbstractMap.SimpleImmutableEntry<Double, Double>> listener) {
        onMovementSubscribers.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotateSpaceshipLeft(boolean state) {
        spaceship.setRotateLeft(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rotateSpaceshipRight(boolean state) {
        spaceship.setRotateRight(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accelerateSpaceship(boolean state) {
        spaceship.setAccelerate(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decelerateSpaceship(boolean state) {
        spaceship.setDecelerate(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderPlanets(Map<String, ? extends IPlanet> planets) {
        for (IPlanet iPlanet : planets.values()) {
            ComponentLoader.loadComponent(map, (((IGlobe)new Globe(iPlanet.getName(), iPlanet.getMap2D().toURI().toString().replace("\\","/")))).getView(), iPlanet.getX(), iPlanet.getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void centerCamera(double coordX, double coordY) {
        camera.setTranslateX(coordX - (subscene.getWidth()/2));
        camera.setTranslateY(coordY - (subscene.getHeight()/2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpaceshipCoordX(double coordX) {
        spaceship.getView().setTranslateX(coordX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpaceshipCoordY(double coordY) {
        spaceship.getView().setTranslateY(coordY);
    }

    /**
     * Method to render graphical elements that visualize stars.
     */
    private void renderStars(){
        for (int i = 0; i < numberOfStars; i++){
            Circle star = new Circle((int) (Math.random() * mapWidth), (int) (Math.random() * mapHeight),10 + (int)(Math.random() * ((80 - 10) + 1)));
            Stop[] stops = {new Stop(0, Color.WHITE), new Stop(0.02,Color.WHITE), new Stop(0.025,Color.rgb(255,255,255,0.2)), new Stop(1, Color.TRANSPARENT)};
            RadialGradient rg = new RadialGradient(0,0.1,star.getCenterX(), star.getCenterY(), star.getRadius(), false, CycleMethod.NO_CYCLE, stops);
            star.setFill(rg);
            star.setCache(true);
            map.getChildren().add(star);
        }
    }

    /**
     * Method to render label in the center of the game map.
     */
    private void renderCenterLabel(){
        Label centerLabel = new Label("Center of the Univserse");
        centerLabel.setStyle("-fx-font-family: 'Circular Std Bold'; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 20;");
        centerLabel.setTranslateX(mapWidth/2 - centerLabel.getWidth()/2);
        centerLabel.setTranslateY(mapHeight/2 - centerLabel.getHeight()/2);
        centerLabel.setCache(true);
        map.getChildren().add(centerLabel);
    }

}
