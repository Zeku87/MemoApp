package com.hercudev.zeku.memo;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Para almacenar las preferencias del usuario usaremos SharedPreferences
 * Permite almacenar con un formato clave-valor las distintas opciones
 * Requiere de un objeto Editor para gestionar el almacenamiento
 * El objeto SharedPreferences será accesible por cualquier clase de esta app
 * y privado para el resto de apps del dispositivo
 */

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private boolean sound;

    public static final int FAST = 0, SLOW = 1, NONE = 2;

    private int animationSpeed;

    CheckBox checkBoxSound;
    RadioGroup radioGroupAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializamos ambos objetos: prefs que es donde almcenamos la estructura clave valor
        //necesita un nombre y un modo( privado: no accesible para otras apps)
        //el objeto editor es el encagargado de salvar la estructura clave valor dentro de prefs
        prefs = getSharedPreferences("MEMO", MODE_PRIVATE);
        editor = prefs.edit();

        checkBoxSound = (CheckBox) this.findViewById(R.id.checkBox_sound);
        radioGroupAnimation = (RadioGroup) this.findViewById(R.id.radioGroup_animation);

        // Antes de salvar datos en el objeto SharedPreferences, debemos averiguar
        // si hay datos ya almacenado en el mismo y cargarlos. Lo haremos en un método privado de clase:
        loadSharedPreferences();


        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

             //El parámetro b almacena el nuevo valor que el usuario le dio al CheckBox
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sound = b;
                editor.putBoolean("sound", sound);
            }
        });


        radioGroupAnimation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             * Al producirse el evento, se almacena la referencia del RadioGroup donde se produjo y
             * la referencia(int i) del RadioButton concreto que produjo el evento
             * Mediante el switch desvelamos que radio button eligió el usuario
             * Damos el valor adecuado a animationSpeed y actualizamos el objeto Editor
             */
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButtonSpeedAnimation = (RadioButton) radioGroup.findViewById(i);

                if(radioButtonSpeedAnimation != null && i > 1){
                    switch (radioButtonSpeedAnimation.getId())
                    {
                        case R.id.radioButton_fast: animationSpeed = FAST; break;
                        case R.id.radioButton_slow: animationSpeed = SLOW; break;
                        case R.id.radioButton_none: animationSpeed = NONE; break;
                        default: animationSpeed = NONE;
                    }
                }

                editor.putInt("animationSpeed", animationSpeed);
            }
        });

    }

    /**
     * El objeto editor salvará los datos en el objeto prefs
     * en cuanto el ciclo de la actividad entre en este estado
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        editor.commit();
    }

    /**
     * Carga de preferencias
     *
     * Primero cargamos el sonido. Como el valor que sostiene es de tipo booleano
     * entonces se usa el método getBoolean. La clave que sostiene el valor es "sound"
     * y por defecto se devuelve el valor false. Por último asignamos el valor al checkbox
     *
     * Segundo cargamos la velocidad de animación elegida. Como ésta es almacenada en forma de entero
     * el valor que sostiene la clave es un entero y por tanto haremos uso del método getInt
     * Por defecto se asigna el valor NONE(igual a 2). Por último se activa el RadioButton correspondiente
     *
     * Se establece un valor por defecto en caso de que la clave no encuentre valor asociado.
     */
    private void loadSharedPreferences() {

        sound = prefs.getBoolean("sound", false);
        checkBoxSound.setChecked(sound);

        animationSpeed = prefs.getInt("animationSpeed", NONE);
        radioGroupAnimation.clearCheck(); // Elimina cualquier selección
        switch (animationSpeed){
            case FAST: radioGroupAnimation.check(R.id.radioButton_fast); break;
            case SLOW: radioGroupAnimation.check(R.id.radioButton_slow); break;
            case NONE: radioGroupAnimation.check(R.id.radioButton_none); break;
        }

    }

    /**
     * En esta actividad se muestra una flecha en la parte superior izquierda
     * que permite navegar a la actividad padre.
     * Para conseguir esto es necesario seguir los pasos que se detallan en
     * https://developer.android.com/training/implementing-navigation/ancestral.html
     *
     * Tras haber seguido los pasos, si están usando AppCompatActivity v7 necesitarán reemplazar
     * getActionBar().setDisplayHomeAsUpEnabled(true);
     * por
     * getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     * Más información: https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html#getSupportActionBar()
     */
     @Override
     public boolean onOptionsItemSelected(MenuItem item)
     {
     if(item.getItemId() == android.R.id.home)
    {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    return super.onOptionsItemSelected(item);
}
}
