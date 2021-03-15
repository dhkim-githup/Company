#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Search_Word_Popular_Level.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Search_Word_Popular_Level

# Write Log End
echo "End /user/home/mro/db_demon/logs/Search_Word_Popular_Level.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
