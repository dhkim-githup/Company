#!/bin/sh
# Write Log Start

# Log date make
# New Image java compile : javac -encoding euc-kr Demon_5_minute_v2.java

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_5_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
#java Demon_5_minute
java Demon_5_minute_v2

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_5_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
