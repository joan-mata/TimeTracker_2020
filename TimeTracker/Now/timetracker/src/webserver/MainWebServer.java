package webserver;
import timetracker.*;

//import core.Activity;
//import core.Clock;

public class MainWebServer {
  private static Reloj Reloj;
  public static void main(String[] args) {
    webServer();
  }

  public static Actividad makeTreeCourses() {
    /*Se crean todos los proyectos y tareas que
    utilizará el test. Según Apéndice A*/
    Proyecto root = new Proyecto("Inicio", null);
    Proyecto softwareDesing = new Proyecto("Software design", root);
    Proyecto softwareTesting = new Proyecto("Software testing", root);
    Proyecto databases = new Proyecto("Databases", root);
    Proyecto problems = new Proyecto("Problems", softwareDesing);
    Proyecto projectTimeTracker = new Proyecto("Project time tracker", softwareDesing);

    Tarea transportation = new Tarea("transportation", root);
    Tarea firstList = new Tarea("first list", problems);
    Tarea secondList = new Tarea("second list", problems);
    Tarea readHandout = new Tarea("read handout", projectTimeTracker);
    Tarea firstMilestone = new Tarea("first milestone", projectTimeTracker);

    return root;
  }
  public static void webServer() {
    final Actividad root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock
    Thread time = new Thread(Reloj.getInstance());
    time.start();

    new WebServer(root);
  }
}