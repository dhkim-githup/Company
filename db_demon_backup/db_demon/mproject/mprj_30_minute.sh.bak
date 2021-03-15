#!/bin/sh
# Write Log Start

# Log date make

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/mprj_30_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

echo "sh  wsIF_PR_PROP_REAL : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon/mproject/real/bin
java wsIF_PR_PROP_REAL

echo "sh  : SKT_real " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon/mproject/SKT_real/bin
java SKT_real 


# Write Log End
echo "End /user/home/mro/db_demon/logs/mprj_30_minute.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
