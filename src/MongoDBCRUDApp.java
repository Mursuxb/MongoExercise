import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MongoDBCRUDApp extends Application {

    // MongoDB connection parameters
    private static final String uri = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "testdb";
    private static final String COLLECTION_NAME = "testcollection";

    // MongoDB client and collection
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    // Text fields
    private TextField idField;
    private TextField nameField;
    private TextField ageField;
    private TextField cityField;

    @Override
    public void start(Stage primaryStage) {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);

        // Creating labels
        Label idLabel = new Label("ID:");
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label cityLabel = new Label("City:");

        // Creating text fields
        idField = new TextField();
        nameField = new TextField();
        ageField = new TextField();
        cityField = new TextField();

        // Creating buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String city = cityField.getText();

            Document document = new Document("id", id)
                    .append("name", name)
                    .append("age", age)
                    .append("city", city);
            collection.insertOne(document);
        });

        Button readButton = new Button("Read");
        readButton.setOnAction(e -> {
            String id = idField.getText();
            Document query = new Document("id", id);
            FindIterable<Document> result = collection.find(query);
            Document document = result.first();
            if (document != null) {
                nameField.setText(document.getString("name"));
                ageField.setText(Integer.toString(document.getInteger("age")));
                cityField.setText(document.getString("city"));
            } else {
                clearFields();
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String city = cityField.getText();

            Document query = new Document("id", id);
            Document update = new Document("$set", new Document("name", name)
                    .append("age", age)
                    .append("city", city));
            collection.updateOne(query, update);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String id = idField.getText();
            Document query = new Document("id", id);
            collection.deleteOne(query);
            clearFields();
        });

        // Creating grid pane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Adding components to the grid pane
        gridPane.add(idLabel, 0, 0);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(ageLabel, 0, 2);
        gridPane.add(cityLabel, 0, 3);

        gridPane.add(idField, 1, 0);
        gridPane.add(nameField, 1, 1);
        gridPane.add(ageField, 1, 2);
        gridPane.add(cityField, 1, 3);

        gridPane.add(addButton, 0, 4);
        gridPane.add(readButton, 1, 4);
        gridPane.add(updateButton, 0, 5);
        gridPane.add(deleteButton, 1, 5);

        // Creating scene
        Scene scene = new Scene(gridPane, 300, 300);

        // Setting stage title and scene
        primaryStage.setTitle("MongoDB CRUD Operations");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Close MongoDB connection
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void clearFields() {
        // Clear all text fields
        idField.clear();
        nameField.clear();
        ageField.clear();
        cityField.clear();
    }
}
