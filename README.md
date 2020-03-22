# MemeStorage
The best storage of your memes

## [Eng]
### What is it?
MemeStorage allows to save all your funny picturs in one place.
To add a picture to the collection, just save it to the clipboard and click on the button “Add image from clipboard”
To get a picture from the collection, click on its thumbnail. Then the picture will be copied to the clipboard.
![Camera sevtors](interface.png)

When closing, MemeStorage will be minimized to tray to allow the user to quickly open the collection if necessary.

### Future updates
Future updates to MemeStorage will add:
- the ability to advertise to tag images, for quick search;
- the ability to import a collection;
- Hot keys for faster user interaction;
- Create your own language packs.

### Build
```
javac -sourcepath ./src -d out/production/MemeStorage src/MemeStorage.java
jar -cmf manifest.mf MemeStorage.jar -C out/production/MemeStorage .
```

### Run
```
java -jar MemeStorage.jar
```
Or double click on .jar file.


## [Ru]
### Что это?
MemeStorage позволяет хранить все мемасики и смешные картинки в одном месте.
Чтобы добавить картинку в коллекцию достаточно сохранить ее в буфер обмена и нажать на кнопку "Add image from clipboard"
Чтобы получить картинку из коллекции необходимо кликнуть на ее миниатюру. Тогда картинка будет скопированна в буфер обмена.
![Camera sevtors](interface.png)

При закрытии MemeStorage свернется в трей, чтобы дать пользователю быстро открыть коллекцию при необходимости.

### Будущие обновления
В будущих обновлениях MemeStorage добавятся:
- Возможность добавлять к картинкам теги для быстрого поиска;
- Возможность импортировать коллекции;
- Горячие клавиши для более быстрого взаимодействия с пользователем;
- Создание своих языковых пакетов.

### Сборка
```
javac -sourcepath ./src -d out/production/MemeStorage src/MemeStorage.java
jar -cmf manifest.mf MemeStorage.jar -C out/production/MemeStorage .
```

### Запуск
```
java -jar MemeStorage.jar
```
Или двойной клик по .jar файлу.
