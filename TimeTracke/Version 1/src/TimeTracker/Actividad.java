package TimeTracker;

import java.lang.Object;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

/*Clase Actividad,
Su función es generalizar dos subclases (Proyecto y Tarea) */
public abstract class Actividad {
    private String a_nombre;
    private LocalDateTime a_LDT_fecha_inicial;   
    private LocalDateTime a_LDT_fecha_final;   
    private String a_fecha_inicial;   
    private String a_fecha_final;
    private int a_tiempo_total;
    private Proyecto a_proyecto_superior;
    
    public Actividad(String name, Proyecto p){ //constructor
        this.a_nombre = name;
        this.a_tiempo_total = 0;
        this.a_proyecto_superior = p;
    }
    
    //GETTERS
    public String get_nombre(){
        return this.a_nombre;
    }
    
    public String get_fecha_inicial(){
        return this.a_fecha_inicial;
    }
    
    public String get_fecha_final(){
        return this.a_fecha_final;
    }
    
    public int get_tiempo_total(){
        return this.a_tiempo_total;
    }

    public Proyecto get_proyecto_superior(){
        return this.a_proyecto_superior;
    }
    
    //SETTERS
    public void set_nombre(String name){
        this.a_nombre = name;
    }

    //Asignas la fecha inicial de la actividad y de sus proyectos superiores si los tuviera
    public void set_fecha_inicial(LocalDateTime start){
        if(this.a_LDT_fecha_inicial == null){
            this.a_LDT_fecha_inicial = start;
            //Le damos el formato deseado al String fecha_inicial
            a_fecha_inicial = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (a_proyecto_superior != null){
            a_proyecto_superior.set_fecha_inicial(start);
        }
    }

    //Asignas la fecha final y el tiempo total de la actividad
    //Tambien de us proyectos superiores si los tuviera
    public void set_fecha_final(LocalDateTime finish){
        a_LDT_fecha_final = finish;
        //Le damos el formato deseado al String fecha_final
        a_fecha_final = finish.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        this.a_tiempo_total = set_tiempo_total();

        //Actualizamos el tiempo final y total del proyecto superior
        if(a_proyecto_superior != null){
            this.a_proyecto_superior.set_fecha_final(finish);
        }
    }

    //Calcula el teimpo total, cada subtarea a su manera
    abstract public int set_tiempo_total();
   
    //muestra por pantalla los datos de la actividad
    public void a_mostrar(){
        System.out.printf("\n%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", "Actividad:", a_nombre, a_fecha_inicial, "", a_fecha_final, "", a_tiempo_total);
        if(a_proyecto_superior != null){
            a_proyecto_superior.a_mostrar();
        }
    }
    
        
}