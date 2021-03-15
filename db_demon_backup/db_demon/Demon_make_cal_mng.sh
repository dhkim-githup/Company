#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_make_cal_mng.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_make_cal_mng

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_make_cal_mng.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
