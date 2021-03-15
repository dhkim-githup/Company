
#!/bin/sh

# Log date make
v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/skgp/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/skgp/logs/SET_NARAE_SKGP.sh : " `date` >> /user/home/mro/db_demon/skgp/logs/$v_log_date.log



#Compile
cd /user/home/mro/db_demon/skgp

javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar SET_NARAE_COMMCODE.java
javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar SET_NARAE_EMPLOYEE.java
javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar SET_NARAE_EMPORDER.java
javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar SET_NARAE_ORGANIZATION.java

javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar GET_NARAE_PHOTO.java
javac -cp .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar PUT_NARAE_PHOTO.java


#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" SET_NARAE_COMMCODE.java
#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" SET_NARAE_EMPLOYEE.java
#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" SET_NARAE_EMPORDER.java
#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" SET_NARAE_ORGANIZATION.java

#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" GET_NARAE_PHOTO.java
#javac -cp ".;\user\home\mro\db_demon\skgp\COMM;jsch-0.1.49.jar;jsch-0.1.55.jar;ojdbc14-10.2.0.4.0.jar;commons-net-3.3.jar;" PUT_NARAE_PHOTO.java



# Excute Java
cd /user/home/mro/db_demon/skgp
java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  SET_NARAE_COMMCODE
java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  SET_NARAE_EMPLOYEE
java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  SET_NARAE_EMPORDER
java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  SET_NARAE_ORGANIZATION

java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  GET_NARAE_PHOTO
java -cp  .:COMM:jsch-0.1.49.jar:jsch-0.1.55.jar:ojdbc14-10.2.0.4.0.jar:commons-net-3.3.jar  PUT_NARAE_PHOTO


# Write Log End
echo "End /user/home/mro/db_demon/skgp/logs/SET_NARAE_SKGP.sh : " `date` >> /user/home/mro/db_demon/skgp/logs/$v_log_date.log



