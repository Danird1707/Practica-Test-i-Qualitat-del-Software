# 🧩 Estructura de clases para un Tetris con interfaz gráfica en Java

La idea es separar la **lógica del juego** de la **interfaz gráfica**, siguiendo una estructura clara.

---

## 🔹 1. Clases principales

### `Tetris`
- Clase principal con el método `main`.
- Se encarga de inicializar la ventana (`JFrame`) y arrancar el juego.

---

## 🔹 2. Interfaz gráfica

### `GamePanel` (extiende `JPanel`)
- Lienzo donde se dibuja el tablero y las piezas.
- Implementa `paintComponent(Graphics g)` para dibujar el estado actual.
- Puede usar un `Timer` (Swing) para actualizar y repintar el juego.

### `GameWindow` (extiende `JFrame`)
- Ventana principal.
- Contiene el `GamePanel`.
- Maneja el input de teclado (flechas, barra espaciadora, etc.).

---

## 🔹 3. Lógica del juego

### `Board`
- Representa el tablero como una **matriz de celdas**.
- Métodos principales:
    - `canMove(Piece pieza, int dx, int dy)` → verifica colisiones.
    - `fixPiece(Piece pieza)` → fija la pieza al tablero.
    - `clearLines()` → elimina filas completas y devuelve cuántas se limpiaron.

### `Piece`
- Representa una pieza de Tetris (Tetrominó).
- Atributos:
    - `Point[] blocks` → coordenadas relativas.
    - `Color color`.
    - `int x, y` → posición en el tablero.
- Métodos:
    - `rotate()` → gira la pieza (90°).
    - `move(dx, dy)` → intenta mover.
    - `getBlocks()` → devuelve posiciones absolutas.

### `PieceFactory`
- Genera piezas nuevas aleatorias (I, O, T, L, J, S, Z).
- Usa un `Random`.

---

## 🔹 4. Control del juego

### `Game`
- Controla la lógica general.
- Atributos:
    - `Board board`
    - `Piece currentPiece`
    - `Piece nextPiece`
    - `int score`
    - `boolean isGameOver`
- Métodos:
    - `update()` → baja la pieza un paso.
    - `spawnPiece()` → coloca nueva pieza.
    - `checkGameOver()`.
    - `restart()`.

---

## 🔹 5. Input del jugador

En el `GameWindow` con un `KeyListener`:

- `→` mover derecha
- `←` mover izquierda
- `↓` bajar más rápido
- `↑` rotar
- `Espacio` → soltar hasta abajo

---


✅ Con esta separación:
- `Board`, `Piece`, `Game` no dependen de Swing.
- `GamePanel` dibuja y actualiza.
- `GameWindow` maneja input y ventana.  
