#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_asp_comp_20m.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_asp_comp_20m

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_asp_comp_20m.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
