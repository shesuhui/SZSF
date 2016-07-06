__author__='sherwin.she'
#
import utils

 # search sourceTree,handle  one by one 
def  searchSourceTree(db_conn,ausp_conn,parent_id):
	sql=None
	print(parent_id)
	if parent_id==None:
		sql="select * from ausp_org where parent_id is null order by parent_id"  
	else:
		sql="select * from ausp_org where parent_id='"+parent_id+"'  order by parent_id"  
   	cur=None
   	print(sql)
        try:
               cur=db_conn.cursor()
               cur.execute(sql)
               data=cur.fetchall()
               print(type(data))
               seq=1
               for d in data:
               	        doMigration(ausp_conn,d,parent_id,seq)
               		searchSourceTree(db_conn,ausp_conn,str(d[0]))               		
               		seq+=1
        except Exception,e:
        	print e
            	raise e
        finally:
            	cur.close() 

def doMigration(ausp_conn,orgData,parent_id,seq):
	cur=None
	try:
		insertSql='insert into ausp_org_unit(c_id,c_name,c_short_name,c_parent_id,c_code,c_type,c_state,c_seq,\
                                     c_duty_id,c_comp_id,c_remark,c_creator_id,c_create_time,c_modifier_id,c_modify_time,c_sync)\
                                      values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'
		cur=ausp_conn.cursor()		
		ausp_parent_id=None
		ausp_parent_code=None
	        if not parent_id is None:
	        	data=getOrgIDFromAPSOrgID(ausp_conn,parent_id)
	        	ausp_parent_id=data[0]
	        	ausp_parent_code=data[1]
	        new_org_id=utils.getNewID()
	        org_code=(ausp_parent_code if not ausp_parent_code is None else '' )+format(seq,'04d')
	        print(org_code)
	        #print(orgData)
	        cur.execute("insert into orgMapping (principal_id,org_id,code) values("+str(orgData[0])+",'"+new_org_id+"','"+org_code+"')")
	        newData=(new_org_id,orgData[1],orgData[1],ausp_parent_id,org_code,'1','1',seq,None,None,None,None,utils.now(),None,utils.now(),'1')		      
	        print(newData)
	        cur.execute(insertSql,newData)
                print('insert '+orgData[1]+' now')
		# ausp_conn.commit()
	except Exception, e:
		raise e
	finally:
		if not cur is None:
			cur.close()

def getOrgIDFromAPSOrgID(ausp_conn,apsorgid):
        cur=None
	try:
	        sql='select org_id ,code from  orgMapping where principal_id='+apsorgid
		cur=ausp_conn.cursor()
		cur.execute(sql)
		data=cur.fetchone()
		return data
	except Exception, e:
		raise e
	finally:
		if not cur is None:
			cur.close()	


def  orgMigration(aps_conn,ausp_conn):
	try:
		ausp_conn.cursor().execute("delete from orgmapping")
	      	rootNode_id=None
        	searchSourceTree(aps_conn,ausp_conn,rootNode_id)
        except Exception,e:
            	raise e
        finally:
        	pass

'''
try:
	aps_db_desc='apsuser/apsuser@shesuhui/orcl'
	aps_conn=None
	ausp_conn=None
	ausp_conn=pymysql.connect(host='shesuhui',user='root',passwd='root',db='appman',port=3306,charset='utf8')
	#print("success get ausp_conn")
	if aps_conn is None:
		aps_conn=getApsConnection(aps_db_desc)
	        #print("success get aps_conn")
	orgMigration(aps_conn,ausp_conn)
except Exception, e:
	raise e
finally:
	aps_conn.close()
	ausp_conn.close()
'''
