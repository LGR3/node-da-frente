# Source Server
Servidor que usa o esper para filtrar o csv, e então se inscreve em 'bus' no broker rabbitmq e publica os ônibus que estão em 'bus' e existem no csv em tópicos com os mesmos nomes dos ônibus

## Run
<pre> CSVPATH="local/nome.csv" ./gradlew run </pre>
* O local do arquivo CSV deve estar definido na variável CSVPATH

Também pode-se definir o endereço do broker na variável RABBITMQADDR, o valor default é 'localhost'. A porta deve ser a 5672.