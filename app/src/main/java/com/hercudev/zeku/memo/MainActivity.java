package com.hercudev.zeku.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/*
 * En esta actividad se presenta el menú en el action bar como pequeños iconos
 *
 * Y se presenta una lista ListView para acceder a las notas creadas por el usuario
 *
 * Veamos como BaseAdapter sirve como molde para gestionar el ListView
 *
 * Ya que los DialogFragment y los Fragment en general son trozos de aplicación que pueden ser
 * puestos en una actividad concreta, podemos acceder a ellos mediante getFragmentManager()
 *
 * Las etiquetas TAG, sirven para rescatar el estado de los fragmentos mediante el método
 * getFragmentByTag(), haremos uso de ello más tarde
 *
 * Created by Zeku on 13/01/17.
*/
public class MainActivity extends AppCompatActivity {

    //final String TAG_MAIN = MainActivity.class.getSimpleName();
    final String TAG_NEW_NOTE_DIALOG = DialogNewNote.class.getSimpleName();
    final String TAG_SHOW_NOTE_DIALOG = DialogShowNote.class.getSimpleName();

    InspectNotesAdapter inspectNotesAdapter;

    SharedPreferences prefs;
    boolean sound;
    int animationSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inspectNotesAdapter = new InspectNotesAdapter();

        ListView noteListView = (ListView) this.findViewById(R.id.listViewNotes);
        noteListView.setAdapter(inspectNotesAdapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note n = inspectNotesAdapter.getItem(i);

                DialogShowNote dialogShowNote = new DialogShowNote();
                dialogShowNote.sendNoteSelected(n);

                dialogShowNote.show(getFragmentManager(), TAG_SHOW_NOTE_DIALOG);
            }
        });

    }

    /**
     * En este método cargamos las preferencias si las hubiese
     * porque este método es el punto de entrada no solo tras ejecutarse onStart()
     * sino también onPause()
     * Para recordar el ciclo de vida puedes consultar la app que hicimos previamente
     * https://github.com/Zeku87/lifecycleandroid
     */
    @Override
    protected void onResume(){
        super.onResume();

        //Debemos iniazilizar el objeto con la misma clave o nombre
        prefs = getSharedPreferences("MEMO", MODE_PRIVATE);

        sound = prefs.getBoolean("sound", false);
        animationSpeed = prefs.getInt("animationSpeed", SettingsActivity.NONE);
    }

    /**
     * Recordemos que a continuación del estado onPause la vida de la app está en duda:
     *  1.- el proceso podría ser destruído por cuestiones de gestión de la memoria
     *  2.- puede entrar en el estado onStop (aquí la interfaz de usuario ya no es visible)
     *  3.- o bien podría resucitar entrando en el estado onResume
     *
     * Debido a estas razones, es un buen momento para salvar los datos: las notas
     *
     */
    @Override
    protected void onPause(){
        super.onPause();

        inspectNotesAdapter.saveNotes();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_add_item)
        {
            DialogNewNote dialogNewNote = new DialogNewNote();
            dialogNewNote.show(getFragmentManager(), TAG_NEW_NOTE_DIALOG);
            return true;
        }

        if(item.getItemId() == R.id.action_settings_item)
        {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    ///// MÉTODOS PROPIOS //////

    /**
     * Pasamos un objeto Note creado por el usuario.
     * Desde DialogFragment se invoca a este método cuya misión es invocar a su vez al método
     * addNote de la clase InspectNotesAdapter para añadir al ListView el objeto Note como elemento.
     * @param note de tipo Note
     */
    public void createNewNote(Note note)
    {
        inspectNotesAdapter.addNote(note);
    }

    ///// CLASES INTERNAS //////
    /**
     * Esta clase sirve para la gestión de cada uno de los elementos que se muestran
     * en el componente gráfico ListView.
     *
     * Heredamos de BaseAdapter, clase abstracta que nos provee de ciertos métodos abstractos
     * mediante los cuáles gestionaremos dichos elementos de la lista
     *
     * Definimos primero un tipo ArrayList donde se guardan logicamente los elementos
     * y declaramos un objeto de la clase JSONSerialization para guardar/cargar notas
     *
     *
     * InspectNotesAdapter es el constructor de la clase y contacta con la clase JSONSerialization
     * para cargar las notas existentes en el objeto List<Note> noteList. De lo contrario lo deja vacío.
     *
     * saveNotes contiene la invocación al método save de la clase JSONSerialization que serializa y almacena
     * los objetos de tipo Note contenidos en el objeto List<Note> noteList
     *
     * getCount nos devuelve el tamaño del tipo ArrayList.
     *
     * geItem nos devuelve el elemento de la posición i del ArrayList
     *
     * getItemId, en este caso nos devuelve tan solo la posición i. No haremos nada especial con
     * este método, sin embargo, es obligatorio definirlo porque es abstracto.
     *
     * getView, en este método tomamos los elementos del ArrayList y los mostramos como
     * elementos del ListView al usuario.
     *
     * Si queremos modificar algún elemento del ArrayList y reflejarlo en el ListView, haremos uso
     * del método notifyDataSetChanged() para registrar la modificación y redibujar por completo el ListView.
     */

    public class InspectNotesAdapter extends BaseAdapter{

        List<Note> noteList = new ArrayList<>();

        private JSONSerialization jSerializer;

        private final String TAG = InspectNotesAdapter.class.getSimpleName();

        public InspectNotesAdapter(){
            jSerializer = new JSONSerialization("memoapp.json", MainActivity.this.getApplicationContext());

            try{
                noteList = jSerializer.load();
            }catch (Exception e)
            {
                noteList = new ArrayList<>();
                Log.d(TAG, "Error al cargar las notas: " + e.getMessage());
            }
        }

        public void saveNotes(){
            try{
                jSerializer.save(noteList);
            }catch (Exception e)
            {
                Log.d(TAG, "Error al salvar las notas: " + e.getMessage());
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        //Por defecto devuelve Object, así que lo cambiamos para que devuelva Note (el tipo que vamos a usar)
        @Override
        public Note getItem(int i) {
            return noteList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /*
         * View se encarga de almacenar la referencia de los elementos de la lista visibles para el usuario
         * Cuando el usuario hace scroll, cada elemento que desaparece se sustituye por el que se hace visible
         * ahorrandose así el tener que crear un objeto View cada vez.
         *
         * Si view es igual a null significa que es la primera vez que se cargan los elementos de la lista o bien
         * que no se ha podido "reciclar" algún elemento. Por tanto tendremos que inflar el elemento.
         *
         * Recuerda que inflar significa almacenar la referencia de un layout en un objeto de tipo View.
         * Ahora view contiene la referencia del elemento de la lista
         * Nuestro elementos se componen específicamente de 3 ImageView y 2 TextView cada uno.
         *
         * ViewGroup es el padre de las vistas creadas por getView, es decir, la referencia a ListView
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_note, viewGroup, false);
            }

            ImageView ideaImageView = (ImageView) view.findViewById(R.id.imageViewIdea);
            ImageView importantImageView = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView todoImageView = (ImageView) view.findViewById(R.id.imageViewTodo);

            TextView titleTextView = (TextView) view.findViewById(R.id.textView_show_title);
            TextView descriptionTextView = (TextView) view.findViewById(R.id.textView_show_description);

            Note note = getItem(i);

            if(!note.isIdea()) ideaImageView.setVisibility(View.GONE);
            if(!note.isImportant()) importantImageView.setVisibility(View.GONE);
            if(!note.isTodo()) todoImageView.setVisibility(View.GONE);

            if(note.isIdea()) ideaImageView.setVisibility(View.VISIBLE);
            if(note.isImportant()) importantImageView.setVisibility(View.VISIBLE);
            if(note.isTodo()) todoImageView.setVisibility(View.VISIBLE);

            titleTextView.setText(note.getTitle());
            descriptionTextView.setText(note.getDescription());

            return view;
        }

        public void addNote(Note note)
        {
            noteList.add(note);
            notifyDataSetChanged();
        }
    }

}
