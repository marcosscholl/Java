package Reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})

/**Interface que marca com annotações quais atributos não devem aparecer na Tabela.
 * Só pode ser usada, uma vez que a annotation TableMode, esta com valor false.
	@author Marcos Vinicius Scholl


*/

public @interface ColumnIgnore {

}
