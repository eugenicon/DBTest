package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class EditBoxController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(arg0);
	}
	
	@FXML
	private void onOK(ActionEvent event){
		closeThisWindow((Node)event.getSource());
	}
	
	@FXML
	private void onCancel(ActionEvent event){
		closeThisWindow((Node)event.getSource());
	}
	
	private Stage getThisWindow(Node node){
		return (Stage) node.getScene().getWindow();
	}
	
	private void closeThisWindow(Node node){
		Stage thisWindow = getThisWindow(node);
		thisWindow.close();
	}

}
