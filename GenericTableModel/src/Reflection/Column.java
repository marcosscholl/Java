package Reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})


/**Interface que marca com annotações quais atributos devem aparecer na Tabela.
 * 
	@author Marcos Vinicius Scholl


*/

/**Interface que marca com annotações quais atributos devem aparecer na Tabela.
 	colPosition() = Posição na tabela. pode ser omitido, ficando a posição conforme a ordem de atributos da classe utilizada.
 	colName() = Nome da Coluna do Atributo. Pode ser omitido, ficando padrão o nome da coluna como nome do atributo.
 	formatter() = Por padrão, sempre é visto na tabela como uma String. Permite Formatação. Ex: (formatter="R$ %,#.2f") saida = (R$ 21,50)
*/
public @interface Column {
	int colPosition() default -1;
	String colName() default "";
	String formatter() default "%s";
}
