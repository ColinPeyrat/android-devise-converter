package ds2.acy.iut.m4104c_ds2;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MainActivity extends Activity {

    // definition des différents flag pour la variable de processing mode
    final static int FLAG_FRANCS_SUISSES = 1;
    final static int FLAG_DOLLARDS = 2;
    final static int FLAG_YEN = 3;

    // definition des variables statiques pour les taux de changes
    final static double TX_CHANGE_FRANCS_SUISSES = 1.09668;
    final static double TX_CHANGE_DOLLARDS = 1.12134;
    final static double TX_CHANGE_YEN = 128.057;

    // definition d'une variable de type processing mode pour savoir quelle devise est actuellement checked
    private int processingMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // on recupere les différents elements de l'interface pour pouvoir interagir avec
        final EditText montant = (EditText)findViewById(R.id.EditTextMontant);
        final TextView resultat = (TextView)findViewById(R.id.textViewResultat);
        RadioGroup radioDevise =(RadioGroup)findViewById(R.id.RadioGroupDevise);



        // on bind le RadioGroup de la devise pour detecter chaque changement d'item checké
        radioDevise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("RadioGroup", "itemCheckedChanged");

                // a chaque changement on change la valeur du processing mode
                switch (checkedId){
                    case R.id.frSuisse:
                        Log.i("RadioButton", "FrancsSuisses");
                        processingMode = FLAG_FRANCS_SUISSES;
                        break;

                    case R.id.dollard:
                        Log.i("RadioButton", "Dollard");
                        processingMode = FLAG_DOLLARDS;
                        break;

                    case R.id.yen:
                        Log.i("RadioButton", "Yen");
                        processingMode = FLAG_YEN;
                        break;

                }
                if(montant.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Veuillez indiquer un montant", Toast.LENGTH_SHORT).show();
                } else {
                    String montantValeur = montant.getText().toString();
                    if( !isDouble(montantValeur) || montantValeur.length() == 0){
                        Toast.makeText(getApplicationContext(), "Saisie incorrect", Toast.LENGTH_SHORT).show();
                    } else{
                        double doubleMontant = Double.parseDouble(montantValeur);
                        resultat.setText(calculerMontant(doubleMontant));
                    }
                }
            }
        });

        // on bind le EditText du montant pour detecter chaque changement de text
        montant.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("EditTextMontant", "afterTextChanged");
                Log.d("ProcessingMode", String.valueOf(processingMode));

                String montantValeur = montant.getText().toString();

                if( !isDouble(montantValeur) || montantValeur.length() == 0){
                    Toast.makeText(getApplicationContext(), "Saisie incorrect", Toast.LENGTH_SHORT).show();
                } else{
                    double doubleMontant = Double.parseDouble(montantValeur);
                    resultat.setText(calculerMontant(doubleMontant));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // méthode qui retourne le string exact avec la valeur convertis concatener avec la devise
    public String calculerMontant(double montant){
        String resultat = "";
        Log.d("D/montant",String.valueOf(montant));
        switch (processingMode){
            case FLAG_FRANCS_SUISSES:
                     resultat = String.valueOf(round(montant * TX_CHANGE_FRANCS_SUISSES,4))+" CHF";
                break;
            case FLAG_DOLLARDS:
                    resultat = String.valueOf(round(montant * TX_CHANGE_DOLLARDS,4))+" $";
                break;
            case FLAG_YEN:
                    resultat = String.valueOf(round(montant * TX_CHANGE_YEN,4))+" YEN";
                break;
            default:
                Toast.makeText(getApplicationContext(), "Veuillez choisir une devise", Toast.LENGTH_SHORT).show();
                break;
        }
        Log.i("I/conversion",resultat);
        return resultat;
    }

    // méthode qui permet de voir si un string peut etre convertis en double
    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // méthode qui permet d'arrondire un double a un nombre de décimal souhaité
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
