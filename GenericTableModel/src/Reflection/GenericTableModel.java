package Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**Classe que gera tabela Dinamica apartir de Reflection e Annotation.
 * 
	@author Marcos Vinicius Scholl
	@version 1.00
 */

public class GenericTableModel extends AbstractTableModel {


	private static final long serialVersionUID = 1L;
	
	private List<?> list;
	private Class<?> clazz;
	private String fieldName;

	public GenericTableModel(List<?> lista, Class<?> classe) {
		this.list = lista;
		this.clazz = classe;
		
		if (lista == null) throw new NullPointerException("List can not be null!");
		if (classe == null) throw new NullPointerException("Class can not be null!");
	}

	/**Retorna o número de colunas no modelo. A JTable usa esse método para determinar quantas colunas ele deve criar e exibir por padrão.
	 * @return int - O número de colunas no modelo
	 */
	@Override
	public int getColumnCount() {
		int colunas = 0;
		TableModel tm = clazz.getAnnotation(TableModel.class);
		boolean includeAll = tm.includeAll();
		
		for (Field field : clazz.getDeclaredFields()) {
			if (includeAll) {
				if (!field.isAnnotationPresent(ColumnIgnore.class)) {
					colunas++;
				} 
			} else {
				if (field.isAnnotationPresent(Column.class)) {
					colunas++;
				}
			}
		}
		return colunas;
	}

	/**Retorna o número de linhas no modelo. A JTable usa esse método para determinar quantas linhas ele deve exibir. 
	 * Este método deve ser rápido, como é chamado com freqüência durante o processamento.
	 * @return int - O número de linhas no modelo
	 */
	@Override
	public int getRowCount() {
		return list.size();
	}

	/**Retorna o valor para a célula na columnIndex e rowIndex .
	 * @param  rowIndex int - a linha cujo valor deve ser consultado.
	 * @param columnIndex int - a coluna cujo valor deve ser consultado.
	 * @return Object - O objeto de valor na célula especificada.
	 */
	
	private ArrayList<Field> listaFields(){
		ArrayList<Field> fields = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields() ) {	
			if (!field.isAnnotationPresent(ColumnIgnore.class)) {
			fields.add(field);
			}	
		}
		return fields;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			int beforePosition = -1, nowPosition = -1, newPosition = -1;
			Object object = list.get(rowIndex);
			ArrayList<Field> fields = listaFields();
			
			TableModel tm = clazz.getAnnotation(TableModel.class);
			boolean includeAll = tm.includeAll();
			for (Field field : clazz.getDeclaredFields() )
			{ 	
				Column c = field.getAnnotation(Column.class);
				
				if (includeAll) {
					if (fields.get(columnIndex).equals(field)) {
						field.setAccessible(true);
						return field.get(object);
					}
				} else {
					field.setAccessible(true);
					
					if (field.isAnnotationPresent(Column.class)) {
						nowPosition = c.colPosition();
						
						if(beforePosition == nowPosition) {
							newPosition++;
							nowPosition = newPosition;
						}
						
						if ( c != null && nowPosition == columnIndex) {
							if (field.getName().equalsIgnoreCase(field.getName())) {
								return String.format(c.formatter(), 
						                  field.get(object));
							}
						}
					}
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**Retorna o nome da coluna no columnIndex . Este é usado para inicializar o nome do cabeçalho da coluna da tabela. 
	   Nota: este nome não precisa ser exclusivo, duas colunas de uma tabela pode ter o mesmo nome.
	 * @param  column int -  o índice de coluna
	 * @return String - O nome da coluna.
	 */
	@Override
	public String getColumnName(int column) {
		//preencheNome
		int beforePosition = -1, nowPosition = -1, newPosition = -1;
		ArrayList<Field> fields = listaFields();
		TableModel tm = clazz.getAnnotation(TableModel.class);
		boolean includeAll = tm.includeAll();

		
		for (Field field : clazz.getDeclaredFields()) {
			
			Column c = field.getAnnotation(Column.class);
			
			if (includeAll) {
				if (!field.isAnnotationPresent(ColumnIgnore.class) && includeAll) {
					
					if (fields.get(column).equals(field)) {
						return field.getName();
					}
				}
			} else {
				if (field.isAnnotationPresent(Column.class)) {
					
					nowPosition = c.colPosition();
					if(beforePosition == nowPosition) {
						newPosition++;
						nowPosition = newPosition;
					}
					
					
					if ((c != null && nowPosition == column)) {
						if (c.colName().equals("")) {
							return field.getName();
						} else {
							return c.colName();
						}
					}
				}
			}
			
		}
		

		return "";
	}

	/**Define o valor da célula em columnIndex e rowIndex para value .
	 * @param  value Object- O novo valor.
	 * @param  rowIndex int - A linha cujo valor deve ser consultado.
	 * @param columnIndex int - A coluna cujo valor deve ser consultado.
	 */
	public void setValueAt(Object value, int rowIndex, int 
			columnIndex){ 
		int pos = -1;
		try {
			Object object = list.get(rowIndex);
			proximo : for (Field field : clazz.getDeclaredFields()) {
				Column c = field.getAnnotation(Column.class);
				TableModel tm = field.getAnnotation(TableModel.class);
				
				if ((c != null || tm != null || ++pos == columnIndex )) {					
					for (Method method : clazz.getDeclaredMethods()) {
						if (method.getName().equalsIgnoreCase("set"+field.getName())) {
							method.invoke(object,value);
							continue proximo;
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void addItem(){
		int line = list.size()-1;
		fireTableRowsInserted(line,line);
	}

	public Object loadItem(int row){
		return list.get(row);
	}

	public void deleteItem(int row){
		fireTableRowsDeleted(row, row);
		list.remove(row);
	}

	public String getName() {
		return fieldName;
	}
}
