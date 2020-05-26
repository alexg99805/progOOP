import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator; 
import java.util.Map;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class JsonParserClass {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// parsing file "JSONExample.json" 
		//http://www.unit-conversion.info/texttools/replace-text/
		//http://json.parser.online.fr/
		//https://www.unixtimestamp.com/index.php
		//https://www.w3schools.com/java/java_arraylist.asp
		
		Object obj = null;
		try {
			obj = new JSONParser().parse(new FileReader("response.json"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File json da parsare non trovato!\n" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore sconosciuto!\n" + e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Json formato errato!\n" + e.getMessage());
		} 
		
		// typecasting obj to JSONObject 
		JSONObject jo = (JSONObject) obj; 
		
		// Creo l'iteratore degli elementi dell'array!
		Iterator<Map.Entry> itr1;
		JSONArray ja = (JSONArray) jo.get("items"); //Prendo l'array items che contiene le date
		
		// iteratore dei field all'interno del singolo elemento array
		Iterator itr2 = ja.iterator(); 

		//Creo due array dinamici: il primo mi memorizza il tempo di pubblicazione di ogni media
		//il secondo, se il media è un video o meno.
		ArrayList<Long> tempos = new ArrayList<Long>();
		ArrayList<Boolean> tipomedia = new ArrayList<Boolean>();
		
		boolean is_video = false;
		
		//Itero tutti gli item presenti nel response json
		while (itr2.hasNext()) 
		{ 
			//Itero il singolo elemento nell'array
			itr1 = ((Map) itr2.next()).entrySet().iterator(); 
			is_video = false;
			
			while (itr1.hasNext()) { 
				//Accedo ai vari field... devo trovare taken_at visto che è quello che contiene il tempo di pubblicazione
				Map.Entry pair = itr1.next(); 
				if(pair.getKey().equals("taken_at"))
					tempos.add((long)pair.getValue()); //Lo aggiungo alla lista dnamica
				    //System.out.println(pair.getValue()); 
				
				//Se esiste una chiave "video_versions" vuol dire che il media è un video, senno appeso.
				if(pair.getKey().equals("video_versions"))
					is_video = true;
			} 
			
			if(is_video) tipomedia.add(true);
			else tipomedia.add(false);
			//Quindi vado a identificare nella stessa posizione se ho un video o una foto.
		} 
		
		/*
		Date date = new Date(1318386508000L);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println(formatted);
        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        formatted = format.format(date);
        System.out.println(formatted);
        */
		
		//Ottengo massimo e minimo, per gli estremi di pubblicaizone. (Prima e ultima pubblicazione)
		long minimo = Collections.min(tempos)*1000; //Conta in millis *1000
		long massimo = Collections.max(tempos)*1000; //Conta in millis  *1000
		long diffdays = (massimo-minimo)/(1000*60*60*24) + 1; //Da oggi a domani sono 2 giorni, non 1!
		//Aggiungo 1 perche dal 23 al 26 (esempio) sono 4 giorni e non 3...
				
		for(int i=0; i<tempos.size(); i++) {
			Date date = new Date(tempos.get(i)*1000);
	        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	        String formatted = format.format(date);
	        System.out.println("Data post: " + formatted);
	        //Stampo la data del post, convertendo l'epochtime come numero di secondi dal 01/01/1970 fino ad oggi
		}
		
		//stampo i risultati ottenuti
		System.out.println("\n---Analisi---\n");
		System.out.println("Numero di media totali: " + tempos.size());
		System.out.println("Numero di video: " + Collections.frequency(tipomedia, true));
		System.out.println("Numero di post o immagini: " + Collections.frequency(tipomedia, false));
		System.out.println("Frequenza pubblicazione media totali: " + (float)tempos.size()/diffdays + " al giorno");
		System.out.println("Frequenza pubblicazione immagini: " +(float) Collections.frequency(tipomedia, false)/diffdays + " al giorno");
		System.out.println("Frequenza pubblicazione video: " + (float)Collections.frequency(tipomedia, true)/diffdays + " al giorno");
	}
}
