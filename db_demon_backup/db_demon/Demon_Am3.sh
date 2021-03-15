#!/bin/sh
# Write Log Start

# Log date make

beginTime=`date +%Y%m%d,%H%M%S`

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
#echo "Demon_Am3.java -- Start -- $beginTime " >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_Am3.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_Am3


endTime=`date +%Y%m%d,%H%M%S`
echo "Demon_Am3.java -- End -- $endTime " >> /user/home/mro/db_demon/logs/$v_log_date.log
# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_Am3.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
