#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/GW_TO_METS.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java -cp .:com.mysql.jdbc_5.1.5.jar:commons-lang-2.6.jar:tika-app-1.5.jar GW_TO_METS

# Write Log End
echo "End /user/home/mro/db_demon/logs/GW_TO_METS.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log


# How to Compile Java
# javac -cp .:tika-app-1.5.jar:commons-lang-2.6.jar:SeedCipher:CryptoPadding:ZeroPadding GW_TO_METS.java
