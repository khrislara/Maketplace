package com.myapplication.marketplace;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.myapplication.marketplace.adapter.ProductoAdapter;
import com.myapplication.marketplace.model.Producto;
import java.util.ArrayList;
import java.util.List;
import com.diegodev.marketplace.R;
// SE ELIMINA 'implements SearchView.OnQueryTextListener'
// La implementación se hace de forma anónima en el método configurarBuscador() (SEMANA5)
public class Home extends AppCompatActivity {
    // Vistas principales
    private RecyclerView recyclerView;
    private FloatingActionButton fabPublicar;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;
    // SEMANA 5: Referencia al Buscador y al Adaptador
    private SearchView searchViewProductos;
    private ProductoAdapter productoAdapter; // Necesario para llamar al método filtrar()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 1. Inicializar Vistas
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rv_productos);
        fabPublicar = findViewById(R.id.fab_publicar);
        // SEMANA 5: Inicializar el SearchView directamente desde el layout
        searchViewProductos = findViewById(R.id.search_view_productos);
        // --- SEMANA 4: Inicialización del Bottom Nav (con Toast)
        bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // SEMANA 5.1: Manejar la navegación a la pantalla de Cuenta
            if (itemId == R.id.navigation_account) {
                Intent intent = new Intent(Home.this, CuentaActivity.class);
                startActivity(intent);
                return true;
            }
            // Revertido a la lógica de Toast temporal (para otros ítems)
            Toast.makeText(Home.this, "Navegando a: " + item.getTitle(),
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        // --- FIN SEMANA 4 ---
        // 2. Configuración CLAVE del RecyclerView
        configurarRecyclerView();
        // SEMANA 5: Configuración del Listener del Buscador para escuchar los cambios de texto
        configurarBuscador();
        // 3. Botón FAB para publicar producto
        fabPublicar.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Publicar.class);
            startActivity(intent);
        });
    }
    /**
     * SEMANA 5: Método para configurar el buscador.
     * Implementa OnQueryTextListener para detectar el texto ingresado.
     */
    private void configurarBuscador() {
        if (searchViewProductos != null) {
            searchViewProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // No hacemos nada al enviar la búsqueda (ej: presionar Enter)
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    // SEMANA 5: Lógica del filtro - llama al método filtrar del ProductoAdapter
                    if (productoAdapter != null) {
                        productoAdapter.filtrar(newText);
                    }
                    return true;
                }
            });
        }
    }
    // 2. Configura el RecyclerView con el LayoutManager y el ProductoAdapter
    private void configurarRecyclerView() {
        // 2.1. Define cómo se organizan los ítems (en este caso, en una lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 2.2. Obtiene datos de prueba
        List<Producto> productosDePrueba = cargarProductosDePrueba();
        // 2.3. Crea el adaptador y lo conecta al RecyclerView
        // SEMANA 5: Almacenamos la referencia en la variable de clase (productoAdapter) para
        usarla en el filtro
        productoAdapter = new ProductoAdapter(this, productosDePrueba);
        recyclerView.setAdapter(productoAdapter);
    }
    // 3. Crea una lista de productos ficticios para probar el RecyclerView (Simulación de DB).
    private List<Producto> cargarProductosDePrueba() {
        List<Producto> lista = new ArrayList<>();
        // ID, Nombre, Descripción (se omite aquí), Precio, ImagenUrl (ficticia)
        lista.add(new Producto("ID001", "Bicicleta Vintage", "Clásica bicicleta de ruta.", "150.000",
                "url_ficticia_1"));
        lista.add(new Producto("ID002", "Auriculares Inalámbricos", "Cancelación de ruido activa.",
                "75.990", "url_ficticia_2"));
        lista.add(new Producto("ID003", "Libro: El Señor de los Anillos", "Edición de lujo tapa
                dura.", "29.990", "url_ficticia_3"));
        lista.add(new Producto("ID004", "Teclado Mecánico RGB", "Switches marrones, 60%.",
                "55.000", "url_ficticia_4"));
        lista.add(new Producto("ID005", "Silla de Oficina Ergonómica", "Soporte lumbar ajustable.",
                "99.990", "url_ficticia_5"));
        lista.add(new Producto("ID006", "Cámara Instantánea", "Incluye 10 películas.", "45.000",
                "url_ficticia_6"));
        lista.add(new Producto("ID007", "Laptop Gaming", "Potente para juegos y trabajo.",
                "750.000", "url_ficticia_7"));
        lista.add(new Producto("ID008", "Mesa de Centro", "Diseño moderno de madera.",
                "80.000", "url_ficticia_8"));
        lista.add(new Producto("ID009", "Zapatillas Deportivas", "Para correr, talla 42.", "35.000",
                "url_ficticia_9"));
        return lista;
    }
    // --- Menú Superior (Toolbar) ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menú que contiene solo el ítem de logout
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(Home.this, "Cerrando Sesión (Simulación)",
                    Toast.LENGTH_SHORT).show();
            irALogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Navega de vuelta a LoginActivity
    public void irALogin() {
        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
        Debemos modifica los archivos themes para aplicar estilos a las estiquetas de nuestra vista
        activity_cuenta.xml que se encuentran en la siguiente ruta: res-> values -> themes -> themes.xml y
themes.xml(night)
<resources xmlns:tools="http://schemas.android.com/tools">
 <!-- Base application theme. -->
 <style name="Base.Theme.MarketPlaces" parent="Theme.Material3.DayNight.NoActionBar">
 </style>
 <style name="Theme.MarketPlaces" parent="Base.Theme.MarketPlaces" />
 <!-- Estilo para las etiquetas (Nombres, Email, etc.) -->
 <style name="PerfilLabel">
 <item name="android:layout_width">0dp</item>
 <item name="android:layout_height">wrap_content</item>
 <item name="android:layout_columnWeight">0.3</item>
 <item name="android:textSize">16sp</item>
 <item name="android:textStyle">bold</item>
 <item name="android:paddingTop">4dp</item>
 <item name="android:paddingBottom">4dp</item>
 </style>
 <!-- Estilo para los valores (Prueba Prueba, Prueba@gmail.com, etc.) -->
 <style name="PerfilValor">
 <item name="android:layout_width">0dp</item>
 <item name="android:layout_height">wrap_content</item>
 <item name="android:layout_columnWeight">0.7</item>
 <item name="android:textSize">16sp</item>
 <item name="android:textColor">@android:color/darker_gray</item>
 <item name="android:gravity">end</item>
 <!-- CORRECCIÓN: Usamos paddingTop y paddingBottom en lugar de paddingVertical para
        compatibilidad con API < 26 -->
 <item name="android:paddingTop">4dp</item>
 <item name="android:paddingBottom">4dp</item>
 </style>
</resources>