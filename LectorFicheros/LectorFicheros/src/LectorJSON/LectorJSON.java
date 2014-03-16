package LectorJSON;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * http://blog.openalfa.com/como-leer-y-escribir-ficheros-json-en-java
 * http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=GsonJavaJSON
 */

public class LectorJSON {

	private String nameFile;
	private String respuesta;
	
	public LectorJSON(String nFile){
		nameFile = nFile;
	}
	
	public void buscarPropiedad(String nombrePropiedad) {
		JsonParser parser = new JsonParser();
		try {
			FileReader fr = new FileReader(nameFile);
			JsonElement datos = parser.parse(fr);
			//printAll(datos);
			String resp = leerJSONElement(datos, nombrePropiedad).toString();
			
			//Devolvemos la respuesta, eliminando la �ltima coma:
			int length = resp.length();
			if( length>0 && String.valueOf(resp.charAt(length-1)).compareTo(",")==0 )
				resp=resp.substring(0,resp.length()-1);
			respuesta = resp;
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	private String leerJSONElement(JsonElement elemento, String propiedad){
		String resp = "";
		
		if (elemento.isJsonObject()) {
			JsonObject obj = elemento.getAsJsonObject();

			resp += propiedadEncontrada(obj,propiedad);

			java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj
					.entrySet();
			java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas
					.iterator();
			while (iter.hasNext()) {
				java.util.Map.Entry<String, JsonElement> entrada = iter.next();
				String result = leerJSONElement(entrada.getValue(), propiedad);
				if (!result.isEmpty())
					resp += entrada.getKey().toString() + ":" + result
							+ ",";
			}
		} else if (elemento.isJsonArray()) {
			JsonArray array = elemento.getAsJsonArray();
			java.util.Iterator<JsonElement> iter = array.iterator();
			while (iter.hasNext()) {
				JsonElement entrada = iter.next();
				resp += leerJSONElement(entrada, propiedad);
			}
		}
		return resp;
	}
	
	
	private String propiedadEncontrada(JsonObject obj, String propiedad){
		/*
		 * Para saber si un objeto JSON tiene una propiedad dada,
		 * basta con utilizar el m�todo "has('nombrePropiedad')".
		 * Ese m�todo no ignora may�suclas/min�sculas, as� que nos obliga
		 * a hacer varias comparaciones...
		 */
		String respuesta = "";
		
		if( obj.has(propiedad) )
			respuesta = obj.get(propiedad).toString();
		if( obj.has(propiedad.toLowerCase()) )
			respuesta = obj.get(propiedad.toLowerCase()).toString();
		if( obj.has(propiedad.toUpperCase()) )
			respuesta = obj.get(propiedad.toUpperCase()).toString();
		
		String inicialMayuscula = String.valueOf(propiedad.charAt(0)).toUpperCase();
		String propiedadConInicialMayuscula = 
			inicialMayuscula + propiedad.substring(1).toLowerCase();
		if( obj.has(propiedadConInicialMayuscula) )
			respuesta = obj.get(propiedadConInicialMayuscula).toString();
		
		return respuesta;
	}
	
	/*
	private void printAll(JsonElement datos) {
		//a�adir: import com.google.gson.Gson;
		
		if (datos.isJsonObject()) {
			final Gson gson = new Gson();
			Object objects = gson.fromJson(datos.getAsJsonObject(),
					Object.class);
			System.out.println(objects.toString());

		} else if (datos.isJsonArray()) {
			final Gson gson = new Gson();
			Object objects = gson.fromJson(datos.getAsJsonArray(),
					Object.class);
			System.out.println(objects.toString());
		}
	}
	 */
	
	// GETTERS & SETTERS
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	public String getRespuesta() {
		return respuesta;
	}
}