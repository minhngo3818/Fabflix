#.PHONY: show-databases create-table set-global-autocommit import-movie-data create-indices commit-and-reset-autocommit check-record-count check-sales-by-date

show-dbs:
	mysql -u mytestuser -p -e "show databases;"
    
sql-createtable:
	mysql -u mytestuser -p < ./scripts/createtable.sql

sql-init-transac:
	mysql -u mytestuser -p -e "SET GLOBAL autocommit = 0;"

sql-init-data:
	mysql -u mytestuser -p --database=moviedb < ./scripts/movie-data.sql

sql-index:
	mysql -u mytestuser -p < ./scripts/create_indices.sql

sql-close-transac:
	mysql -u mytestuser -p -e "COMMIT; SET GLOBAL autocommit = 1;"

sql-count:
	mysql -u mytestuser -p -e "use moviedb;select count(*) from stars;select count(*) from movies;"

sql-sales:
	mysql -u mytestuser -p -e "select * from sales where saleDate = '{demo date}';"

sql-add-ta:
	mysql -u mytestuser -p -e "use moviedb; insert into employees (email, password, fullname) values('classta@email.edu', 'classta', 'TA CS122B');"

sql-procedures:
	mysql -u mytestuser -p < ./scripts/stored_procedure.sql

sql-viewlog-conf:
	mysql -u mytestuser -p -se "SHOW VARIABLES" | grep -e log_error -e general_log -e slow_query_log -e datadir

sql-querylog:
	sudo tail -f /var/lib/mysql/tonysanchez-MS-7C91-slow.log

tomcat-deploy:
	sudo cp ./target/*.war /var/lib/tomcat10/webapps/

tomcat-viewdir:
	ls -lah /var/lib/tomcat10/webapps/

pw-employees:
	mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePassword" -Dexec.args="employees"

pw-customers:
	mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePassword" -Dexec.args="customers"

insert-xml:
	mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="XMLParser.XMLInsertDataProcess"
