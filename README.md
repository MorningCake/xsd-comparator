## Инструкция

### Запуск через UI
Запустить [UiMain.java](src/main/java/ru/alfabank/epk/reactive/ui/UiMain.java) - откроется такой UI :-) <br>
![img_1.png](img_1.png)
<br><br>В результате выполнения получим файлы, которые можно сохранить на диск:<br>
![img_2.png](img_2.png) <br>
![img_3.png](img_3.png)

### Запуск алгоритмов
1) xsd-схемы - положить в папку [resources](src/main/resources)
2) сгенерированные файлы будут в папке [generated](src/main/resources/generated). `Сгенерированные файлы можно удалять, нельзя удалять саму папку!`
3) режим парсинга одной схемы - [ParserMain.java](src/main/java/ru/alfabank/epk/reactive/ok/ParserMain.java)
   <br>Вписать название схемы и выходного файла<br>
```java
      saxReader.readXsd("название схемы.xsd",
                true, true, true, true, false, "название выходного файла без расширения");
```
4) запустить зеленой стрелкой, дождаться появления файлов в папке [generated](src/main/resources/generated) <br>
![img.png](img.png)
5) режим сверки двух схем - [ComparatorMain.java](src/main/java/ru/alfabank/epk/reactive/ok/ComparatorMain.java)
   <br>Вписать название схем и выходных файлов<br>
```java
        Path path1 = saxReader.readXsd("название схемы №1.xsd",
            false, true, false, false, false, "название выходного файла №1 (без расширения)");

        //...

        Path path2 = saxReader.readXsd("название схемы №2.xsd",
                false, true, false, false, false, "название выходного файла №2 (без расширения)");
```
6) запустить аналогично п.4 из ComparatorMain