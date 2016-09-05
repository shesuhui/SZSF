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
import  OA.migrateorgfromOA as migrateorgfromOA
import OA.migrateuserfromOA as migrateuserfromOA



def getConnection(db_desc):
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
		db_desc='newexoa/newexoa@shesuhui/orcl'
		conn=None
		ausp_conn=None
		ausp_conn=pymysql.connect(host='shesuhui',user='root',passwd='root',db='appman',port=3306,charset='utf8')
		#print("success get ausp_conn")
		if conn is None:
			conn=getConnection(db_desc)
		        #print("success get aps_conn")
	        migrateorgfromOA.orgMigration(conn,ausp_conn)
	        userMigrate=migrateuserfromOA.UserMigration(conn,ausp_conn)
	        userMigrate.doMigration()
	        ausp_conn.commit()
	except Exception, e:
		ausp_conn.rollback()
		raise e
	finally:
		conn.close()
		ausp_conn.close()


if __name__=='__main__':
	main()



