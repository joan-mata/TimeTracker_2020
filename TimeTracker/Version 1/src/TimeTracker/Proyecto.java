package TimeTracker;

import java.lang.Object;
import java.util.ArrayList;
import java.time.LocalDateTime;

/*Clase proyecto extendidad de Actividad
Es utilizada para generalizar las partes de un trabajo o entrega.
Tiene acceso a una lista de sus proyectos inferiores, en el caso que tuviese. 
Lo mismo con las tareas que pudieran depender de él.*/
public class Proyecto extends Actividad{ 
    private ArrayList<Tarea> p_lista_tareas;
    private ArrayList<Proyecto> p_lista_proyectos;

    //CONSTRUCTORES
    public Proyecto(String name, Proyecto p){
        super (name, p);
        this.p_lista_tareas = new ArrayList<Tarea>();
        this.p_lista_proyectos = new ArrayList<Proyecto>();
        if(this.get_proyecto_superior() != null ){
            this.get_proyecto_superior().añadir_proyecto(this);
        }
    }
    

    //FUNCIONES
    public void añadir_tarea(Tarea t) {
        p_lista_tareas.add(t);
    }

    public void añadir_proyecto(Proyecto p) {
        p_lista_proyectos.add(p);
    }

    public void eliminar_tarea(Tarea t){
        p_lista_tareas.remove(t);
    }  

    public void eliminar_Proyect(Proyecto p){
        p_lista_proyectos.remove(p);
    }  

    //Calcula el tiempo total del proyecto
    @Override
    public int set_tiempo_total(){ //MAL - Demasiada memoria
        int total_time = 0;
        for(int i = 0; i < p_lista_tareas.size(); i++){
            total_time += p_lista_tareas.get(i).set_tiempo_total();
        }
        for(int i = 0; i < p_lista_proyectos.size(); i++){
            total_time += p_lista_proyectos.get(i).set_tiempo_total();
        }
        return total_time;
    }
}