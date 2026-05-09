package com.scheduler.schedulercomparison.gui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class EditableCell extends TableCell<ProcessRow, String> {

    private TextField textField;

    public EditableCell() {}

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null) createTextField();
        setText(null);
        setGraphic(textField);
        textField.setText(getItem());
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            if (textField != null) textField.setText(item);
            setText(null);
            setGraphic(textField);
        } else {
            setText(item);
            setGraphic(null);
            setStyle("-fx-text-fill: #cdd6f4; -fx-alignment: CENTER;");
        }
    }

    private void createTextField() {
        textField = new TextField(getItem());
        textField.setStyle("-fx-background-color: #45475a; -fx-text-fill: #cdd6f4;");
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        textField.setOnAction(e -> commitEdit(textField.getText()));

        textField.focusedProperty().addListener((obs, wasF, isF) -> {
            if (!isF) commitEdit(textField.getText());
        });
    }
}