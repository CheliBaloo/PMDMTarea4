package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//Clase para crear animación de fuego (o algo similar) con Canvas

public class FireView extends View {
    Paint p; //Pincel
    Path flamePath; //Patrón
    int speedX = 0; //Velocidad de dibujo en eje X
    int speedY = 0; //Velocidad de dibujo en eje Y

    //Constructor de la vista
    public FireView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        flamePath = new Path();
    }

    //Metodo que dibuja la vista
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(speedX<80){ //control de velocidad
            speedX+=1;
            speedY +=3;
        }
        drawFire(canvas);

        invalidate(); //Metodo para redibujar la vista
    }

    //Metodo que crea y dibuja el patrón
    private void drawFire(Canvas canvas){
        p.setColor(Color.RED); //ponemos color al pincel

        //Colocamos el inicio de la llama lo más cercano a laa boca de Spyro
        float startX = getWidth()*0.22f; //aprox 22% del ejeX y 20% del ejeY
        float startY = getHeight()*0.20f;
        //dibujamos la "llama"
        flamePath.reset();
        flamePath.moveTo(startX, startY);
        flamePath.lineTo(startX+speedX, startY+speedY);
        flamePath.lineTo(startX-speedX, startY+speedY);
        canvas.drawPath(flamePath, p);
    }
}
