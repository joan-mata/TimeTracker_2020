package TimeTracker;

import java.util.Scanner;
import java.lang.Object;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.util.Observable;
import java.util.Observer;

/*Clase Intervalo, tratada como observer
Trata de dividir una tarea en los lapsos de tiempos utilizados para finalizarla*/
public class Intervalo implements Observer{
    private LocalDateTime i_LDT_fecha_inicial;
    private LocalDateTime i_LDT_fecha_final;
    private String i_fecha_inicial;
    private String i_fecha_final;
    private int i_tiempo_total;
    private Tarea i_tarea_superior;
    
    public Intervalo(Tarea t, LocalDateTime start){
        this.i_tarea_superior = t;
        this.i_LDT_fecha_inicial = start; 
        this.i_fecha_inicial = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(start + " " + LocalDateTime.now());
    }
    
    //GETTERS
    public String i_get_fecha_inicial(){
        return this.i_fecha_inicial;
    }
    
    public String i_get_fecha_final(){
        return this.i_fecha_final;
    }
    
    public Tarea i_get_tarea_superior(){
        return this.i_tarea_superior;
    }
    
    public int i_get_tiempo_total(){
        return this.i_tiempo_total;
    }

    //SETTERS
    public void i_set_fecha_inicial(LocalDateTime start){
        if(this.i_LDT_fecha_inicial == null){
            this.i_LDT_fecha_inicial = start;
            this.i_fecha_inicial = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
    
    //Asigna la fecha final del intervalo y calcula el teimpo total
    public void i_set_fecha_final(LocalDateTime finish){
        i_LDT_fecha_final = finish;
        i_fecha_final = finish.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //Calcular tiempo total
        int i_segundos_inicial = i_LDT_fecha_inicial.getSecond();
        LocalDateTime total = i_LDT_fecha_final.minusSeconds(i_segundos_inicial);
        this.i_tiempo_total = total.getSecond();

        this.i_tarea_superior.set_fecha_final(finish);

    }

    //muestra las variables del intervalo
    public void i_mostrar(){
        System.out.printf("\n%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", "Interval:", "", i_fecha_inicial, "", i_fecha_final, "", i_tiempo_total);
        i_tarea_superior.a_mostrar();
    }
    
    //Sobreescribe los datos para que los pueda ver el observer
    @Override
    public void update(Observable o, Object arg) { 
        this.i_set_fecha_final((LocalDateTime) arg);
        this.i_mostrar();
    }
}
