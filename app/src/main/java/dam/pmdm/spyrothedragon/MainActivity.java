package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.models.Collectible;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;

    NavController navController = null;
    //Variable para conteo de la pantalla de la guía por la que va
    private int guideScreen = 0;
    //Veces que se ha tocado la gema para el EaterEgg
    int gems_selected = 0;
    SharedPreferences sharedPreferences;
    //Creamos los recursos necesarios para reproducir los sonidos
    SoundPool soundPool;
    private int soundGem, soundFire,  soundSkip, soundNext, soundEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        guideBinding = binding.guideLayout;

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        //Comprobamos si es necesario mostrar la guía
        sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("needGuide", true)) {
            initializeGuide(); //iniciamos la guía
        }
        initializeSounds(); //cargamos los sonidos para que esten listos


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
    }
    //Metodo para cargar sonidos
    private void initializeSounds() {
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundGem = soundPool.load(this, R.raw.gem_sound, 1);
        soundFire = soundPool.load(this, R.raw.fire_sound, 1);
        soundSkip = soundPool.load(this, R.raw.egg_thief_sound, 1);
        soundNext = soundPool.load(this, R.raw.next_sound, 1);
        soundEnd = soundPool.load(this, R.raw.end_sound, 1);
    }
    //Metodo para iniciar guía
    private void initializeGuide() {
        //La hacemos visible
        guideBinding.guideLayout.setVisibility(View.VISIBLE);
        //Cuando pulsemos el botón de comenzar
        guideBinding.guideButton.setOnClickListener(view -> {
            startGuide();
        });
        //Si pulsamos el botón de omitir guia
        guideBinding.skipButton.setOnClickListener(view -> {
            //reproducimos sonido
            soundPool.play(soundSkip, 1f, 1f, 0, 0, 1f);
            //invisibilizamos la guía
            guideBinding.guideLayout.setVisibility(View.GONE);
            setNeedGuideData(false); //guardamos la guía como no necesaria
            //Regresamos al menú inicial
            if(navController.getCurrentDestination().getId() == R.id.navigation_worlds)
                navController.navigate(R.id.action_navigation_worlds_to_navigation_characters);
            else if(navController.getCurrentDestination().getId() == R.id.navigation_collectibles)
                navController.navigate(R.id.action_navigation_collectibles_to_navigation_characters);
        });
        //Cuando pulsamos el botón de siguiente en la guía
        guideBinding.nextButton.setOnClickListener(view -> {
            if(guideScreen<4) { //Si son los pasos de la guia
                guideScreen++;
                guideSteps();
                soundPool.play(soundNext, 1f, 1f, 0, 0, 1f);
            }else{ //si es la pantalla de finalizacion
                //mostramos la pantalla resumen
                guideBinding.guideSteps.setVisibility(View.GONE);
                guideBinding.summaryGuide.setVisibility(View.VISIBLE);
                soundPool.play(soundEnd, 1f, 1f, 0, 0, 1f);
            }
        });
        //Si pulsamos el boton de finalizar
        guideBinding.endBt.setOnClickListener(view -> {
            setNeedGuideData(false); //registramos el fin de la guia
            guideBinding.summaryGuide.setVisibility(View.GONE); //invisibilizamos la guia
            navController.navigate(R.id.action_navigation_collectibles_to_navigation_characters); //vamos al fragmento inicial (Personajes)
            guideScreen=0; //Reiniciamos la guía (Solo para pruebas)
        });

    }
    //Metodo que inicia la guía
    private void startGuide(){
        soundPool.play(soundGem, 1f, 1f, 0, 0, 1f);
        guideBinding.spyroImg.setVisibility(View.VISIBLE); //Hacemos visible la guia
        float dist = guideBinding.welcomeFrame.getWidth()+50f; //tomar distancia que debe volar la animacion
        //Anim de volar
        ObjectAnimator animFlying = ObjectAnimator.ofFloat(guideBinding.spyroImg, "translationX", -50f, dist);
        animFlying.setDuration(1000);
        //Anim de desaparecer gema
        ObjectAnimator animGem = ObjectAnimator.ofFloat(guideBinding.guideButton, "alpha", 1f, 0f);
        animGem.setDuration(500);

        //Los reproducimos juntos
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animFlying, animGem);
        animSet.start();
        //Cuando termine, cambiamos de pantalla
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                guideBinding.welcomeFrame.setVisibility(View.GONE);
                guideBinding.guideSteps.setVisibility(View.VISIBLE);
                guideScreen = 1;
                guideSteps();
            }
        });

    }
    //Metodo que controla las animaciones de los pasos de la guía
    private void guideSteps(){
        float posBefore = 0; //Posicion desde la que inicia la animacion de traslacion en el eje X
        float pos = 0; //Posicion donde finaliza
        guideBinding.imgSteps.setImageResource(R.drawable.spyro_flying);
        //Iniciamos el AnimatorSet y las animaciones comunes para todos los pasos
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator fade_text = ObjectAnimator.ofFloat(guideBinding.textSteps,"alpha", 0f, 1f );
        ObjectAnimator scaleX_circle = ObjectAnimator.ofFloat(guideBinding.tabSelector, "scaleX", 0f,2.5f);
        ObjectAnimator scaleY_circle = ObjectAnimator.ofFloat(guideBinding.tabSelector, "scaleY", 0f, 2.5f);
        if(guideScreen<4){ //Para los 3 primero pasos, tomamos las posiciones de los botones de navegación y cambiamos el texto del bocadillo. También navegamos a otro fragmento si es necesario
            if(guideScreen == 1){
                posBefore = 0;
                pos = getMenuItemPos(guideScreen);
                guideBinding.textSteps.setText(R.string.tab_char);
            }else if(guideScreen == 2){
                navController.navigate(R.id.action_navigation_characters_to_navigation_worlds);
                posBefore = getMenuItemPos(1);
                pos = getMenuItemPos(guideScreen);
                guideBinding.textSteps.setText(R.string.tab_worlds);
            }else if(guideScreen == 3) {
                navController.navigate(R.id.action_navigation_worlds_to_navigation_collectibles);
                posBefore = getMenuItemPos(2);
                pos = getMenuItemPos(2) + (getMenuItemPos(2) - getMenuItemPos(1));
                guideBinding.textSteps.setText(R.string.tab_collect);
            }
            //Creamos las animaciones específicas: movimientos en el eje X
            ObjectAnimator spyro_moveX = ObjectAnimator.ofFloat(guideBinding.imgSteps, "translationX", posBefore, pos);
            ObjectAnimator circle_moveX = ObjectAnimator.ofFloat(guideBinding.tabSelector, "translationX", posBefore-40f, pos-40f);

            //Lanzamos las animaciones
            animatorSet.playTogether(spyro_moveX, circle_moveX,scaleX_circle, scaleY_circle, fade_text);
            animatorSet.setDuration(1000);
            animatorSet.start();

        }else if(guideScreen == 4){ //En el 4 paso, la animación se moverá en el eje Y
            guideBinding.textSteps.setText(R.string.dialog_about);
            guideBinding.imgSteps.setRotation(-90);
            float posY = guideBinding.guideLayout.getHeight()-binding.toolbar.getHeight()*2; //Tomamos la posición a la que tiene que ir los marcadores, según las otras Views
            //Creamos y lanzamos animaciones
            ObjectAnimator circle_moveY = ObjectAnimator.ofFloat(guideBinding.tabSelector, "translationY", 0f, -posY-75);
            ObjectAnimator spyro_moveY = ObjectAnimator.ofFloat(guideBinding.imgSteps, "translationY", 0f, -posY+200f);
            animatorSet.setDuration(1000);
            animatorSet.playTogether(circle_moveY, spyro_moveY, fade_text, scaleX_circle, scaleY_circle);
            animatorSet.start();
        }
        //Cuando finalizan las animaciones:
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                guideBinding.imgSteps.setImageResource(R.drawable.spyro_guide);
                if(guideScreen == 4)
                    guideBinding.imgSteps.setRotation(0);
            }
        });

    }
    //Metodo para guardar la preferencia needGuide en SharedPreferences
    private void setNeedGuideData(boolean b){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("needGuide", b);
        editor.apply();
    }
    //Metodo para obtener las posiciones de los elementos del navView
    private float getMenuItemPos(int id){
        MenuItem menuItem = binding.navView.getMenu().getItem(id);
        View view = findViewById(menuItem.getItemId());
        return view.getX();
    }
    //Metodo para el control de selección de los Coleccionables, para el EasterEgg del video
    public void onCollectibleSelected(Collectible collectible, View view){
        if(collectible.getName().equals("Gemas")) //si el cardview seleccionado es el de "Gemas"
            gems_selected++; //aumentamos el contador

        if(gems_selected == 4){ //Cuando lleguemos a 4
            gems_selected = 0; //Lo reiniciamos y reproducimos el video
            binding.video.setVideoPath("android.resource://"
                    + getPackageName()+ "/"
                    + R.raw.video);
            binding.videoLayout.setVisibility(View.VISIBLE);
            binding.video.setMediaController(new MediaController(this)); //Activamos los controles de video por si quiere adelantarlo
            binding.video.start();

            //Cuando finalice, volvemos a la pantalla de coleccionables
            binding.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    binding.videoLayout.setVisibility(View.GONE);
                }
            });
        }
        //¡¡SOLO PARA PRUEBAS, PARA PODER REACTIVAR LA GUÍA AL PULSAR SOBRE LA ULTIMA OPCION DE LOS COLECCIONABLES!! Comentar para tener la versión final
        if(collectible.getName().equals("Ladrón de Huevos"))
            setNeedGuideData(true);
    }
    //Metodo de control de pulsación prolongada sobre los personajes, para EasterEgg del fuego
    public boolean onCharacterSelected(Character character, View view){
        //Si el personaje seleccionado es Spyro
        if(character.getName().equals("Spyro")){
            soundPool.play(soundFire, 1f, 1f, 0, 0, 1f); //Sonido de fuego
            //añadimos la animacion de fuego al layout
            FireView fireView = new FireView(this, null);
            binding.rootLayout.addView(fireView);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() { //Esperamos 1 segundo y la quitamos
                @Override
                public void run() {
                    binding.rootLayout.removeView(fireView);
                }
            }, 1000);
        }
        return true;
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters){
            if(navController.getCurrentDestination().getId() == R.id.navigation_worlds){
                navController.navigate(R.id.action_navigation_worlds_to_navigation_characters);
            }else if(navController.getCurrentDestination().getId() == R.id.navigation_collectibles){
                navController.navigate(R.id.action_navigation_collectibles_to_navigation_characters);
                gems_selected = 0; //reiniciamos el contador de toques
            }

        } else if (menuItem.getItemId() == R.id.nav_worlds) {
            if(navController.getCurrentDestination().getId() == R.id.navigation_characters){
                navController.navigate(R.id.action_navigation_characters_to_navigation_worlds);
            }else if(navController.getCurrentDestination().getId() == R.id.navigation_collectibles){
                navController.navigate(R.id.action_navigation_collectibles_to_navigation_worlds);
                gems_selected = 0; //reiniciamos el contador de toques
            }
        }else{
            if(navController.getCurrentDestination().getId() == R.id.navigation_worlds){
                navController.navigate(R.id.action_navigation_worlds_to_navigation_collectibles);
            }else if(navController.getCurrentDestination().getId() == R.id.navigation_characters){
                navController.navigate(R.id.action_navigation_characters_to_navigation_collectibles);
            }
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }


}