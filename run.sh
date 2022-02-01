#!/bin/sh

NCUSTOMERS="3"
NCRAFTSMAN="3"
PRODUCTCOST="1"
MATERIALDELIVERYSIZE="10"
BATCHCAPACITY="10"
TOTALMATERIAL="20"
LOGFILENAME="logFile1.txt"
MAXPURCHASES="2"



#PORTS
REPOSERVERPORT="22250"
SHOPSERVERPORT="22251"

#IPS
REPOIP="127.0.0.1"

#START
#REPO
(java -classpath dist/T2_TP2_G5.jar main.RepositoryMain $NCUSTOMERS $NCRAFTSMAN $PRODUCTCOST $MATERIALDELIVERYSIZE $BATCHCAPACITY $TOTALMATERIAL $LOGFILENAME $REPOSERVERPORT &)

#SHOP
(java -classpath dist/T2_TP2_G5.jar main.ShopMain $NCUSTOMERS $MAXPURCHASES $REPOIP $REPOSERVERPORT $SHOPSERVERPORT &)
#(java -classpath dist/T2_TP2_G5.jar main.WSMain &)
#(java -classpath dist/T2_TP2_G5.jar main.EntrepreneurMain &)
#(java -classpath dist/T2_TP2_G5.jar main.CustomerMain &)
#(java -classpath dist/T2_TP2_G5.jar main.CraftsmanMain &)




