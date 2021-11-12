package main.java;


import de.fhpotsdam.unfolding.UnfoldingMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.TreeSet;

//panel de restaurantes hecho a mano

public class PnlRestaurantes extends JPanel implements ActionListener, ChangeListener {

    private UnfoldingMap mapa;
    public TreeSet<Restaurante> restaurantes;
    public static JList<String> jlistrestaurantes;

    Mapa map;

    String[] listarestaurantes = new String[25];

    JButton btnBuscar,btnGeneradorAleatorio,btnperfil, borrarfiltros, reservar;

    JTextField jtxtBuscar,jtxtbarrio;
    JLabel lblfiltros, lbltitulo;

    JCheckBox tipo, restaurante, bar, taberna, comidarapida, barrio; //podriamos poner tambien valoraciones, valoraciones, una, dos, tres,cuatro,cinco;


    public PnlRestaurantes()
    {
        this.setLayout(null);
        // restaurantes = IO.leerFicheroRestaurantes(); //no sirve porque no usamos la clase io ni ficheros
        //this.setBounds(0, 0, getMaximumSize().width, 1000);
        this.setBackground(new Color(221, 234, 245, 202));

        //lista de restaurantes de prueba que vamos usando para asegurarnos que se muestran en el scrollpane
        restaurantes = new TreeSet<>();

        restaurantes.add(new Restaurante("Ginos", "Calle Julian Romea, 4 "));
        restaurantes.add(new Restaurante("La Máquina", "Calle Ponzano, 39"));
        restaurantes.add(new Restaurante("Lateral", "Pº Castellana, 42"));
        restaurantes.add(new Restaurante("Five Guys", "Calle Gran Via, 44"));
        //System.out.println(restaurantes);



        jlistrestaurantes = new JList<>(listarestaurantes);
        JScrollPane barraDesplazamiento = new JScrollPane(jlistrestaurantes);



        barraDesplazamiento.setViewportView(jlistrestaurantes);
        jlistrestaurantes.setLayoutOrientation(JList.VERTICAL);
        jlistrestaurantes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jlistrestaurantes.setLayoutOrientation(JList.VERTICAL);

        //recorremos el treeset de restaurantes para guardarlo en el vector de Strings que semete en el jlist
        for (Restaurante r : restaurantes)
        {
            for (int i = 0; i < listarestaurantes.length; i++){

                if(listarestaurantes[i]==null) {
                    listarestaurantes[i] = r.getNombre() + "," + r.getDireccion();
                    i = listarestaurantes.length;
                }
            }
        }
        jlistrestaurantes.setBackground(new Color(133, 177, 204, 182));
        jlistrestaurantes.setFont(new Font("Lirio", Font.ITALIC, 25));
        jlistrestaurantes.setForeground(Color.BLACK);


        barraDesplazamiento.setBounds(1150,240,350,420);
        this.add(barraDesplazamiento);

        reservar= new JButton("RESERVAR");
        reservar.setFont(new Font("Lirio", Font.BOLD, 20));
        reservar.setForeground(Color.BLACK);
        reservar.setHorizontalTextPosition( SwingConstants.CENTER );
        reservar.setVerticalTextPosition( SwingConstants.BOTTOM );
        reservar.setBackground(new Color(133, 177, 204, 182));
        reservar.setBounds(1150,670,350,60);
        this.add(reservar);

        jtxtBuscar = new JTextField(30);
        jtxtBuscar.setBounds(1180,190,200,40);
        this.add(jtxtBuscar);

        btnBuscar = new JButton("BUSCAR");
        //btnBuscar.addActionListener(this);
        btnBuscar.setFont(new Font("Georgia", Font.BOLD, 12));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setHorizontalTextPosition( SwingConstants.CENTER );
        btnBuscar.setVerticalTextPosition( SwingConstants.BOTTOM );
        btnBuscar.setBackground(new Color(133, 177, 204, 182));//(90, 130, 156));
        btnBuscar.setBounds(1400,190,100,40);
        this.add(btnBuscar);

        lblfiltros=new JLabel("Filtros: ");
        lblfiltros.setFont(new Font("Lirio", Font.ITALIC, 30));
        lblfiltros.setForeground(Color.BLACK);
        lblfiltros.setBounds(100,190,100,40);
        this.add(lblfiltros);

        tipo=new JCheckBox("Tipo de comida");
        tipo.setFont(new Font("Georgia", Font.BOLD, 15));
        tipo.setForeground(Color.BLACK);
        tipo.setBounds(80,260,180,30);
        tipo.setBackground(new Color(133, 177, 204, 182));
        this.add(tipo);

            restaurante =new JCheckBox("Restaurante");
            restaurante.setFont(new Font("Georgia", Font.BOLD, 15));
            restaurante.setForeground(Color.BLACK);
            restaurante.setBounds(100,300,160,30);
            restaurante.setBackground(new Color(133, 177, 204, 182));
            restaurante.setEnabled(false);
            this.add(restaurante);

            bar =new JCheckBox("Bar");
            bar.setFont(new Font("Georgia", Font.BOLD, 15));
            bar.setForeground(Color.BLACK);
            bar.setBounds(100,340,160,30);
            bar.setBackground(new Color(133, 177, 204, 182));
            bar.setEnabled(false);
            this.add(bar);

            taberna =new JCheckBox("Taberna");
            taberna.setFont(new Font("Georgia", Font.BOLD, 15));
            taberna.setForeground(Color.BLACK);
            taberna.setBounds(100,380,160,30);
            taberna.setBackground(new Color(133, 177, 204, 182));
            taberna.setEnabled(false);
            this.add(taberna);

            comidarapida=new JCheckBox("Comida Rápida");
            comidarapida.setFont(new Font("Georgia", Font.BOLD, 15));
            comidarapida.setForeground(Color.BLACK);
            comidarapida.setBounds(100,420,160,30);
            comidarapida.setBackground(new Color(133, 177, 204, 182));
            comidarapida.setEnabled(false);
            this.add(comidarapida);

        barrio =new JCheckBox("Barrio");
        barrio.setFont(new Font("Georgia", Font.BOLD, 15));
        barrio.setForeground(Color.BLACK);
        barrio.setBounds(80,480,180,30);
        barrio.setBackground(new Color(133, 177, 204, 182));
        this.add(barrio);

            jtxtbarrio=new JTextField(20);
            jtxtbarrio .setBounds(100,520,160,30);
            jtxtbarrio.setEnabled(false);
            this.add(jtxtbarrio);

        tipo.addChangeListener(this);
        barrio.addChangeListener(this);


        borrarfiltros=new JButton("Borrar filtros");
        borrarfiltros.setFont(new Font("Lirio", Font.BOLD, 20));
        borrarfiltros.setForeground(Color.BLACK);
        borrarfiltros.setHorizontalTextPosition( SwingConstants.CENTER );
        borrarfiltros.setVerticalTextPosition( SwingConstants.BOTTOM );
        borrarfiltros.setBackground(new Color(133, 177, 204, 182));
        borrarfiltros.setBounds(80,580,200,30);
        borrarfiltros.addActionListener(this);
        this.add(borrarfiltros);



        btnGeneradorAleatorio= new JButton();
        btnGeneradorAleatorio.setText("<html><p>RESTAURANTE</p><p>ALEATORIO</p></html>");
        btnGeneradorAleatorio.setFont(new Font("Georgia", Font.BOLD, 20));
        btnGeneradorAleatorio.setForeground(Color.BLACK);
        btnGeneradorAleatorio.setHorizontalTextPosition( SwingConstants.CENTER );
        btnGeneradorAleatorio.setVerticalTextPosition( SwingConstants.BOTTOM );
        btnGeneradorAleatorio.setBackground(new Color(133, 177, 204, 182));
        btnGeneradorAleatorio.setBounds(80,650,200,80);
        this.add(btnGeneradorAleatorio);


        btnperfil = new JButton();
        ImageIcon perfil = new ImageIcon("src"+ File.separator +"main"+ File.separator + "resources" + File.separator + "perfilcoloreado.bmp.png");
        ImageIcon imagenperfil = new ImageIcon(perfil.getImage().getScaledInstance(80,-1,Image.SCALE_DEFAULT));
        //imagen del logo
        btnperfil.setIcon(imagenperfil);
        //btnperfil.setBackground(new Color(90, 130, 156));
        btnperfil.setBounds(JInicioSesion.screenSize.width-120,10,80,80);
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

//tengo que poner que si seleccionan el checkbox tipo de comida o el de precio se ponga setEnable(true) los correspondientes
}

