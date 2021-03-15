#!/bin/sh
# Write Log Start

# Log date make
# New Image java compile : javac -encoding euc-kr Demon_sell_buy_dati.java

v_log_date=`date +20%y%m%d`
echo " "  >> /user/home/mro/db_demon/logs/$v_log_date.log
echo "Start /user/home/mro/db_demon/logs/Demon_sell_buy_dati.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log

# Excute java demon,
cd /user/home/mro/db_demon
java Demon_sell_buy_dati

# Write Log End
echo "End /user/home/mro/db_demon/logs/Demon_sell_buy_dati.sh : " `date` >> /user/home/mro/db_demon/logs/$v_log_date.log
