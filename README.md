Generic Tabl eModel with Reflection and Annotation
====
GenericTableModel com Reflection e Annotation

GENERICTABLEMODEL COM REFLECTION E ANNOTATIONS
Vou falar um pouco sobre TableModel, Reflection e Annotation.

O que é o TableModel? 
A maioria dos componentes swing possuem uma arquitetura que separa model e view, de forma que é definida uma interface para cada model. Dentro deste contexto, TableModel é a interface que representa o model da JTable. Se não informarmos qual será o model, a JTable usará por padrão uma instância de DefaultTableModel.

Ai que muitas discussões ocorrem. A quem diga para nunca usar DefaultTableModel, alguns já garantiram a sua morte, mas como poucos tirariam tempo para desenvolver o seu TableModel muitos o utilizam e não veem problema nisso. Mas uma coisa é certa, a complexidade de manter um DefaultTableModel é grande.  

Segundo VinnyGodoy, moderador do maior forum java do Brasil(www.guj.com.br), esses seriam os argumentos para não usar esse modelo.

Não use DefaultTableModel!!!!
Motivos: 
?
1. É mais difícil que escrever seu próprio TableModel;
2. É mais lento (usa classes sincronizadas);
3. Ocupa mais espaço em memória (duplica seus dados);
4. Deixa o código mais confuso e difícil de manter;
5. Usa casts inseguros;
6. Força que você tenha que exibir informações desnecessárias (como o ID) na tabela, ou controlar o ID numa lista separada;
7. Faz a sua mulher te deixar, o leite da sua geladeira azedar, e pessoas apontarem o dedo para você na rua;
?
 
?
 
Por que a JTable precisa de um TableModel?
A JTable chama os métodos do TableModel para obter informações sobre o conteúdo da tabela, como número de linhas e colunas, conteúdo de cada célula, etc.

Qual o papel da classe AbstractTableModel? 
AbstractTableModel é uma classe abstrata que oferece a implementação de alguns métodos da interface TableModel, além de um conjunto de métodos úteis. Portanto o usual é estender esta classe ao invés de implementar TableModel diretamente.

Ok, entendi. Tenho que Criar meu próprio TableModel extendendo a classe AbstractTableModel e cumprindo o contrato sobrescrevendo seus métodos. Mas a onde entra Reflection nisso?

Bom, se você assim como eu, também não tinha ideia de o que era Reflection e como ela funciona, posso garantir, e mais fácil do que parece.

Por que Reflection? 
Java permite que, enquanto escrevemos nosso código, tenhamos total controle sobre exatamente o que vai ser executado, o que faz parte do nosso sistema.
Em tempo de desenvolvimento, olhando nosso código, sabemos quantos atributos uma classe tem, quais métodos ela tem, qual chamada de método está sendo feita e assim por diante.
O que a reflection faz é isso. Olhar para dentro de nossa classe e descobrir seu conteúdo.

O ponto de partida de reflection é a classe "Class".
Através dela conseguimos obter informações como os atributos dela, os métodos, os construtores etc.

Reflection é um pacote do Java que permite chamadas dinâmicas em tempo de execução sem precisar conhecer as classes e objetos envolvidos quando escrevemos nosso código (tempo de compilação).

É ideal para resolvermos determinadas coisas que nosso programa só descobre quando realmente estiver o rodando.

Neste caso, ao criar uma GenericTableModel, ela deve estar pronta a receber qualquer list("Genérico, não?") como parâmetro e mostrar a partir de uma jTable seus dados contidos.
O TableModel não sabe que atributos tem esse list, mas através de reflection, ela consegue "descobrir" esses campos e gerar a jTable.

É possível fazer muitas coisas usando reflection. Apenas para citar algumas possibilidades:
* Listar todos os atributos de uma classe e pegar seus valores em um objeto específico;
* Instanciar classes cujo nome só vamos conhecer em tempo de execução;
* Invocar métodos dinamicamente baseado no nome do método como String;
* Descobrir se determinados pedaços do código foram anotados com as anotações do Java 5.

Ex: 
?
public class Pessoa {
 private String nome;
 private String email;
 private int idade;
 // Getters, setters, métodos de pessoa...
}
?
 
?
Class<pessoa> classe = Pessoa.class;
for (Field field : classe.getDeclaredFields()) {
System.out.println(field.getName());
}</pessoa>

A saída Será: 
?
nome
email
idade
?
 
Dessa maneira, posso deixar o TableModel Genérico.
A cada execução, a cada classe distinta, ele descobre seus atributos e age sobre eles da mesma forma. 

Ok. Funcional. Mas e se eu quiser que meu GenericTabelModel não atue sobre todos os atributos, como eu faço para "marcar" um atributo ignorado? Como eu Faço para marcar apenas o Atributo que quero que apareça na jTable?

Bom, e ai que entra as Annotations. 
Não vou explicar o funcionamento de annotation, ja que o colega Bruno Lima, fez um post sobre o seu funcionamento. (@Annotations "metadados" que não interferem diretamente no código anotado) 

Posso criar anotações e usar na minha classe, onde em tempo de execução e de acordo com a annotation criada o programa age da maneira que eu especificar. 

No casso do GenericTableModel trabalhei com as Seguintes Annotations: 
@TableModel - Anotação de classe, atua sobre todos os seus atributos, coloca todos na jTable.
@ColumnIgnore - Anotação de campo. o Atributo que possuir essa anotação, não irá aparecer na jTable.
@Column - Anotação de Campo e Metodos. O atributo que possuir essa anotação vai aparecer na jTable.     * colName() = Pode-se alterar o nome da coluna, 
    * colPosition() = Posição de coluna na jTable 
    * formatt() = formatar um campo, sendo sua saida uma String. 


Funcionamento: 
?
GenericTableModel tableModel = new GenericTableModel(getData(), 
?
            Pessoa.class);
<span class="Apple-tab-span" style="white-space: pre;"> </span>JTable table = new JTable(tableModel);
<span class="Apple-tab-span" style="white-space: pre;"> </span>JFrame frame = new JFrame("GenericTableModel");
<span class="Apple-tab-span" style="white-space: pre;"> </span>table.setModel(tableModel);
 
 
 
private List<copyofpessoa> getData() {
List<pessoa> list = new ArrayList<pessoa>();
list.add(new Pessoa("Marcos", "viny_scholl@hotmail.com", 21, "xxxxx", 1000.5));
list.add(new Pessoa("Marco", "tony_scholl@hotmail.com", 21, "bbbbbb", 21.75 ));
list.add(new Pessoa("Felipe", "felipe_scholl@hotmail.com", 27, "qwert", 75.40));
return list;
}
</pessoa></pessoa></copyofpessoa>
?
<copyofpessoa><pessoa><pessoa>
</pessoa></pessoa></copyofpessoa>
Exemplos de Execução da GenericTableModel com diferentes Annotations.
Ex1:
?
@TableModel(includeAll=true)
public class CopyOfPessoa {
 private String nome;
 private String email;
 @ColumnIgnore
 private int idade;
 private String senha;
 private double saldo;
}
Saída:  


 ----------------------------------------
Ex2:
  
?
@TableModel(includeAll=true)
public class CopyOfPessoa {
 @ColumnIgnore
 private String nome;
 @ColumnIgnore
 private String email;
 @ColumnIgnore
 private int idade;
 @ColumnIgnore
 private String senha;
 private double saldo;
}
Saída: 


----------------------------------------
Ex3:
  
?
@TableModel(includeAll=false)
public class CopyOfPessoa {
 
 private String nome;
 @Column
 private String email;
 private int idade;
 @Column
 private String senha;
 @Column
 private double saldo;
}
Saída: 


---------------------------------------- 
Ex4:
Com a Formatação possivel Pelo @Column
  
?
@TableModel(includeAll=false)
public class CopyOfPessoa {
 
 @Column(colName = "Nome", colPosition = 0)
 private String nome;
 @Column(colName = "Email", colPosition = 1)
 private String email;
 @Column(colName = "Idade", colPosition = 4)
 private int idade;
 @Column(colName = "Pass", colPosition = 3)
 private String senha;
 @Column(colName = "Saldo", colPosition = 2, formatter="R$ %,#.2f")
 private double saldo;
}
Saída: 




Assim, posso trabalhar de diferentes formas e com diferentes resultados de saída, a partir de uma unica classe usando Reflection e Annotations. 

Temos uma tabela Dinâmica. Uma GenerictableModel.

Referencias:
http://www.guj.com.br/java/199067-redimensionar-jtable---pra-variar--resolvido-/2#1001295
http://www.guj.com.br/java/149861-recuperar-id-de-jtable/2#813171
http://devsv.wordpress.com/2012/07/08/como-implementar-um-tablemodel/
http://markytechs.wordpress.com/2009/05/29/objecttablemodel/
http://www.guj.com.br/java/231928-morte-definitiva-ao-default-table-model#1399642
Caelum FJ-16 -Laboratório Java com Testes, XML e Design Patterns
