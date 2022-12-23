package TimeTracker;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*Clase TotalTime
Calcula el tiempo total de una actividad en un intervalo de tiempo.
No está finalizada por completo.*/
public class TotalTime {
  private LocalDateTime ttLdtTiempoInicial;
  private LocalDateTime ttLdtTiempoFinal;
  private Actividad ttActividad;
  private static final Logger logger = LoggerFactory.getLogger(TotalTime.class);


  /*Constructor de la classe, debido a varias pruebas, actualmente hay 2 constructores
  cada uno pensado para un getTtTotalTime distinto.*/
  public TotalTime(Actividad root) {
    this.ttActividad = root;
    assert ttInvariant() : "Invariante";
  }

  public TotalTime(Actividad root, int ini, int fin) {
    this.ttActividad = root;
    this.ttLdtTiempoInicial = root.getLdtFechaInicial().plusSeconds(ini);
    this.ttLdtTiempoFinal = root.getLdtFechaInicial().plusSeconds(fin);
    assert ttInvariant() : "Invariante";
  }

  protected boolean ttInvariant() {
    return ttActividad.getProyectoSuperior() == null;
  }

  /*Calcula el tiempo total de ejecución de la actividad, y sus sub-actividades
  en caso de que las tuviera, sin salirse del intervalo de tiempo dado.
  Está duplicado al igual que los constructores no para el resultado final,
  sino debido a pruebas en la programación.*/
  public int getTtTotalTime(Actividad a) {
    logger.trace("Estoy en el método getTtTotalTime de la clase TotalTime");
    logger.debug("Actividad {}", a);
    assert ttInvariant() : "Invariante";
    assert (ttLdtTiempoInicial != null) : "Tiempo inicial és null";
    assert (ttLdtTiempoFinal != null) : "Tiempo final és null";
    return a.getTtTotalTime(ttLdtTiempoInicial, ttLdtTiempoFinal);
  }

  public int getTtTotalTime(Actividad a, int ini, int fin) {
    logger.trace("Estoy en el método getTtTotalTime de la clase TotalTime");
    logger.debug("Actividad {}", a);
    assert ttInvariant() : "Invariante";
    this.ttLdtTiempoInicial = ttActividad.getLdtFechaInicial().plusSeconds(ini);
    this.ttLdtTiempoFinal = ttActividad.getLdtFechaInicial().plusSeconds(fin);
    assert ttInvariant() : "Invariante";
    return a.getTtTotalTime(ttLdtTiempoInicial, ttLdtTiempoFinal);
  }

}