package icai.dtc.isw.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.Cliente;
import main.java.Reserva;
import main.java.TarjetaCredito;
import org.apache.log4j.Logger;

import icai.dtc.isw.configuration.PropertiesISW;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.message.Message;

import main.java.Restaurante;

import javax.swing.*;

public class Client implements Serializable {
	private String host;
	private int port;
	final static Logger logger = Logger.getLogger(Client.class);

	public void envio(String contexto, HashMap<String, Object> session) {
		//quitamos el main y ponemos que esto sea un método.

		//Configure connections
		//String host = PropertiesISW.getInstance().getProperty("host");
		//int port = Integer.parseInt(PropertiesISW.getInstance().getProperty("port"));

		String host = "127.0.0.1";
		int port =  8081;

		Logger.getRootLogger().info("Host: "+host+" port"+port);
		//Create a cliente class
		Client client=new Client(host, port);
		
	//	HashMap<String,Object> session=new HashMap<String, Object>();
		//session.put("/getCustomer","");
		
		Message mensajeEnvio=new Message();
		Message mensajeVuelta=new Message();
		mensajeEnvio.setContext(contexto);
		mensajeEnvio.setSession(session);
		client.sent(mensajeEnvio,mensajeVuelta);

		System.out.println("LLego aqui");
		System.out.println("ME C : " + mensajeEnvio.getContext());
		System.out.println("ME S : " + mensajeEnvio.getSession());
		System.out.println("MV C : " + mensajeVuelta.getContext());
		System.out.println("MV S : " + mensajeEnvio.getSession());





		switch (mensajeVuelta.getContext()) {
			case "/hacerLoginResponse": //"/hacerLoginResponse"
				int res=(Integer) mensajeVuelta.getSession().get("RespuestaLogin");
				session.put("RespuestaLogin",res);
				break;

			case"/buscarRestauranteResponse":
				int res2 =(Integer) mensajeVuelta.getSession().get("RespuestaBuscarRestaurante");
				session.put("RespuestaBuscarRestaurante", res2);
				break;

			case "/obtenerListaRestaurantesResponse":
				ArrayList<Restaurante> res1 = (ArrayList<Restaurante>) mensajeVuelta.getSession().get("RespuestaObtenerListaRestaurantes");
				session.put("RespuestaObtenerListaRestaurantes", res1);
				break;

			case "/obtenerInfoClienteResponse":
				Cliente cliente = (Cliente) mensajeVuelta.getSession().get("RespuestaObtenerInfoCliente");
				session.put("RespuestaObtenerInfoCliente", cliente);
				break;


			case "/filtrarResponse":
				ArrayList<Restaurante> listafiltrada = (ArrayList<Restaurante>) mensajeVuelta.getSession().get("RespuestaFiltrar");
				session.put("RespuestaFiltrar", listafiltrada);
				break;

			case "/obtenerIgualesResponse":
				ArrayList<Restaurante> listaIguales = (ArrayList<Restaurante>) mensajeVuelta.getSession().get("RespuestaObtenerIguales");
				session.put("RespuestaObtenerIguales", listaIguales);
				break;


			case "/hacerRegistroResponse":
				//System.out.println("MV get Session : "+ mensajeVuelta.getSession());
				int res3 = (Integer) mensajeVuelta.getSession().get("RespuestaRegistro");
				//System.out.println("res3" +  res3);
				session.put("RespuestaRegistro", res3);

				//System.out.println("LLego aqui");
				//System.out.println(session);
				break;

			case "/incluirTarjetaResponse":
				int res4 = (Integer) mensajeVuelta.getSession().get("RespuestaIncluirTarjeta");
				//System.out.println("res3" +  res3);
				session.put("RespuestaIncluirTarjeta", res4);

				//System.out.println("LLego aqui");
				//System.out.println(session);
				break;

			case "/obtenerRestauranteAleatorioResponse":
				Restaurante restauranteAleatorio = (Restaurante) mensajeVuelta.getSession().get("RespuestaObtenerRestauranteAleatorio");
				session.put("RespuestaObtenerRestauranteAleatorio", restauranteAleatorio);
				break;

			case "/hacerReservaResponse":
				//System.out.println()
				int res5 = (Integer) mensajeVuelta.getSession().get("RespuestaReserva");
				session.put("RespuestaReserva", res5);
				break;

			case "/mostrarReservasAnterioresResponse":
				ArrayList<Reserva> listaReservasAnteriores = (ArrayList<Reserva>) mensajeVuelta.getSession().get("RespuestaMostrarReservasAnteriores");
				session.put("RespuestaMostrarReservasAnteriores", listaReservasAnteriores);
				break;

			case "/obtenerCodigoUltimaReservaResponse":
				int codigoUltimaReserva = (Integer) mensajeVuelta.getSession().get("RespuestaObtenerCodigoUltimaReserva");
				session.put("RespuestaObtenerCodigoUltimaReserva", codigoUltimaReserva);
				break;



			default:
				Logger.getRootLogger().info("Option not found");
				System.out.println("\nError a la vuelta");
				break;

		}

		//System.out.println("3.- En Main.- El valor devuelto es: "+((String)mensajeVuelta.getSession().get("Nombre")));
	}

	public Client()
	{

	}

	public Client(String host, int port) {
		this.host=host;
		this.port=port;
	}

	public void sent(Message messageOut, Message messageIn) {
		try {

			System.out.println("Connecting to host " + host + " on port " + port + ".");

			Socket echoSocket = null;
			OutputStream out = null;
			InputStream in = null;

			try {
				echoSocket = new Socket(host, port);
				in = echoSocket.getInputStream();
				out = echoSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);

				//Create the objetct to send
				objectOutputStream.writeObject(messageOut);


				// create a DataInputStream so we can read data from it.
		        ObjectInputStream objectInputStream = new ObjectInputStream(in);
				//HASTA AQUI SE EJECUTA PARA RESTAURANTES
		        Message msg=(Message)objectInputStream.readObject(); //esta linea ya no se ejecuta en restaurantes
				messageIn.setContext(msg.getContext());
		        messageIn.setSession(msg.getSession());
		        /*System.out.println("\n1.- El valor devuelto es: "+messageIn.getContext());
		        String cadena=(String) messageIn.getSession().get("Nombre");
		        System.out.println("\n2.- La cadena devuelta es: "+cadena);*/
				
			} catch (UnknownHostException e) {
				System.err.println("Unknown host: " + host);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Unable to get streams from server");
				System.exit(1);

			}		

			/** Closing all the resources */
			out.close();
			in.close();			
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}