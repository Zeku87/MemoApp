package com.hercudev.zeku.memo;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * Esta clase realiza la serialización y deserialización
 *
 * Definimos una variable String fileName con el nombre del fichero donde se almecenan los datos
 * Y una variable de tipo Context que contendrá los datos
 *
 * Created by zeku on 11/04/17.
 */
public class JSONSerialization {

    private String fileName;
    private Context context;

    private final String TAG = JSONSerialization.class.getSimpleName();

    public JSONSerialization(String fileName, Context context)
    {
        this.fileName = fileName;
        this.context = context;
    }

    /*
    * Aquí es donde la serialización tiene lugar
    * Escribimos en disco de forma privada los datos serializados
    * */
    public void save(List<Note> notes) throws IOException, JSONException{

        JSONArray jArray = new JSONArray();

        for(Note n : notes){
            jArray.put(n.convertToJSON());
        }

        Writer writer = null;
        try{
            OutputStream out = context.openFileOutput(fileName, context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        }
        catch (IOException e){
            Log.d(TAG, "Excepción IOException, operación de serialización fallida\n Causa: " + e.getMessage());
        }
        finally {
            if(writer != null)
                writer.close();
        }

    }

    /*
    * Aquí deserializamos los datos
    * Extraemos, leemos y devolvemos un ArrayList de Note
    *
    * Usamos StringBuilder para concatenar porque es más eficiente que
    * sobrecargar el operador + usando la clase String
    */
    public ArrayList<Note> load() throws IOException, JSONException{

        ArrayList<Note> notes = new ArrayList<>();
        BufferedReader reader = null;

        try{
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jString = new StringBuilder();
            String line = null;

            while( (line = reader.readLine()) != null)
                jString.append(line);

            JSONArray jArray = (JSONArray) new JSONTokener(jString.toString()).nextValue();
            for(int i = 0; i < jArray.length(); i++)
            {
                notes.add(new Note(jArray.getJSONObject(i)));
            }
        }
        catch (IOException e){
            Log.d(TAG, "Excepción IOException, operación de deserialización fallida\n Causa: " + e.getMessage());
        }
        finally {
            if(reader != null)
                reader.close();
        }

        return notes;
    }
}