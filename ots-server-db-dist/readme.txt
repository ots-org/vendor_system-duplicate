#Database configuration to create the users for IPT SYSTEM

#Run Local Database : This command will delete the existing tables from local database, create tables and insert data into tables of local database
mvn integration-test -Plocal -Ddb.driver=oracle.jdbc.OracleDriver -Ddb.url=jdbc:oracle:thin:@localhost:1521/xe -Ddb.ddlusername=ipt_sys -Ddb.appusername=ipt_sys -Ddrop-ddl -Dcreate-ddl -Dinsert-sql
