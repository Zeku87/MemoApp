package com.hercudev.zeku.memo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Aquí inflamos y gestionamos el diálogo que muestra una nota determinada
 * De momento, solo presenta los datos.
 *
 * Debemos resaltar los atributos estáticos View.GONE y View.INVISIBLE y la diferencia entre ellos:
 * View.GONE: hace desaparecer el widget del layout(lienzo), además los widgets colindantes ocuparán
 * su espacio en el layout; sin embargo, View.INVISIBLE, hace desaparecer el widget respetando
 * el lugar que toma en el lienzo quedando un hueco vacío.
 *
 * Podéis experimentar para ver el efecto en el layout.
 *
 * Created by Zeku on 13/01/17.
 */
public class DialogShowNote extends DialogFragment {

    private Note note;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogShowNoteView = inflater.inflate(R.layout.dialog_show_note, null);
        dialogBuilder.setView(dialogShowNoteView);

        ImageView ideaImageView = (ImageView) dialogShowNoteView.findViewById(R.id.imageViewIdea);
        ImageView importantImageView = (ImageView) dialogShowNoteView.findViewById(R.id.imageViewImportant);
        ImageView todoImageView = (ImageView) dialogShowNoteView.findViewById(R.id.imageViewTodo);

        TextView tituloTextView = (TextView) dialogShowNoteView.findViewById(R.id.textView_show_title);
        TextView descriptionTextView = (TextView) dialogShowNoteView.findViewById(R.id.textView_show_description);

        Button okayButton = (Button) dialogShowNoteView.findViewById(R.id.buttonOk);
        Button cancel = (Button) dialogShowNoteView.findViewById(R.id.buttonCancel);
        try {
            if (!note.isIdea())
                ideaImageView.setVisibility(View.GONE);

            if (!note.isImportant())
                importantImageView.setVisibility(View.GONE);

            if (!note.isTodo())
                todoImageView.setVisibility(View.GONE);

            tituloTextView.setText(note.getTitle());
            descriptionTextView.setText(note.getDescription());
        }catch (NullPointerException e) {
            Toast.makeText(getActivity(), "primero añade una nota", Toast.LENGTH_LONG).show();
        }
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogBuilder.create();

    }

    /**
     * En la actividad MainActivity el usuario tocará una nota y será enviada como parámetro de entrada
     * @param noteSelected de tipo Note
     */
    public void sendNoteSelected(Note noteSelected) {
        this.note = noteSelected;
    }



}
