# Comparación del rendimiento de segmentación de palabras entre HanLP y LTP
En este estudio, se han utilizado HanLP 1.6.0 y LTP 3.4.0 para la comparación.
## Método de prueba
Para evaluar el rendimiento, se entrenaron dos bibliotecas de segmentación de palabras utilizando el mismo corpus y se probaron con el mismo conjunto de datos de prueba.
El corpus seleccionado fue el de People's Daily correspondiente al mes de enero de 1998: [Corpus de People's Daily de 1998-01](https://github.com/hankcs/OpenCorpus/blob/master/pku98/199801.txt).
Este corpus incluye etiquetas de clasificación gramatical, por lo que es necesario eliminarlas para adaptar el formato al conjunto de datos de entrenamiento de LTP.
El conjunto de datos de prueba utilizado es el conjunto de datos abierto proporcionado por [SIGHan2005](http://sighan.cs.uchicago.edu/bakeoff2005/).
Para utilizar SIGHan2005, consulte el archivo readme adjunto.

## HanLP

```bash
java -cp libs/hanlp-1.6.0.jar com.hankcs.hanlp.model.perceptron.Main -task CWS -train -reference ../OpenCorpus/pku98/199801.txt -model cws.bin

mkdir -p data/model/perceptron/pku199801

mv -f cws.bin data/model/perceptron/pku199801/cws.bin

```
De forma predeterminada, el número de iteraciones de entrenamiento es 5.

Modifique el archivo src/main/resources:
```bash
root=../test-hanlp-ltp
```

Comando de empaquetado:

```groovy
gradle clean build 
```

​ 
### Conjunto de pruebas MSR de SIGHan2005

Ejecute el siguiente comando:
```bash
java -cp build/libs/test-hanlp-ltp-1.0-SNAPSHOT.jar  com.zongwu33.test.TestForSIGHan2005 ../NLP/icwb2-data/testing/msr_test.utf8    segment-msr-result.txt

```
Los resultados de la segmentación se guardarán en el archivo `segment-msr-result.txt`.
Genere las puntuaciones utilizando el script de SIGHan2005:
```perl
perl ../NLP/icwb2-data/scripts/score ../NLP/icwb2-data/gold/msr_training_words.utf8 \
    ../NLP/icwb2-data/gold/msr_test_gold.utf8 segment-msr-result.txt > score-msr.utf8
```
Los resultados de HanLP en el conjunto de datos MSR son los siguientes:
```
=== TOTAL TRUE WORDS RECALL: 0.870
=== TOTAL TEST WORDS PRECISION: 0.848
=== F MEASURE: 0.859
```
### Conjunto de pruebas PKU de SIGHan2005

```bash
java -cp build/libs/test-hanlp-ltp-1.0-SNAPSHOT.jar  com.zongwu33.test.TestForSIGHan2005 ../NLP/icwb2-data/testing/pku_test.utf8  segment-pku-result.txt

```
```bash
perl ../NLP/icwb2-data/scripts/score ../NLP/icwb2-data/gold/pku_training_words.utf8  ../NLP/icwb2-data/gold/pku_test_gold.utf8   segment-pku-result.txt > score-pku.utf8

```
Resultados:
```
=== TOTAL TRUE WORDS RECALL: 0.894
=== TOTAL TEST WORDS PRECISION: 0.915
=== F MEASURE: 0.905
```


## Instalación de LTP en Docker

## LTP
Genere un archivo de corpus de entrenamiento en formato compatible con LTP:
```bash
java  -cp build/libs/test-hanlp-ltp-1.0-SNAPSHOT.jar  com.zongwu33.test.CreateSimpleCorpus ../OpenCorpus/pku98/199801.txt  simple-199801.txt

```
El archivo `simple-199801.txt` es el resultado.
Tanto el conjunto de entrenamiento como el conjunto de desarrollo se especifican como este archivo:

```bash
../LTP/ltp-3.4.0/tools/train/otcws learn  --model model-test --reference simple-199801.txt --development simple-199801.txt  --max-iter  5
```
### Conjunto de pruebas MSR de SIGHan2005

Prueba:
```bash
../LTP/ltp-3.4.0/tools/train/otcws test  --model model-test  --input /data/testLTP/icwb2-data/testing/msr_test.utf8  > msr_result.txt
```
Genere las puntuaciones utilizando el script de SIGHan2005:
```bash
perl icwb2-data/scripts/score icwb2-data/gold/msr_training_words.utf8 \
    icwb2-data/gold/msr_test_gold.utf8 msr_result.txt > ltp-msr-score.utf8
```
Consulte el archivo `ltp-msr-score.utf8`:

```bash
=== TOTAL TRUE WORDS RECALL: 0.886
=== TOTAL TEST WORDS PRECISION: 0.854
=== F MEASURE: 0.870
```
### Conjunto de pruebas PKU de SIGHan2005

```bash
../LTP/ltp-3.4.0/tools/train/otcws test  --model model-test  --input /data/testLTP/icwb2-data/testing/pku_test.utf8  > pku_result.txt
```
```bash
perl icwb2-data/scripts/score icwb2-data/gold/pku_training_words.utf8  \
    icwb2-data/gold/pku_test_gold.utf8  pku_result.txt > ltp-pku-score.utf8

```

```
=== TOTAL TRUE WORDS RECALL: 0.928
=== TOTAL TEST WORDS PRECISION: 0.939
=== F MEASURE: 0.934
```
## Comparación

### Conjunto de pruebas MSR

|     | Iteraciones | RECALL | PRECISION | F1    |
|-----|------------|--------|-----------|-------|
|HanLP|5          | 0.870  | 0.848     | 0.859 |
|LTP  |5          | 0.886  | 0.854     | 0.870 |
|HanLP|50         | 0.881  | 0.855     | 0.868 |
|LTP  |50         | 0.888  | 0.859     | 0.873 |


### Conjunto de pruebas PKU

|     | Iteraciones | RECALL | PRECISION | F1    |
|-----|------------|--------|-----------|-------|
|HanLP|5          | 0.894  | 0.915     | 0.905 |
|LTP  |5          | 0.928  | 0.939     | 0.934 |
|HanLP|50         | 0.908  | 0.922     | 0.915 |
|LTP  |50         | 0.931  | 0.946     | 0.939 |


### Pruebas de rendimiento

Configuración del servidor Alibaba Cloud ECS:

Procesador: Intel Xeon CPU *4 2.50GHz, Memoria: 16G.

Conjunto de datos de prueba: 20 MB de novela en línea, aproximadamente 140.315 oraciones (sin contar las líneas en blanco).

#### HanLP
```bash
java -cp test-hanlp-ltp-1.0-SNAPSHOT.jar com.zongwu33.test.PerformanceTest  ../NLP/strict-utf8-booken.txt  
```
```
init model: 313 ms
tiempo total: 15677 ms 
número total: 140315 
```
Se necesitan 15.677 segundos, lo que corresponde a una velocidad de procesamiento de 1.375 millones de caracteres por segundo.

#### LTP
```bash

../LTP/ltp-3.4.0/tools/train/otcws test  --model model-test  --input  strict-utf8-booken.txt  > /dev/null
```
```bash

[INFO] 2018-03-26 17:04:19 ||| segmentador LTP, probando ...
[INFO] 2018-03-26 17:04:19 informe: archivo de entrada = strict-utf8-booken.txt
[INFO] 2018-03-26 17:04:19 informe: archivo de modelo = model-test
[INFO] 2018-03-26 17:04:19 informe: evaluación = falso
[INFO] 2018-03-26 17:04:19 informe: probabilidad de secuencia = falso
[INFO] 2018-03-26 17:04:19 informe: probabilidad marginal = falso
[INFO] 2018-03-26 17:04:19 informe: número de etiquetas = 4
[INFO] 2018-03-26 17:04:19 informe: número de características = 491820
[INFO] 2018-03-26 17:04:19 informe: número de dimensiones = 1967296
[INFO] 2018-03-26 17:05:13 Tiempo transcurrido 53.680000
```
Se necesitan 53 segundos, lo que corresponde a una velocidad de procesamiento de 389.000 caracteres por segundo.

### Comparación

|       | Tiempo total     | Velocidad      |
| ----- | ------- | ------- |
| HanLP | 15.68 s | 1.375 millones de caracteres/s |
| LTP   | 53.68 s | 389.000 caracteres/s |

## Licencia de código abierto
**Licencia Apache Versión 2.0**
## Agradecimientos
[HanLP](https://github.com/hankcs/HanLP)
[LTP](https://github.com/HIT-SCIR/ltp)
