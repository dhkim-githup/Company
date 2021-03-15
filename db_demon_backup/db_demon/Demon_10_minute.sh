#!/bin/sh
# Write Log Start

# Log date make
# 2020.01.30 New comile : javac -encoding euc-kr Demon_10_minute_v2.java 

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_10_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
#java Demon_10_minute
java Demon_10_minute_v2

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_10_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
