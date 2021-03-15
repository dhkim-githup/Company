#!/bin/bash
export LANG=ko
# Write Log Start
# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/FimsDemonProc.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon/skt_fims

# 1. Excute Send Demon 
#echo "Start skt_fims.FimsSendDemon : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
#java -Dfile.encoding=UTF-8 skt_fims.FimsSendDemon

# 2. Excute Send Demon 
echo "Start skt_fims.FimsSendDemon10min : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
java -Dfile.encoding=UTF-8 skt_fims.FimsSendDemon10min

# Write Log End
echo "End /user/home/mro/db_demon/logs/FimsDemonProc.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
