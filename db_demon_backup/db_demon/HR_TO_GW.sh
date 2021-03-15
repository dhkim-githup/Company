#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/HR_TO_GW.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log


# Excute java demon,
cd /user/home/mro/db_demon
java -cp .:*.jar HR_TO_GW3 

# Write Log End
echo "End /user/home/mro/db_demon/logs/HR_TO_GW.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log


# How to Compile Java
# javac -cp .:com.mysql.jdbc_5.1.5.jar:commons-lang-2.6.jar:jmimemagic-0.1.0.jar:jsch-0.1.54.jar:jsch-0.1.54.jar:tika-app-1.5.jar:SeedCipher:CryptoPadding:ZeroPadding HR_TO_GW3.java
