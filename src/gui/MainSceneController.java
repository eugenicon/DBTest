package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import main.Main;
import sql.SqlManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

public class MainSceneController implements Initializable {
	
	@FXML
	ChoiceBox<String> databaseList;
	
	@FXML
	TextField serverField;
	
	@FXML
	TextField userField;
	
	@FXML
	TextField passwordField;
	
	@FXML
	ListView<String> tableList;
	
	@FXML
	TableView<?> tableContentView;
			
	ObservableList<String> databases;
	
	ObservableList<String> tables;
	
	@SuppressWarnings("rawtypes")
	ObservableList tableContent;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		databases = FXCollections.observableArrayList();
		databaseList.setItems(databases);
		
		databaseList.getSelectionModel().selectedIndexProperty().addListener(
				(selectedIndex, n2, n3) -> {

					updateTables(databases.get(selectedIndex.getValue().intValue()));
					
		});
		
		tables = FXCollections.observableArrayList();
		tableList.setItems(tables);
		
		serverField.setText(SqlManager.getServer());
		userField.setText(SqlManager.getUser());
		passwordField.setText(SqlManager.getPassword());
		
		tableContent = FXCollections.observableArrayList();
		tableContentView.setItems(tableContent);
	}
	
	@FXML
	private void beforeDbSelection(MouseEvent event){
		
		showIfDisconnected();
				
		if (databases.isEmpty()) {
			
			SqlManager.connect();
			SqlManager.getDatabaseList(databases);
			
			Platform.runLater(() -> databaseList.show());
				
		}
		
	}
	
	@FXML
	private void afterDbSelection(MouseEvent event){
		
		updateTables(databaseList.getSelectionModel().getSelectedItem());
			
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void afterTableSelection(MouseEvent event){
		
		if (event.getClickCount() == 2) {
			
			tableContent.clear();
			tableContentView.getColumns().clear();
			
			List<Map<String, String>> meta = SqlManager.getTableMetadata(
					tableList.getSelectionModel().getSelectedItem());
			
			for (Map<String, String> map : meta) {
				TableColumn column = new TableColumn(map.get("COLUMN_NAME"));
				column.setId(map.get("COLUMN_NAME"));
				
				/*column.setCellValueFactory(parameter -> {
					
					CellDataFeatures<Map, String> value = (CellDataFeatures<Map, String>)parameter;
					
					//System.out.println(  );
					
					
					return value.getValue().get(column.getId());
						
				});*/
				
				column.setCellValueFactory(new PropertyValueFactory(column.getId()){

					@Override
					public ObservableValue call(CellDataFeatures param) {
						//System.out.println(param.getValue());
						return new ObservableValue() {

							@Override
							public void addListener(InvalidationListener listener) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void removeListener(InvalidationListener listener) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void addListener(ChangeListener listener) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public Object getValue() {
								return ((HashMap<String, ?>)param.getValue()).get(column.getId());
							}

							@Override
							public void removeListener(ChangeListener listener) {
								// TODO Auto-generated method stub
								
							}
							
							
							
						};
					}
					
				});
				
				tableContentView.getColumns().add(column);
				
				System.out.println(map);
			}
			
			SqlManager.getTableData(
					tableList.getSelectionModel().getSelectedItem(), 
					(List<Map<String, String>>) tableContent);
			
			
			
		}
				
	}
	
	@FXML
	private void onServerChanged(KeyEvent event){
				
		SqlManager.setServer( serverField.getText() );
		showIfDisconnected();
		
	}
	
	@FXML
	private void onUserChanged(KeyEvent event){
				
		SqlManager.setUser( userField.getText() );
		showIfDisconnected();
		
	}
	
	@FXML
	private void onPasswordChanged(KeyEvent event){
				
		SqlManager.setPassword( passwordField.getText() );
		showIfDisconnected();
		
	}
	
	@FXML
	private void onTableScroll(ScrollEvent event){
		System.out.println(event);
	}
	
	
	private void showIfDisconnected(){
		
		if (!SqlManager.isConnected()) {
			databases.clear();
			databaseList.getSelectionModel().clearSelection();
		}
		
	}
	
	private void updateTables(String db){
		
		if (db != null && !db.isEmpty()) {
			
			if (!db.equals(SqlManager.getDatabase())) {
				tables.clear();
				SqlManager.setDatabase(db);
				SqlManager.getTableList(tables);
			}
		}
		
	}
	
	@FXML
	private void onDbAdd(ActionEvent event){

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("../res/EditBox.fxml"));
			Scene sc = new Scene(loader.load());
			EditBoxController controller = loader.getController();
			System.out.println(controller);
			Stage editWindow = new Stage();
			editWindow.setScene(sc);
			editWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
