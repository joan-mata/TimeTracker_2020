package TimeTracker;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDateTime;
//import java.lang.Object

//Esta clase se encargará de acceder a la hora del dispositivo e ir actualizando a sus observadores
class Rellotge extends Thread {
    //Reloj observable = new Reloj(); //no se para que sirve pero da error
    private int r_periodo;
    //private Timer timer;
    private static Rellotge uniqueInstance;
    private boolean r_ejecutar;
    private Intervalo r_intervalo; //Intervalo al que pertenece

    //Constructor donde inicializamos el periodo mediante el cual irá actualizando la hora a los observadores.
    private Rellotge() {
        this.r_periodo = 2;
        this.uniqueInstance = null;
        this.r_ejecutar = true;

    }
      
    //Método que devuelve la instancia del reloj para asegurar que solo hay un reloj corriendo para todos los observadores.
    public static Rellotge r_getInstance() {
        if(uniqueInstance == null) {
            return new Rellotge();
        }
        else {
            return uniqueInstance;
        }
    }
      
    //Función que actualiza la hora y notifica a los observadores.
    public void r_start(Intervalo i) {
        r_intervalo = new Intervalo(i);
        this.run();
        System.out.println("Salimos start");

    }
    
    public void r_stop(){
        System.out.println("Entramos Stop");
        this.r_ejecutar = false;
    }
    public void Notify(){
        LocalDateTime hora = LocalDateTime.now(); //Guarda la hora actual del sistema.
        setChanged();
        notifyObservers(hora); //Notifica a los observadores y les envia el objeto del reloj.
    }
    
    @Override
    public void run(){
        while(!Thread.interrupted() && r_ejecutar){
            try{
                Thread.sleep(1000 * r_periodo);
                LocalDateTime time = LocalDateTime.now();
                this.r_intervalo.i_cambiar_tiempos(time);
                this.r_intervalo.i_mostrar();
                Notify(); //Función a llamar cada 2" mediante scheduleAtFixedRate.
                
                /*
                runOnUiThread(new Runnable()
                    {
                    @Override
                    public void run(){
                        
                    }
                });*/
                
            }
            catch (InterruptedException e){
                e.printStackTrace();
                //r_ejecutar = false;
            }
        }
    }

}
