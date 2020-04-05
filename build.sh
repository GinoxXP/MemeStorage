javac -sourcepath ./src -d out/production/MemeStorage src/MemeStorage.java
jar -cmf manifest.mf MemeStorage.jar -C out/production/MemeStorage . 
