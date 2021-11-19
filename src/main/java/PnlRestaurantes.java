package main.java;


//import de.fhpotsdam.unfolding.UnfoldingMap;
import icai.dtc.isw.client.Client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.*;

/**panel princpal en el que se muestra la lista de restaurantes, los filtros y mas adelante el mapa**/

public class PnlRestaurantes extends JPanel implements ActionListener, ChangeListener, Serializable{

    //private UnfoldingMap mapa;
    public ArrayList<Restaurante> restaurantes;
    public static JList<String> jlistrestaurantes;

    public static JPanel panelNorte;

    public static JPanel panelInfoRestaurante;

    Mapa map;

    String[] listarestaurantes = new String[25];

    JButton btnBuscar,btnGeneradorAleatorio,btnperfil, borrarfiltros, reservar, aceptar;

    JTextField jtxtBuscar,jtxtbarrio;
    JLabel lblfiltros, lbltitulo;

    JCheckBox tipo, restaurante, bar, taberna, comidarapida, barrio; //podriamos poner tambien valoraciones, valoraciones, una, dos, tres,cuatro,cinco;

    private Dimension dim;

    /**constructor del panel en el que inicializamos todos los elementos**/

    public PnlRestaurantes()
    {
        this.setLayout(null);
        // restaurantes = IO.leerFicheroRestaurantes(); //no sirve porque no usamos la clase io ni ficheros
        //this.setBounds(0, 0, getMaximumSize().width, 1000);
        this.setBackground(new Color(221, 234, 245, 202));

        dim = super.getToolkit().getScreenSize();
        this.setSize(JInicioSesion.screenSize.width, JInicioSesion.screenSize.height-100);



        //super.setSize(dim);

        //para que cuando se inicie sesion y cambie a la pantalla principal se ponga en modo panalla completa:
        //this.setPreferredSize(new Dimension(getMaximumSize().width,getMaximumSize().height));
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //lista de restaurantes de prueba que vamos usando para asegurarnos que se muestran en el scrollpane
        restaurantes = new ArrayList<Restaurante>();

        panelInfoRestaurante = new JPanelInfoRestaurante();


        /**codigo para conectar con las bases de datos**/
        Client client=new Client();
        HashMap<String,Object> session=new HashMap<String, Object>();
        session.put("user",11);
        session.put("pass",11);
        client.envio("/obtenerListaRestaurantes",session);

        // CustomerDAO customerDAO = new CustomerDAO();
        ArrayList<Restaurante> respuesta = (ArrayList<Restaurante>) session.get("RespuestaObtenerListaRestaurantes");

        jlistrestaurantes = new JList<>();
        JScrollPane barraDesplazamiento = new JScrollPane(jlistrestaurantes);

        DefaultListModel modelo = new DefaultListModel();


        for (Restaurante r: respuesta)
        {
            restaurantes.add(r);
            String nombre = r.getNombreRestaurante();
            modelo.addElement(nombre);
        }


        jlistrestaurantes.setModel(modelo);

        barraDesplazamiento.setViewportView(jlistrestaurantes);
        jlistrestaurantes.setLayoutOrientation(JList.VERTICAL);
        jlistrestaurantes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jlistrestaurantes.setLayoutOrientation(JList.VERTICAL);

        jlistrestaurantes.setBackground(new Color(133, 177, 204, 182));
        jlistrestaurantes.setFont(new Font("Lirio", Font.ITALIC, 15));
        jlistrestaurantes.setForeground(Color.BLACK);


        barraDesplazamiento.setBounds(JInicioSesion.ancho-350-35,210,350,420);
        this.add(barraDesplazamiento);


        //info del boton reservar

        reservar= new JButton("RESERVAR");
        reservar.setFont(new Font("Lirio", Font.BOLD, 20));
        reservar.setForeground(Color.BLACK);
        reservar.setHorizontalTextPosition( SwingConstants.CENTER );
        reservar.setVerticalTextPosition( SwingConstants.BOTTOM );
        reservar.setBackground(new Color(133, 177, 204, 182));
        reservar.setBounds(JInicioSesion.ancho-350-35,640,350,60);
        this.add(reservar);

        jtxtBuscar = new JTextField(30);
        jtxtBuscar.setBounds(JInicioSesion.ancho-350-35,160,200,40);
        this.add(jtxtBuscar);


        //info del boton buscar

        btnBuscar = new JButton("BUSCAR");
        //btnBuscar.addActionListener(this);
        btnBuscar.setFont(new Font("Georgia", Font.BOLD, 12));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setHorizontalTextPosition( SwingConstants.CENTER );
        btnBuscar.setVerticalTextPosition( SwingConstants.BOTTOM );
        btnBuscar.setBackground(new Color(133, 177, 204, 182));//(90, 130, 156));
        btnBuscar.setBounds(JInicioSesion.ancho-130-35,160,130,40);
        this.add(btnBuscar);



        //cuando se pulse "enter" tras introducir en el buscador que se vaya al boton de buscar
        jtxtBuscar.addKeyListener (new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent keyEvent){
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                    btnBuscar.requestFocus();
            }
        });

        //si se presiona "enter" que guarde lo escrito en el buscador
        btnBuscar.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent keyEvent){
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                    try{
                        String restauranteBuscado =jtxtBuscar.getText(); //aqui tenemos que poner algo del tipo, si existe este nombre en la base de datod
                        comprobarRestaurante(restauranteBuscado);
                        jtxtBuscar.setText("");
                    }
                    catch (BuscarRestauranteException bre) {
                        JOptionPane.showMessageDialog(PnlRestaurantes.this, bre.getMessage());
                        jtxtBuscar.setText("");
                        jtxtBuscar.requestFocus();
                    }

            }
        });

        //cuando se pulse que compare si esta el restaurante en la bbdd
        btnBuscar.addActionListener(e -> {
            try{
                String restauranteBuscado =jtxtBuscar.getText(); //aqui tenemos que poner algo del tipo, si existe este nombre en la base de datod
                comprobarRestaurante(restauranteBuscado);
                jtxtBuscar.setText("");
            }
            catch (BuscarRestauranteException bre) {
                JOptionPane.showMessageDialog(PnlRestaurantes.this, bre.getMessage());
                jtxtBuscar.setText("");
                jtxtBuscar.requestFocus();
            }
        });


        //filtros

        lblfiltros=new JLabel("Filtros: ");
        lblfiltros.setFont(new Font("Lirio", Font.ITALIC, 30));
        lblfiltros.setForeground(Color.BLACK);
        lblfiltros.setBounds(100,160,100,40);
        this.add(lblfiltros);

        tipo=new JCheckBox("Tipo de comida");
        tipo.setFont(new Font("Georgia", Font.BOLD, 15));
        tipo.setForeground(Color.BLACK);
        tipo.setBounds(80,230,180,30);
        tipo.setBackground(new Color(133, 177, 204, 182));
        this.add(tipo);

            restaurante =new JCheckBox("Restaurante");
            restaurante.setFont(new Font("Georgia", Font.BOLD, 15));
            restaurante.setForeground(Color.BLACK);
            restaurante.setBounds(100,270,160,30);
            restaurante.setBackground(new Color(133, 177, 204, 182));
            restaurante.setEnabled(false);
            this.add(restaurante);

            bar =new JCheckBox("Bar");
            bar.setFont(new Font("Georgia", Font.BOLD, 15));
            bar.setForeground(Color.BLACK);
            bar.setBounds(100,310,160,30);
            bar.setBackground(new Color(133, 177, 204, 182));
            bar.setEnabled(false);
            this.add(bar);

            taberna =new JCheckBox("Taberna");
            taberna.setFont(new Font("Georgia", Font.BOLD, 15));
            taberna.setForeground(Color.BLACK);
            taberna.setBounds(100,350,160,30);
            taberna.setBackground(new Color(133, 177, 204, 182));
            taberna.setEnabled(false);
            this.add(taberna);

            comidarapida=new JCheckBox("Comida Rápida");
            comidarapida.setFont(new Font("Georgia", Font.BOLD, 15));
            comidarapida.setForeground(Color.BLACK);
            comidarapida.setBounds(100,390,160,30);
            comidarapida.setBackground(new Color(133, 177, 204, 182));
            comidarapida.setEnabled(false);
            this.add(comidarapida);

        barrio =new JCheckBox("Barrio");
        barrio.setFont(new Font("Georgia", Font.BOLD, 15));
        barrio.setForeground(Color.BLACK);
        barrio.setBounds(80,450,180,30);
        barrio.setBackground(new Color(133, 177, 204, 182));
        this.add(barrio);

            jtxtbarrio=new JTextField(20);
            jtxtbarrio .setBounds(100,490,160,30);
            jtxtbarrio.setEnabled(false);
            this.add(jtxtbarrio);

        tipo.addChangeListener(this);
        barrio.addChangeListener(this);


        borrarfiltros=new JButton("Borrar filtros");
        borrarfiltros.setFont(new Font("Lirio", Font.BOLD, 15));
        borrarfiltros.setForeground(Color.BLACK);
        borrarfiltros.setHorizontalTextPosition( SwingConstants.CENTER );
        borrarfiltros.setVerticalTextPosition( SwingConstants.BOTTOM );
        borrarfiltros.setBackground(new Color(133, 177, 204, 182));
        borrarfiltros.setBounds(80,580,200,20);
        borrarfiltros.addActionListener(this);
        this.add(borrarfiltros);

        aceptar=new JButton("Aceptar");
        aceptar.setFont(new Font("Lirio", Font.BOLD, 15));
        aceptar.setForeground(Color.BLACK);
        aceptar.setHorizontalTextPosition( SwingConstants.CENTER );
        aceptar.setVerticalTextPosition( SwingConstants.BOTTOM );
        aceptar.setBackground(new Color(133, 177, 204, 182));
        aceptar.setBounds(80,550,200,20);
        //aceptar.addActionListener(this);
        this.add(aceptar);


        btnGeneradorAleatorio= new JButton();
        btnGeneradorAleatorio.setText("<html><p>RESTAURANTE</p><p>ALEATORIO</p></html>");
        btnGeneradorAleatorio.setFont(new Font("Georgia", Font.BOLD, 20));
        btnGeneradorAleatorio.setForeground(Color.BLACK);
        btnGeneradorAleatorio.setHorizontalTextPosition( SwingConstants.CENTER );
        btnGeneradorAleatorio.setVerticalTextPosition( SwingConstants.BOTTOM );
        btnGeneradorAleatorio.setBackground(new Color(133, 177, 204, 182));
        btnGeneradorAleatorio.setBounds(80,620,200,80);
        this.add(btnGeneradorAleatorio);


        btnperfil = new JButton();
        ImageIcon perfil = new ImageIcon("src"+ File.separator +"main"+ File.separator + "resources" + File.separator + "perfilcoloreado.bmp.png");
        ImageIcon imagenperfil = new ImageIcon(perfil.getImage().getScaledInstance(80,-1,Image.SCALE_DEFAULT));
        //imagen del logo
        btnperfil.setIcon(imagenperfil);
        //btnperfil.setBackground(new Color(90, 130, 156));
        btnperfil.setBounds(JInicioSesion.ancho-120,10,80,80);
        btnperfil.addActionListener(this);
        JInicioSesion.panelNorte.add(btnperfil);


        // map = new Mapa(40.429944f, -3.712778f, 480, 310); //coordenadas icai


        //map=new Mapa();//(40.429944f, -3.712778f);
        //map.setBounds(180,150,400,310);
        //this.add(map);

    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==btnperfil)
        {
            JInicioSesion.crearPanelPeque("MI PERFIL", JInicioSesion.panelperfil);
        }


        if (e.getSource()==borrarfiltros)
        {
            if(tipo.isSelected())
            {
                tipo.setSelected(false);
            }
            if(barrio.isSelected())
            {
                barrio.setSelected(false);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        if(tipo.isSelected())
        {
            restaurante.setEnabled(true);
            bar.setEnabled(true);
            taberna.setEnabled(true);
            comidarapida.setEnabled(true);
        }else{
            restaurante.setSelected(false);
            bar.setSelected(false);
            taberna.setSelected(false);
            comidarapida.setSelected(false);

            restaurante.setEnabled(false);
            bar.setEnabled(false);
            taberna.setEnabled(false);
            comidarapida.setEnabled(false);

        }

        if (barrio.isSelected())
        {
            jtxtbarrio.setEnabled(true);
        }else{
            jtxtbarrio.setText("");
            jtxtbarrio.setEnabled(false);
        }

    }

    /**metodo que conecta el panel con la base de datos de restaurantes**/

    public int comprobarRestaurante(String restaurante) throws BuscarRestauranteException {
        //Atilano
        Client client=new Client();
        HashMap<String,Object> session=new HashMap<String, Object>();
        session.put("restaurante",restaurante);

        client.envio("/buscarRestaurante",session);
        // CustomerDAO customerDAO = new CustomerDAO();
        int respuesta = (Integer) session.get("RespuestaBuscarRestaurante");  //esto puede estar mal
        //customerDAO.autenticar(user, pw.toString());
        if(respuesta == 1)
        {
            JInicioSesion.crearPanelPeque("RESTAURANTE ENCONTRADO", this.panelInfoRestaurante);

        }

        else if (respuesta ==0)
        {
            throw new BuscarRestauranteException();
        }

        return respuesta;
    }

}

