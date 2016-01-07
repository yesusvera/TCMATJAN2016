package br.com.diabetesvirtual.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.diabetesvirtual.R;
import br.com.diabetesvirtual.dao.PerfilDao;
import br.com.diabetesvirtual.model.Perfil;
import br.com.diabetesvirtual.model.PerfilCategoria;
import br.com.diabetesvirtual.model.Sexo;
import br.com.diabetesvirtual.util.ConnectDatabase;
import br.com.diabetesvirtual.util.Formatos;
import br.com.diabetesvirtual.util.Mensagem;

@SuppressLint("SimpleDateFormat")
public class PerfilActivity extends Activity implements TextWatcher, OnCheckedChangeListener{
	Perfil p;
	PerfilDao perfilDao;
	EditText editText_nome;
	EditText editText_altura;
	EditText editText_peso;
	EditText editText_obs;
	EditText edittext_data;
	EditText editText_fator_glic;
	EditText editText_fator_carb;
	TextView idade;
	String current = "";
    String ddmmyyyy = "ddmmyyyy";
    Calendar cal = Calendar.getInstance();
    int day;
    int month;
    int year;
    Mensagem msg;
    RelativeLayout tela1;
    RelativeLayout tela2;
    RelativeLayout tela3;    
 //radioGroup SEXO
    RadioGroup radioGroup;
    RadioButton masc;
    RadioButton fem;
//radioGroup CATEGORIA  
	RadioGroup radioGroup_cat;
    RadioButton cat1;
    RadioButton cat2;
    RadioButton cat3;
    RadioButton cat4;
    RadioButton cat5;
    RadioButton cat6;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perfil_inserir);
		tela1 = (RelativeLayout) findViewById(R.id.perfil_tela1);
		tela2 = (RelativeLayout) findViewById(R.id.perfil_tela2);
		tela3 = (RelativeLayout) findViewById(R.id.perfil_tela3);
		
		editText_nome = (EditText) findViewById(R.id.perfil_nome);
		edittext_data = (EditText) findViewById(R.id.perfil_data_nasc);
		edittext_data.addTextChangedListener(this);
		editText_obs = (EditText) findViewById(R.id.perfil_obs);
		editText_peso = (EditText) findViewById(R.id.perfil_peso);
		editText_altura = (EditText) findViewById(R.id.perfil_altura);
		editText_fator_glic = (EditText) findViewById(R.id.fator_glic_editText);
		editText_fator_carb = (EditText) findViewById(R.id.fator_carb_editText);
		
		
		
		masc = (RadioButton) findViewById(R.id.sexo_m);
	    fem = (RadioButton) findViewById(R.id.sexo_f);
	    radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
	    radioGroup.setOnCheckedChangeListener(this);
	    masc.setText(Sexo.MASCULINO.getNome());
		fem.setText(Sexo.FEMININO.getNome());
		
		radioGroup_cat = (RadioGroup) findViewById(R.id.radioGroup_fator);
		radioGroup_cat.setOnCheckedChangeListener(this);
	    cat1 = (RadioButton) findViewById(R.id.radioButton_cat1);
	    cat2 = (RadioButton) findViewById(R.id.radioButton_cat2);
	    cat3 = (RadioButton) findViewById(R.id.radioButton_cat3);
	    cat4 = (RadioButton) findViewById(R.id.radioButton_cat4);
	    cat5 = (RadioButton) findViewById(R.id.radioButton_cat5);
	    cat6 = (RadioButton) findViewById(R.id.radioButton_cat6);
		
		
		idade = (TextView) findViewById(R.id.idade);
//		if(ConnectionNetwork.verifiedInternetConnection(this)){
		if(LoginActivity.usuarioLogado != null &&
		   LoginActivity.usuarioLogado.getUser() != null &&
		   LoginActivity.usuarioLogado.getUser().getPerfil()!=null){
			this.carregaPerfilOnline();
		}
//		}else{
//			this.carregaPerfilLocal();
//		}
	}
	
	public void salvar(View view) {
		try {
			if (editText_altura.getText().toString().equals("") || editText_peso.getText().toString().equals("") 
					|| year == 0 || editText_fator_carb.getText().toString().equals("") || editText_fator_glic.getText().toString().equals("")
					|| editText_nome.getText().toString().equals(""))   {
				msg = new Mensagem();
				msg.mensagemToast(this, "Preencha os campos obrigatórios do perfil.");
			} else {
				p = Perfil.getPerfil(p);
				p.setNome(editText_nome.getText().toString());
				p.setAltura(Double.parseDouble(editText_altura.getText().toString()));
				p.setObs(editText_obs.getText().toString());
				p.setData_nasc(this.getData());			
				p.setFatorCarboidrato(Double.parseDouble(editText_fator_carb.getText().toString()));
				p.setFatorGlicemia(Double.parseDouble(editText_fator_glic.getText().toString()));
				perfilDao = new PerfilDao(this);
				perfilDao.salvaOuAtualiza(p);
				Intent i = new Intent(this, MetasActivity.class);
				startActivity(i);				
				finish();
				return;
			}			
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao salvar perfil.");
		}
	}
	
	public void voltar(View view) {
		finish();
	}
	
	public void tela1(View view) {
		tela2.setVisibility(View.GONE);
		tela1.setVisibility(View.VISIBLE);
	}
	
	public void tela2(View view) {
		if (tela3.getVisibility() == View.VISIBLE) {
			tela3.setVisibility(View.GONE);
			tela2.setVisibility(View.VISIBLE);
		} else {
			if (editText_peso.getText().toString().equals("")) {
				msg = new Mensagem();
				msg.mensagemToast(this, "Informe o peso para calcular o fator de sensibilidade");
			} else {
				p.setPeso(Double.parseDouble(editText_peso.getText().toString()));
				tela1.setVisibility(View.GONE);
				tela2.setVisibility(View.VISIBLE);
			}
		}		
	}
	
	public void tela3(View view) {
//		if (p.getId() == 0) {
//			this.calcularFator(null);
//		}		
		tela1.setVisibility(View.GONE);
		tela2.setVisibility(View.GONE);
		tela3.setVisibility(View.VISIBLE);	
	}
	
	public void calcularFator(View view) {
		double x=0;
		double fatorGlic=0;
		double fatorCarb=0;
		x = p.getPeso()*0.5;
		fatorGlic = 1800/x;
		fatorCarb = 500/x;
		editText_fator_carb.setText(Formatos.formataDoubleCasaDec(fatorCarb));
		editText_fator_glic.setText(Formatos.formataDoubleCasaDec(fatorGlic));
		p.setFatorCarboidrato(fatorCarb);
		p.setFatorGlicemia(fatorGlic);
	}
	
	public void carregaPerfilOnline() {
//			LoginActivity.usuarioLogado.getUser().getPerfil();
			p = new Perfil();
				editText_nome.setText(LoginActivity.usuarioLogado.getUser().getNome());
				editText_obs.setText(LoginActivity.usuarioLogado.getUser().getPerfil().getObservacao());
				editText_peso.setText(LoginActivity.usuarioLogado.getUser().getPerfil().getPeso().toString());
				editText_altura.setText(LoginActivity.usuarioLogado.getUser().getPerfil().getAltura().toString());
				
				try{
					edittext_data.setText(new SimpleDateFormat("dd/MM/yyyy").
												format(
													LoginActivity.usuarioLogado.getUser().getPerfil().getDataNascimento()
														));
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(LoginActivity.usuarioLogado.getUser().getPerfil().getDataNascimento().getTime());
					idade.setText(Perfil.getIdade(cal)+"");
				}catch(Exception e){
					
				}
				
				editText_fator_glic.setText(Formatos.formataDoubleCasaDec(LoginActivity.usuarioLogado.getUser().getPerfil().getFatorGlicemia()));
				editText_fator_carb.setText(Formatos.formataDoubleCasaDec(LoginActivity.usuarioLogado.getUser().getPerfil().getFatorCarboidrato()));
				if (LoginActivity.usuarioLogado.getUser().getPerfil().getSexo() == 1) {
					masc.setChecked(true);
					p.setSexo(Sexo.MASCULINO);
				} else {
					fem.setChecked(true);
					p.setSexo(Sexo.FEMININO);
				}
				p.setCategoria(PerfilCategoria.CAT_1);
//			else {
//				p.setSexo(Sexo.FEMININO);
//				p.setCategoria(PerfilCategoria.CAT_1);
//			}			
	}
	
	public void carregaPerfilLocal() {
		try {
			p = new Perfil();
			perfilDao = new PerfilDao(this);
			p = perfilDao.getPerfil();
			if (p.getId() != 0) {
				if (p.getNome()==null) {
					p.setNome("");
				}
				editText_nome.setText(p.getNome());
				editText_altura.setText(p.getNome());
				editText_obs.setText(p.getObs());
				editText_peso.setText(p.getPeso()+"");
				editText_altura.setText(p.getAltura()+"");
				edittext_data.setText(this.getDataFormatada(p.getData_nasc()));
				editText_fator_glic.setText(Formatos.formataDoubleCasaDec(p.getFatorGlicemia()));
				editText_fator_carb.setText(Formatos.formataDoubleCasaDec(p.getFatorCarboidrato()));
				if (p.getSexo() == Sexo.FEMININO) {
					fem.setChecked(true);
				} else {
					masc.setChecked(true);
				}
				if (p.getCategoria() == PerfilCategoria.CAT_1) {
					cat1.setChecked(true);
				} else if (p.getCategoria() == PerfilCategoria.CAT_2) {
					cat2.setChecked(true);
					
				} else if (p.getCategoria() == PerfilCategoria.CAT_3) {
					cat3.setChecked(true);
					
				}else if (p.getCategoria() == PerfilCategoria.CAT_4) {
					cat4.setChecked(true);					
				} else if (p.getCategoria() == PerfilCategoria.CAT_5) {
					cat5.setChecked(true);					
				} else {
					cat6.setChecked(true);	
				}				
			} else {
				p.setSexo(Sexo.FEMININO);
				p.setCategoria(PerfilCategoria.CAT_1);
			}
		} catch (Exception e) {
			msg = new Mensagem();
			msg.mensagemToast(this, "Erro ao recuperar perfil.");
		}
	}
		
	//customização do edittext da data
	@Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d.]", "");
            String cleanC = current.replaceAll("[^\\d.]", "");
            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
               clean = clean + "________".substring(clean.length());
               idade.setText("");
               year=0;
            }else{
               //This part makes sure that when we finish entering numbers
               //the date is correct, fixing it otherways
               day  = Integer.parseInt(clean.substring(0,2));
               month  = Integer.parseInt(clean.substring(2,4));
               year = Integer.parseInt(clean.substring(4,8));
               if(month > 12) month = 12;
               cal.set(Calendar.MONTH, month-1);
               day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
               year = (year<1900)?1900:(year>2100)?2100:year;
               clean = Formatos.formataData(year, month, day);
               Calendar x = Calendar.getInstance();
               x.set(year, month-1, day);
               idade.setText(Perfil.getIdade(x)+"");
            }
            clean = String.format(Locale.getDefault(),"%s/%s/%s", clean.substring(0, 2),
            clean.substring(2, 4),
            clean.substring(4, 8));
            current = clean;
            edittext_data.setText(current);
            edittext_data.setSelection(sel < current.length() ? sel : current.length());
        }
    }

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {		
	}

	public String getDataFormatada(Calendar c) {
		String d=c.get(Calendar.DAY_OF_MONTH)+"";
		String m=c.get(Calendar.MONTH)+1+"";
		int day = Integer.parseInt(d);
		int month = Integer.parseInt(m);
		int year = c.get(Calendar.YEAR);
		if (day < 10) {
			d = "0"+day+"";
		}
		if (month < 10) {
			m = "0" + m;
		}
		return d+"/"+m+"/"+year;
	}
	
	public Calendar getData() { //recupera a data do edittext customizado
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day);
		return c;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == radioGroup) {
			switch (checkedId) {
			case R.id.sexo_f:
				p.setSexo(Sexo.FEMININO);
				break;
			case R.id.sexo_m:
				p.setSexo(Sexo.MASCULINO);
				break;
			default:
				p.setSexo(Sexo.MASCULINO);
				break;
			}
		} else {
			switch (checkedId) {
			case R.id.radioButton_cat1:
				p.setCategoria(PerfilCategoria.CAT_1);
				break;
			case R.id.radioButton_cat2:
				p.setCategoria(PerfilCategoria.CAT_2);
				break;
			case R.id.radioButton_cat3:
				p.setCategoria(PerfilCategoria.CAT_3);
				break;
			case R.id.radioButton_cat4:
				p.setCategoria(PerfilCategoria.CAT_4);
				break;
			case R.id.radioButton_cat5:
				p.setCategoria(PerfilCategoria.CAT_5);
				break;
			case R.id.radioButton_cat6:
				p.setCategoria(PerfilCategoria.CAT_6);
				break;
			}
		}	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, getResources().getString(R.string.b_backup));
		menu.add(0, 1, 1, getResources().getString(R.string.b_restaurar));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				AlertDialog.Builder msg1 = new AlertDialog.Builder(this);
				msg1.setTitle("Back-up");
				msg1.setMessage(getResources().getString(R.string.backup_info));
				msg1.setNeutralButton("Cancelar", null);
				msg1.setNegativeButton("OK", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						backup();
					}
				});
				msg1.show();	
				break;
			case 1:	
				AlertDialog.Builder msg2 = new AlertDialog.Builder(this);
				msg2.setTitle("Restaurar Back-up");
				msg2.setMessage(getResources().getString(R.string.restaurar_info));
				msg2.setNeutralButton("Não", null);
				msg2.setNegativeButton("Sim", new DialogInterface.OnClickListener() {			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						restaurar();
						finish();
						return;
					}
				});
				msg2.show();	
				break;
		}
		return true;
	}
	
	// metodos dos relatorios do BACKUP
	private void backup() {
		ConnectDatabase connectDatabase = new ConnectDatabase(this);
		connectDatabase.backUp();
	}
	
	private void restaurar() {
		ConnectDatabase connectDatabase = new ConnectDatabase(this);
		connectDatabase.restore();
	}
}
