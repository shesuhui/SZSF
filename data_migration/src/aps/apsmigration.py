#syn orgunit
#syn role
#syn user
#syn user-role
#syn user-org
__author__='sherwin.she'
#
import os 
os.environ['NLS_LANG'] = 'SIMPLIFIED CHINESE_CHINA.UTF8'
import  cx_Oracle
import pymysql
import uuid
import aps.migrateorgunit
import aps.migraterole
import aps.migrateuser



def getApsConnection(db_desc):
	try:
		conn=cx_Oracle.connect(db_desc)	
		return conn
	except Exception ,e:
		print(e)
	#raise e
	else:
		pass
	finally:
    		pass 		


def main():
	try:
		aps_db_desc='apsuser/apsuser@shesuhui/orcl'
		aps_conn=None
		ausp_conn=None
		ausp_conn=pymysql.connect(host='shesuhui',user='root',passwd='root',db='appman',port=3306,charset='utf8')
		#print("success get ausp_conn")
		if aps_conn is None:
			aps_conn=getApsConnection(aps_db_desc)
		        #print("success get aps_conn")
		#syn orgunit
		migrateorgunit.orgMigration(aps_conn,ausp_conn)
		#syn role
		role=migraterole.RoleMigration(aps_conn,ausp_conn)
		role.doMigration()
		user=migrateuser.UserMigration(aps_conn, ausp_conn)
		user.doMigration()
		ausp_conn.commit()
	except Exception, e:
		raise e
		ausp_conn.rollback()
	finally:
		aps_conn.close()
		ausp_conn.close()



main()



