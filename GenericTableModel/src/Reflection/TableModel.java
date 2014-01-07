package Reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})

/**Interface que marca todos que todos atributos da classe devem aparecer na Tabela.
 * Para que todos aparecam na jTable, deve ter ser valor true.
	@author Marcos Vinicius Scholl
*/

public @interface TableModel {
	boolean includeAll() default false;
	
}
