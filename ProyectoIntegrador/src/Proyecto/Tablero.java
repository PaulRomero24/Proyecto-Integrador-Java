package Proyecto;

import java.util.Random;

public class Tablero {
    //VARIABLES PARA EL TABLERO
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;


    private int[][] barcos;          // 0 = agua, 1/2/3 = tipo de barco, -1 = ya tocado
    private char[][] vistaJugador;   // '?', 'X', 'O', '#'
    private int[][] copiaOriginal;   // para mostrar al final

    //UTILIDAD PARA EL ALEATORIO
    private final Random random = new Random();

    //CONSTRUCTOR
    public Tablero(){
        this.barcos = new int[FILAS][COLUMNAS];
        this.vistaJugador = new char[FILAS][COLUMNAS];
        this.copiaOriginal = new int[FILAS][COLUMNAS];
        Inicializamos();
    }
    
    //CREACION DEL TABLERO
    private void Inicializamos(){
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                barcos [i][j] = 0;
                vistaJugador[i][j] = '?';
            }
        }
    }
    //AÑADIMOS LOS BARCOS
    public void colocarBarco(){
        colocarBarco(TipoBarco.SUBMARINO);
        colocarBarco(TipoBarco.CRUCERO);
        colocarBarco(TipoBarco.DESTRUCTOR);
        copiarBarcos();
    }

    //IMPORTAMOS LOS METODOS DE LA CLASE "TIPO BARCO"
    private void colocarBarco(TipoBarco tipo){
        int tamaño = tipo.getTamaño();
        int id = tipo.toId();
        boolean colocado = false;

        //CICLOS QUE UTILIZA EL ALEATORIO PARA UBICAR LOS BARCOS,Y VERIFICA SI SE PUEDE COLOCAR
        while (!colocado) {
            int fila = random.nextInt(FILAS);
            int columna = random.nextInt(COLUMNAS);
            boolean horizontal = random.nextBoolean();
            if (horizontal && columna + tamaño <= COLUMNAS) {
                boolean libre = true;
                for (int j = 0; j < tamaño; j++) {
                    if (barcos[fila][columna + j] != 0) {
                        libre = false;
                        break;
                    }
                }
                if (libre) {
                    for (int j = 0; j < tamaño; j++) {
                        barcos[fila][columna + j] = id;
                    }
                    colocado = true;
                }
            } else if (!horizontal && fila + tamaño <= FILAS) {
                boolean libre = true;
                for (int i = 0; i < tamaño; i++) {
                    if (barcos[fila + i][columna] != 0) {
                        libre = false;
                        break;
                    }
                }
                if (libre) {
                    for (int i = 0; i < tamaño; i++) {
                        barcos[fila + i][columna] = id;
                    }
                    colocado = true;
                }
            }
        }
    }


    //METODO PARA UBICAR EL BARCO EN EL TABLERO
    private void copiarBarcos(){
        for (int i = 0; i < FILAS; i++) {
            System.arraycopy(barcos[i], 0, copiaOriginal[i], 0, COLUMNAS);
        }
    }

    /*-------------SISTEMA DE DISPAROS-------------*/

    public boolean disparar(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
            return false;
        }
        if (vistaJugador[fila][columna] != '?') {
            return false;
        }
        int valor = barcos[fila][columna];
        if (valor == 0) {
            vistaJugador[fila][columna] = 'O';
        } else {
            vistaJugador[fila][columna] = 'X'; // ¡solo cambia la vista!
            if (barcoHundido(valor)) {
                marcarHundido(valor); // ahora sí usa 'valor'
            }
        }
        return true;
    }

    private boolean barcoHundido(int tipo) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (barcos[i][j] == tipo && vistaJugador[i][j] == '?') {
                    return false; // aún hay parte del barco sin tocar
                }
            }
        }
        return true; // todas las partes fueron tocadas
    }

    private void marcarHundido(int tipo) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (barcos[i][j] == tipo && vistaJugador[i][j] == 'X') {
                    vistaJugador[i][j] = '#';
                }
            }
        }
    }


    public boolean juegoFinalizado() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (barcos[i][j] > 0) {
                    return false; // aún hay barcos no hundidos
                }
            }
        }
        return true;
    }


    // === Métodos para mostrar ===
    //ESTE METODO LE MOSTRARA AL USUARIO EL TABLERO
    public void mostrarVistaJugador() {
        System.out.println("    A B C D E F G H");
        for (int i = 0; i < FILAS; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(vistaJugador[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Y ESTE MOSTRARA EL TABLERO CON LA UBICACION DE LOS BARCOS
    public void mostrarTableroOriginal() {
        System.out.println();
        System.out.println("=== UBICACIÓN DE LAS NAVES EN EL TABLERO ===");
        System.out.println("    A B C D E F G H");
        for (int i = 0; i < FILAS; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < COLUMNAS; j++) {
                int id = copiaOriginal[i][j];
                char simbolo = switch (id) {
                    case 1 -> 'S';
                    case 2 -> 'D';
                    case 3 -> 'C';
                    default -> '~';
                };
                System.out.print(simbolo + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("S = Submarino, D = Destructor, C = Crucero, ~ = Agua");
        System.out.println();
    }

    // === Getters útiles ===

    public char[][] getVistaJugador() {
        return vistaJugador;
    }

    public int[][] getCopiaOriginal() {
        return copiaOriginal;
    }
}





