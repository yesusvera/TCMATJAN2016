package br.com.docedesafio.relatorios;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class RelatorioExercicios {
 
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SQLException, JRException, ClassNotFoundException, FileNotFoundException {
		Class.forName("org.postgresql.Driver");
		Connection connection = new ConnectionFactory().getConnection();
		JasperCompileManager.compileReportToFile("reports/exercicios.jrxml");
		Map<String, Object> params = new HashMap<String, Object>();
		JasperPrint jasperPrint = JasperFillManager.fillReport("reports/exercicios.jasper", params, connection);
		JRExporter exporter = new JRPdfExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("relatorio_exercicios.pdf"));
		exporter.exportReport();
		connection.close();
		
	}
	
}
