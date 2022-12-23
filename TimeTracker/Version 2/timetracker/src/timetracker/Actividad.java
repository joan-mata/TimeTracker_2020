package TimeTracker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*Clase Actividad,
Su función es generalizar dos subclases (Proyecto y Tarea) */
public abstract class Actividad {
  private String actNombre;
  private LocalDateTime actLdtFechaInicial;
  private LocalDateTime actLdtFechaFinal;
  private String actFechaInicial;
  private String actFechaFinal;
  private int actTiempoTotal;
  private Proyecto actProyectoSuperior;
  private String actClase;
  private int ttTiempoTotal;
  private int id;
  private UniqueId actUniqueId;
  
  private static final Logger logger = LoggerFactory.getLogger(Actividad.class);

  public Actividad(String name, Proyecto p, String clase) {
    this.actNombre = name;
    this.actProyectoSuperior = p;
    //un valor muy pequeño para trabajar con él cuanod esta sin "inicializar"
    this.actTiempoTotal = -100; 
    this.actClase = clase;
    this.ttTiempoTotal = 0;
    this.id = actUniqueId.uniqueGetInstance().getUniqueId();
    assert actInvariant() : "Invariante";
  }

  protected boolean actInvariant() {
    return actTiempoTotal == -100 || (actTiempoTotal > 0 && actTiempoTotal % 2 == 0);
  }
  
  public String getNombre() {
    assert actInvariant() : "Invariante";
    return this.actNombre;
  }

  public int getId(){
    assert actInvariant() : "Invariante";
    return this.id;
  }

  public LocalDateTime getLdtFechaInicial() {
    assert actInvariant() : "Invariante";
    return this.actLdtFechaInicial;
  }

  public String getFechaInicial() {
    assert actInvariant() : "Invariante";
    return this.actFechaInicial;
  }

  public String getFechaFinal() {
    assert actInvariant() : "Invariante";
    return this.actFechaFinal;
  }

  public int getTiempoTotal() {
    assert actInvariant() : "Invariante";
    return this.actTiempoTotal;
  }

  public Proyecto getProyectoSuperior() {
    assert actInvariant() : "Invariante";
    return this.actProyectoSuperior;
  }

  public int getTtTiempoTotal() {
    return this.ttTiempoTotal;
  }
  
  //Calcula el tiempo total, pero con las delimitaciones del intervalo de tiempo.
  public abstract int getTtTotalTime(LocalDateTime ini, LocalDateTime fin);

  //Asignas la fecha inicial de la actividad y de sus proyectos superiores si los tuviera.
  public void setFechaInicial(LocalDateTime start) {
    assert actInvariant() : "Invariante";

    logger.trace("Estoy en el método setFechaInicial de la clase Actividad.");
    
    if (this.actLdtFechaInicial == null) {
      this.actLdtFechaInicial = start;
      //Le damos el formato deseado al String fecha_inicial.
      actFechaInicial = actLdtFechaInicial.format(
                          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    if (actProyectoSuperior != null) {
      actProyectoSuperior.setFechaInicial(actLdtFechaInicial);
    }
    
    logger.debug("Fecha inicial asignada. (f.i){}", actFechaInicial);

    assert actInvariant() : "Invariante";
  }

  /*Asigna la fecha final y el tiempo total de la actividad
  Tambien de sus proyectos superiores si los tuviera*/
  public void setFechaFinal(LocalDateTime finish) {
    assert actInvariant() : "Invariante";

    
    logger.trace("Estoy en el método setFechaFinal de la clase Actividad.");

    assert (finish.isAfter(actLdtFechaInicial)) : "El tiempo final es inferior al tiempo inicial.";

    actLdtFechaFinal = finish;
    actFechaFinal = actLdtFechaFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    logger.debug("Fecha final asignada.");

    this.actTiempoTotal = setTiempoTotal();
    logger.debug("Tiempo total asignado. (f.i) {}", actFechaFinal);

    //Actualizamos el tiempo final y total del proyecto superior.
    if (actProyectoSuperior != null) {
      this.actProyectoSuperior.setFechaFinal(actLdtFechaFinal);
    }
    assert actInvariant() : "Invariante";
  }

  //Calcula el tiempo total, cada subtarea a su manera.
  public abstract int setTiempoTotal();

  public void sumaTtTiempoTotal(int valor) {
    this.ttTiempoTotal += valor;
  }
  

  /*Muestra por pantalla los datos de la 
  actividad y de sus actividades superiores.*/
  public void actMostrar() {
    assert actInvariant() : "Invariante";
    
    logger.trace("Estoy en el método actMostra de la clase Actividad.");

    logger.info("Interval: (n) {} (f.i) {} (f.f) {} (t.t) {}",
                actNombre, actFechaInicial, actFechaFinal, actTiempoTotal);

    if (actProyectoSuperior != null) {
      actProyectoSuperior.actMostrar();
    }

    assert actInvariant() : "Invariante";
  }

  public abstract JSONObject toJson(int depth);

  public abstract Actividad findActivityById(int id);
}

