#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_4_aday.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_4_aday

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_4_aday.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log