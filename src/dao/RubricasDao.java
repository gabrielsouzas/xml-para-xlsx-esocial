package dao;

import view.TelaLerXML;

public class RubricasDao {

	
	// Ralatório de Rubricas Datasul - FP0021
		// Parametros 
		//      >> Marca -> ExportaCSV
		//                  Imprime Complemento eSocial
		public void popularListaRubricas(){
			TelaLerXML.rubricas.add("100-001;Hrs Normais Diurnas;1000;11;11;11;11");
			TelaLerXML.rubricas.add("100-002;Valor devido ao Prestador;3501;0;0;0;0");
			TelaLerXML.rubricas.add("100-003;Hrs Normais Noturnas;1000;11;11;11;11");
			TelaLerXML.rubricas.add("100-004;Hrs Abonadas Diurnas;1099;11;11;11;11");
			TelaLerXML.rubricas.add("100-005;Hrs Abonadas Noturnas;1099;11;11;11;11");
			TelaLerXML.rubricas.add("100-006;Ferias Prop Adic - Aviso Inden;6006;0;79;0;0");
		}
}
