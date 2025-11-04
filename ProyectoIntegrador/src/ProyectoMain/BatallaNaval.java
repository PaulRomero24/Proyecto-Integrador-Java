package ProyectoMain;



import Proyecto.Jugadores;
import Proyecto.Tablero;
import Utilidades.Ranking;
import Utilidades.Utilidades;
import java.util.Scanner;

public class BatallaNaval {
    private static final Ranking ranking = new Ranking();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (scanner) {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero();
                switch (opcion) {
                    case 1 -> jugar();
                    case 2 -> ranking.mostrarTop10();
                    case 3 -> System.out.println("Gracias por jugar.");
                    default -> System.out.println("Opción inválida.");
                }
            } while (opcion != 3);
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Jugar");
        System.out.println("2. Ver Ranking");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void jugar() {
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Jugador";

        int puntaje = 125;
        int disparosRestantes = 25;

        Tablero tablero = new Tablero();
        tablero.colocarBarco();

        // Bucle de juego
        while (!tablero.juegoFinalizado() && disparosRestantes > 0) {
            System.out.println();
            tablero.mostrarVistaJugador();
            System.out.println("\nDisparos restantes: " + disparosRestantes);
            System.out.println("Puntaje actual: " + puntaje);
            System.out.print("\nIngrese coordenada (ej: A5): ");

            String input = scanner.nextLine();
            int[] coords = Utilidades.parsearCoordenada(input);

            if (coords == null) {
                System.out.println("\nFormato incorrecto. Use letra y número (ej: A5)");
                continue;
            }

            int fila = coords[0];
            int columna = coords[1];

            boolean disparoValido = tablero.disparar(fila, columna);
            if (!disparoValido) {
                System.out.println("\nYa disparaste ahí o coordenada inválida. Elige otra.");
                continue;
            }

            // Actualizar puntaje y disparos
            char resultado = tablero.getVistaJugador()[fila][columna];
            if (resultado == 'O') {
                puntaje -= 5;
                System.out.println("\n¡Agua! (-5 puntos)");
            } else if (resultado == 'X' || resultado == '#') {
                // Tocado o hundido: no se resta puntaje (solo se suma en algunos juegos, pero en tu lógica no)
                // En tu pseudocódigo, no se suma por tocar, solo se resta por agua
                System.out.println("\n¡Tocado o hundido!");
            }

            disparosRestantes--;
        }

        // Mostrar resultado final
        mostrarResultadoFinal(tablero, puntaje, disparosRestantes);

        // Guardar en ranking
        ranking.agregarJugador(new Jugadores(nombre, puntaje));
    }

    private static void mostrarResultadoFinal(Tablero tablero, int puntaje, int disparosRestantes) {
        boolean gano = tablero.juegoFinalizado();
        System.out.println();
        System.out.println("==========================================");
        System.out.println("           JUEGO FINALIZADO");
        System.out.println("==========================================");

        if (gano) {
            System.out.println("\n¡¡¡FELICITACIONES!!! ¡GANASTE!");
            System.out.println("Hundiste todos los barcos enemigos.");
            System.out.println("\n=== TU TABLERO DE DISPAROS ===");
            tablero.mostrarVistaJugador();
        } else {
            System.out.println("\n¡PERDISTE! Te quedaste sin disparos.");
            System.out.println("No lograste hundir todos los barcos enemigos.");
            tablero.mostrarTableroOriginal(); // muestra ubicación real de los barcos
            System.out.println("=== TUS DISPAROS ===");
            tablero.mostrarVistaJugador();
        }

        System.out.println("\nPuntaje final: " + puntaje + " puntos");
        System.out.println("Disparos utilizados: " + (25 - disparosRestantes) + " de 25");
        System.out.println("==========================================\n");
    }
}