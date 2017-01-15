package com.hercudev.zeku.memo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Mediante esta clase inflamos y gestionamos nuestro DialogFragment
 * donde se aloja una nueva nota.
 *
 * A tener en cuenta; podríamos haber definido un objeto Dialog dentro de MainActivity.java
 * sin embargo, es recomendable heredar de la clase DialogFragment ya que es más "estable".
 *
 * Cuando inflamos un lienzo mediante un objeto de tipo LayoutInflater, lo que estamos haciendo
 * es convertir un lienzo, dialog_new_note, en un objeto de tipo View para así tener acceso a
 * todos sus widgets.
 *
 * Los widgets son definidos como final debido a que se utilizan dentro de clases anónimas
 * La referencia de los widgets, tal como R.id.editTitle, es una referencia(int)
 * almacenada en la pila(heap)
 *
 * Created by zeku87 on 13/01/17.
 */
public class DialogNewNote extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogNewNoteView = inflater.inflate(R.layout.dialog_new_note, null);
        dialogBuilder.setView(dialogNewNoteView);


        final CheckBox ideaCheckBox = (CheckBox) dialogNewNoteView.findViewById(R.id.checkBoxIdea);
        final CheckBox importantCheckBox = (CheckBox) dialogNewNoteView.findViewById(R.id.checkBoxImportant);
        final CheckBox todoCheckBox = (CheckBox) dialogNewNoteView.findViewById(R.id.checkBoxTodo);

        final EditText titleEditText = (EditText) dialogNewNoteView.findViewById(R.id.editTitle);
        final EditText descriptionEditText = (EditText) dialogNewNoteView.findViewById(R.id.editDescription);

        final Button cancelButton = (Button) dialogNewNoteView.findViewById(R.id.buttonCancel);
        final Button okayButton = (Button) dialogNewNoteView.findViewById(R.id.buttonOk);

        //dialogBuilder.setMessage(getString(R.string.dialog_new_note_title));


        okayButton.setOnClickListener(new View.OnClickListener() {

            /*
            TODO: manejo de errores. Postcondición: description no debe estar vacío
            TODO: definir createNewNote(Note) correctamente en MainActivity
             */
            @Override
            public void onClick(View view) {

                Note note = new Note();

                note.setTitle(titleEditText.getText().toString());
                note.setDescription(descriptionEditText.getText().toString());

                note.setIdea(ideaCheckBox.isChecked());
                note.setImportant(importantCheckBox.isChecked());
                note.setTodo(todoCheckBox.isChecked());

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.createNewNote(note);

                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return dialogBuilder.create();

    }
}
