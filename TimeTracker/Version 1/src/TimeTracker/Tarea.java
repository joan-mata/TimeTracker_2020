package TimeTracker;

import java.lang.Object;
import java.util.ArrayList;
import java.time.LocalDateTime;

/*Clase Tarea, extendida de Actividad
Su función es dividir una clase proyecto en partes más pequeñas, y con ello más sencillas de trabajar.
Tendrá una lista de los intervalos que se han realizado durante la tarea*/
public class Tarea extends Actividad{
    private ArrayList<Intervalo> t_lista_intervalos;
    private Reloj t_reloj;
 
    public Tarea(String name, Proyecto p){
        super(name, p);
        this.t_lista_intervalos = new ArrayList<Intervalo>();
        this.get_proyecto_superior().añadir_tarea(this);
    }

    //Conseguimos la instancia única del reloj
    public Reloj t_getInstance(){ 
        return t_reloj.getInstance();
    }

    //Conseguimos el tiempo total de tarea. al sumar los tiempos de sus intervalos
    @Override
    public int set_tiempo_total(){
        int total_time = 0;
        for(int i = 0; i < t_lista_intervalos.size(); i++){
            total_time += t_lista_intervalos.get(i).i_get_tiempo_total();
        }
        return total_time;
    }

    public void añadir_intervalo(Intervalo i) { 
        this.t_lista_intervalos.add(i);
    }

    public void eliminar_intervalo(Intervalo i){ 
        this.t_lista_intervalos.remove(i);
    }
    
    //Inicializas el intervalo que toca, nuevo en la lista y lo muestras
    public void start(){
        LocalDateTime hora = LocalDateTime.now(); //Guarda la hora actual del sistema.
        Intervalo i = new Intervalo(this, hora);
        set_fecha_inicial(hora);       
        añadir_intervalo(i);
        t_getInstance().addObserver(i);
    }
    
    //Finalizamos la actividad
    public void stop(){ 
        Intervalo i = this.t_lista_intervalos.get(this.t_lista_intervalos.size() - 1);
        t_getInstance().deleteObserver(i);
    }

}