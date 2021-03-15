#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_ebill_file_trans.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_ebill_file_trans

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_ebill_file_trans.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
