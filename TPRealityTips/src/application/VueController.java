package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.io.*; 

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class VueController {

	
	@FXML
	private TextField txtBill;
	
	@FXML
	private TextField txtPourcentage;
	
	@FXML
	private TextField txtNbp;
	
	@FXML
	private Label labTip;
	
	@FXML
	private Label labTotal;
	
	@FXML
	private Label labErreur;
	
	@FXML
	private DatePicker datePickerDate;
	
	public void Calculate() {

		try {
			float bill = checkNumber(txtBill.getText());
			float pourcentage = checkNumber(txtPourcentage.getText());
			float nbp = checkNumber(txtNbp.getText());
			String date = checkDate(datePickerDate.getValue());
			
			checkNegatif(bill);
			checkNegatif(pourcentage);
			checkNegatif(nbp);
			
			checkNbp(nbp);

			float pb = bill * (pourcentage/100);
			float tip = pb / nbp;
		
			System.out.println(bill);
			System.out.println(pourcentage);
			System.out.println(nbp);
			System.out.println(tip);
			
			String result = Float.toString(tip);
			System.out.println(result);
			labTip.setText(result);
			
			float total = (pb + bill) / nbp;
			
			String resultTotal = Float.toString(total);
			labTotal.setText(resultTotal);
			
			String texte = date + ";" + bill + ";" + tip + ";" + nbp + "\n";
			if(this.Datefichier(date)) date = "\n" + date;
            FileWriter flux = new FileWriter("Calculs.txt", true);
            
            for (int i = 0; i < texte.length(); i++) 
            {
            	flux.write(texte.charAt(i));
            }
            flux.close();
            
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			labErreur.setText(e.getMessage());
		}
	}
	
	private float checkNumber (String saisie) throws Exception{
		try {
			return Float.parseFloat(saisie);
		}
		catch(Exception e) {
			throw new Exception("Il faut saisir des nombres");
		}
	}
	
	private void checkNegatif(Float saisie) throws Exception{
		if (saisie < 0) {
			throw new Exception("Il faut que les nombres saisie soit positif");
		}
	}
	
	private void checkNbp(Float saisie) throws Exception{
		if (saisie <= 0) {
			throw new Exception("Il faut que le nombre de personne soit supérieur à zéro");
		}
	}
	
	private String checkDate (LocalDate saisie) {
        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(saisie.toString());
            sdf.applyPattern("dd/MM/yyyy");
            return sdf.format(d);
            
        } catch(Exception e) {
            throw new NumberFormatException("Ce n'est pas une date valide.");
        }
    }
	
	
	private boolean Datefichier (String Date) throws IOException {
        Scanner fileScanner = new Scanner(new File("Calculs.txt"));
        String touteLignes = "";
        boolean trouve = false;
        while (fileScanner.hasNextLine()) {
             String ligne = fileScanner.nextLine();

             if(ligne.contains(Date)){
                 trouve = true;
             }else {
                 touteLignes += ligne + "\n"; 
             }
         }
        if(trouve) {
            try(FileWriter writer = new FileWriter("Calculs.txt")){
                writer.write(touteLignes);
            }
        }
        return trouve;
    }
	
	public void initialisation () throws FileNotFoundException{
		
		System.out.println(this.datePickerDate.getValue());
		String date = checkDate(datePickerDate.getValue());
        Scanner fichier = new Scanner(new File("Calculs.txt"));
        while (fichier.hasNextLine()) {
             String ligne = fichier.nextLine();
             if(ligne.contains(date)){
            	 String[] tableau = ligne.split(";");
     			 txtBill.setText(tableau[1]);
     			 txtPourcentage.setText(tableau[2]);
     			 txtNbp.setText(tableau[3]);
             }
         }
		
	}
	
	
	
	
	
}
